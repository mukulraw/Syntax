package com.syntax.note;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.syntax.note.allNoteResponsePOJO.allNoteResponseBean;
import com.syntax.note.deleteNoteRequestPOJO.Data;
import com.syntax.note.deleteNoteRequestPOJO.deleteNoteRequestBean;
import com.syntax.note.home.HomeActivity;
import com.syntax.note.searchResultPOJO.searchResultBean;
import com.syntax.note.updateNoteRequestPOJO.updateNoteRequestBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleNote extends AppCompatActivity {

    Toolbar toolbar;
    EditText note;
    ProgressBar progress;
    Button update;
    String id;
    String catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);

        id = getIntent().getStringExtra("id");
        catId = getIntent().getStringExtra("catid");

        toolbar = findViewById(R.id.toolbar2);
        note = findViewById(R.id.note);
        progress = findViewById(R.id.progressBar2);
        update = findViewById(R.id.button);


        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.m_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        note.setText(getIntent().getStringExtra("note"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String n = note.getText().toString();

                if (n.length() > 0) {

                    progress.setVisibility(View.VISIBLE);

                    updateNoteRequestBean body = new updateNoteRequestBean();

                    body.setAction("edit_note");
                    com.syntax.note.updateNoteRequestPOJO.Data data = new com.syntax.note.updateNoteRequestPOJO.Data();

                    data.setCatId(catId);
                    data.setDescription(n);
                    data.setNoteId(id);
                    data.setTitle(getIntent().getStringExtra("title"));
                    data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
                    body.setData(data);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constant.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

                    Call<searchResultBean> call = serviceInterface.update(body);

                    call.enqueue(new Callback<searchResultBean>() {
                        @Override
                        public void onResponse(Call<searchResultBean> call, Response<searchResultBean> response) {

                            if (response.body().getStatus().equals("1"))
                            {
                                Toast.makeText(SingleNote.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<searchResultBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, note.getText().toString());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.delete) {

            deleteNote();

        }

        return super.onOptionsItemSelected(item);

    }


    public void deleteNote() {

        progress.setVisibility(View.VISIBLE);

        deleteNoteRequestBean body = new deleteNoteRequestBean();
        body.setAction("delete_note");
        Data data = new Data();
        data.setNoteId(id);
        data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
        body.setData(data);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

        Call<allNoteResponseBean> call = serviceInterface.deleteNote(body);

        call.enqueue(new Callback<allNoteResponseBean>() {
            @Override
            public void onResponse(Call<allNoteResponseBean> call, Response<allNoteResponseBean> response) {

                if (response.body().getStatus().equals("1")) {
                    Toast.makeText(SingleNote.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<allNoteResponseBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

}
