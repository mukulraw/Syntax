package com.syntax.note;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.syntax.note.allNoteResponsePOJO.allNoteResponseBean;
import com.syntax.note.categoryRequestPOJO.CategoryRequestBean;
import com.syntax.note.categoryResponsePOJO.CategoryResponseBean;
import com.syntax.note.deleteNoteRequestPOJO.Data;
import com.syntax.note.deleteNoteRequestPOJO.deleteNoteRequestBean;
import com.syntax.note.note.AddNoteActivity;
import com.syntax.note.searchResultPOJO.searchResultBean;
import com.syntax.note.updateNoteRequestPOJO.updateNoteRequestBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleNote extends AppCompatActivity {

    Toolbar toolbar;
    EditText note , title;
    ProgressBar progress;
    Button update;
    String id;
    String cat1 , cat;
    Spinner spinCategory;
    Retrofit retrofit;
    ServiceInterface serviceInterface;

    ArrayList<String> catId = new ArrayList<>();
    ArrayList<String> catName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);

        catId = new ArrayList<>();
        catName = new ArrayList<>();


        id = getIntent().getStringExtra("id");
        cat1 = getIntent().getStringExtra("catid");

        toolbar = findViewById(R.id.toolbar);
        note = findViewById(R.id.desc);
        spinCategory = findViewById(R.id.spinCategory);
        progress = findViewById(R.id.progressBar);
        update = findViewById(R.id.submit);
        title = findViewById(R.id.title_text);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String n = note.getText().toString();

                if (n.length() > 0) {

                    progress.setVisibility(View.VISIBLE);

                    updateNoteRequestBean body = new updateNoteRequestBean();

                    body.setAction("edit_note");
                    com.syntax.note.updateNoteRequestPOJO.Data data = new com.syntax.note.updateNoteRequestPOJO.Data();

                    data.setCatId(cat);
                    data.setDescription(n);
                    data.setNoteId(id);
                    data.setTitle(title.getText().toString());
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

        getCategory();

        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {

                cat = catId.get(position);

                // Toast.makeText(AddNoteActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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

    private void getCategory() {

        CategoryRequestBean body = new CategoryRequestBean();
        body.setAction("category_list");
        com.syntax.note.categoryRequestPOJO.Data data = new com.syntax.note.categoryRequestPOJO.Data();


        data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
        //  Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
        body.setData(data);

        Call<CategoryResponseBean> call = serviceInterface.getCategory(body);
        call.enqueue(new Callback<CategoryResponseBean>() {
            @Override
            public void onResponse(Call<CategoryResponseBean> call, Response<CategoryResponseBean> response) {
                assert response.body() != null;
                if (response.body().getStatus().equals("1")) {

                    catId.clear();
                    catName.clear();

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        catId.add(response.body().getData().get(i).getId());
                        catName.add(response.body().getData().get(i).getCatName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SingleNote.this,
                            android.R.layout.simple_spinner_item, catName);//setting the country_array to spinner
                    // string value
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinCategory.setAdapter(adapter);


                    for (int i = 0; i < response.body().getData().size(); i++) {

                        if (response.body().getData().get(i).getId().equals(cat1))
                        {
                            spinCategory.setSelection(i);
                        }

                    }


                }

            }

            @Override
            public void onFailure(Call<CategoryResponseBean> call, Throwable t) {
                Toast.makeText(SingleNote.this, "api response fail" + t, Toast.LENGTH_SHORT).show();
            }
        });


    }

}
