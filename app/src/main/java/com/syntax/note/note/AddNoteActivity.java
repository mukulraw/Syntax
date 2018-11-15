package com.syntax.note.note;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.syntax.note.R;
import com.syntax.note.addNoteRequestPOJO.addNoteRequestBean;
import com.syntax.note.addNoteResponsePOJO.addNoteResponseBean;
import com.syntax.note.categoryRequestPOJO.CategoryRequestBean;
import com.syntax.note.categoryRequestPOJO.Data;
import com.syntax.note.categoryResponsePOJO.CategoryResponseBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNoteActivity extends AppCompatActivity {

    Spinner spinCategory;
  ArrayList<String> cat = new ArrayList<>();
    Retrofit retrofit;
    ServiceInterface serviceInterface;
    EditText title,desc;
    Button submit;
    String mTitle,mDesc,mCatId;
    String mUserId;
    ConstraintLayout rootlayout;
  //  private TextInputLayout inputLayoutTitle,inputLayoutDesc;
    boolean isValid=false;
    String item;
    ProgressBar pBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note2);
        /*// Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(" Note");
       mToolbar.setNavigationIcon(R.drawable.m_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        //back button
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setPadding(10,0,0,0);
        mToolbar.setTitle("Add Note");
       // mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.m_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // b.mylist.remove(b.mylist.size() - 1);

                finish();

            }
        });

        //Retrofit
        setupWidget();
        pBar.setVisibility(View.GONE);
        getData();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);
        getCategory();

        spinCategory = findViewById(R.id.spinCategory);

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dataValidation();
               if (isValid) {
                   addNoteReq();
                   pBar.setVisibility(View.VISIBLE);
               }else
               {
                   Toast.makeText(AddNoteActivity.this, "Fill All Details First..", Toast.LENGTH_SHORT).show();
               }
           }
       });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.category));//setting the country_array to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapter);
      //if you want to set any action you can do in this listener
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                int r=position;
                mCatId =String.valueOf(r+1);
                 item= String.valueOf( arg0.getItemAtPosition(position));
               // Toast.makeText(AddNoteActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }



    private void addNoteReq() {
        addNoteRequestBean body = new addNoteRequestBean();
        body.setAction("create_note");
        com.syntax.note.addNoteRequestPOJO.Data data = new com.syntax.note.addNoteRequestPOJO.Data();
        data.setCatId(mCatId);
        data.setTitle(mTitle);
        data.setDesc(mDesc);
        data.setUserId(mUserId);
        body.setData(data);


        Call<addNoteResponseBean> call = serviceInterface.addNote(body);
        call.enqueue(new Callback<addNoteResponseBean>() {
            @Override
            public void onResponse(Call<addNoteResponseBean> call, Response<addNoteResponseBean> response) {
                if (response.body().getStatus().equals("1"))
                {
                    pBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(rootlayout,""+response.body().getMessage(),Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.seaGreen));
                    snackbar.show();
                    title.setText("");
                    desc.setText("");
                }
                else
                {
                    pBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(rootlayout,""+response.body().getMessage(),Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.seaGreen));
                    snackbar.show();

                    }
            }

            @Override
            public void onFailure(Call<addNoteResponseBean> call, Throwable t) {
                pBar.setVisibility(View.GONE);

            }
        });
    }


    private void getCategory() {

        CategoryRequestBean body = new CategoryRequestBean();
        body.setAction("category_list");
        Data data = new Data();

         mUserId= SharePreferenceUtils.getInstance().getString(Constant.USER_id);

        data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
      //  Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
        body.setData(data);

        Call<CategoryResponseBean> call = serviceInterface.getCategory(body);
        call.enqueue(new Callback<CategoryResponseBean>() {
            @Override
            public void onResponse(Call<CategoryResponseBean> call, Response<CategoryResponseBean> response) {
                assert response.body() != null;
                if(response.body().getStatus().equals("1"))
                {
                   // Toast.makeText(AddNoteActivity.this, "sucess", Toast.LENGTH_SHORT).show();

                    for (int i=0;i<response.body().getData().size();i++)
                    {
                        cat.add (String.valueOf(response.body().getData().indexOf(i)));
                    }

                }else
                {
                    Toast.makeText(AddNoteActivity.this, "else", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponseBean> call, Throwable t) {
                Toast.makeText(AddNoteActivity.this, "api response fail"+t, Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void setupWidget() {
        title = findViewById(R.id.title_text);
        desc = findViewById(R.id.desc);
       // inputLayoutTitle = findViewById(R.id.inputLayoutTitle);
       // inputLayoutDesc = findViewById(R.id.inputLayoutDesc);
        submit = findViewById(R.id.submit);
        rootlayout = findViewById(R.id.rootlayout);
        pBar = (ProgressBar)findViewById(R.id.progressBar);


    }

    private void getData() {
        mTitle = title.getText().toString().trim();
        mDesc =desc.getText().toString().trim();
    }

    private void dataValidation() {
        isValid = true;

        if (title.getText().toString().isEmpty()) {
           // title.setError("Title Missing");
            isValid = false;
        } else {
            //inputLayoutTitle.setErrorEnabled(false);
        }
        if (desc.getText().toString().isEmpty()) {
           // inputLayoutDesc.setError("Description Missing");
            isValid = false;
        } else {
           // inputLayoutDesc.setErrorEnabled(false);
        }


    }

}