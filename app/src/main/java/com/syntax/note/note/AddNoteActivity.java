package com.syntax.note.note;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.syntax.note.R;
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
    private TextInputLayout inputLayoutTitle,inputLayoutDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note2);
        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(" Note");
      //  mToolbar.setNavigationIcon(R.id.back);

        //Retrofit
        setupWidget();
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
               addNoteReq();
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
               // Toast.makeText(AddNoteActivity.this, ""+mCatId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void addNoteReq() {
    }


    private void getCategory() {

        CategoryRequestBean body = new CategoryRequestBean();
        body.setAction("category_list");
        Data data = new Data();

        String a= SharePreferenceUtils.getInstance().getString(Constant.USER_id);

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
                Toast.makeText(AddNoteActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void setupWidget() {
        title = findViewById(R.id.title_text);
        desc = findViewById(R.id.desc);
        inputLayoutTitle = findViewById(R.id.inputLayoutTitle);
        inputLayoutDesc = findViewById(R.id.inputLayoutDesc);
        submit = findViewById(R.id.submit);

    }

    private void getData() {
        mTitle = title.getText().toString().trim();
        mDesc =desc.getText().toString().trim();
    }



}