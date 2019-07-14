package com.syntax.note;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.syntax.note.allNoteResponsePOJO.allNoteResponseBean;
import com.syntax.note.categoryRequestPOJO.CategoryRequestBean;
import com.syntax.note.categoryRequestPOJO.Data;
import com.syntax.note.categoryResponsePOJO.CategoryResponseBean;
import com.syntax.note.deleteNoteRequestPOJO.deleteNoteRequestBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrashDetails extends AppCompatActivity {

    Toolbar toolbar;
    TextView note , title , cat;
    ProgressBar progress;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_details);

        id = getIntent().getStringExtra("id");

        toolbar = findViewById(R.id.toolbar2);
        note = findViewById(R.id.note);
        cat = findViewById(R.id.cat);
        title = findViewById(R.id.title_text);
        progress = findViewById(R.id.progressBar2);


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
        title.setText(getIntent().getStringExtra("title"));
        cat.setText(getIntent().getStringExtra("cat"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trash_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.restore) {

            recover();

        } else if (id == R.id.delete) {

            deleteNote();

        }

        return super.onOptionsItemSelected(item);

    }


    public void deleteNote() {

        progress.setVisibility(View.VISIBLE);

        CategoryRequestBean body = new CategoryRequestBean();
        body.setAction("delete_trash_note");
        Data data = new Data();

        data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
        //  Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
        body.setData(data);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

        Call<CategoryResponseBean> call = serviceInterface.emptyTrash(body);

        call.enqueue(new Callback<CategoryResponseBean>() {
            @Override
            public void onResponse(Call<CategoryResponseBean> call, Response<CategoryResponseBean> response) {

                if (response.body().getStatus().equals("1")) {
                    Toast.makeText(TrashDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<CategoryResponseBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    public void recover() {

        progress.setVisibility(View.VISIBLE);

        deleteNoteRequestBean body = new deleteNoteRequestBean();
        body.setAction("recover_note");
        com.syntax.note.deleteNoteRequestPOJO.Data data = new com.syntax.note.deleteNoteRequestPOJO.Data();
        data.setNoteId(id);
        data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
        body.setData(data);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

        Call<allNoteResponseBean> call = serviceInterface.recoverNote(body);

        call.enqueue(new Callback<allNoteResponseBean>() {
            @Override
            public void onResponse(Call<allNoteResponseBean> call, Response<allNoteResponseBean> response) {

                if (response.body().getStatus().equals("1")) {
                    Toast.makeText(TrashDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
