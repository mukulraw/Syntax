package com.syntax.note.note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.syntax.note.R;
import com.syntax.note.trashRequestPOJO.Data;
import com.syntax.note.trashRequestPOJO.TrashRequestBean;
import com.syntax.note.trashResponsePOJO.TrashResponseBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrashActivity extends AppCompatActivity {
    ServiceInterface serviceInterface;
    Retrofit retrofit;
    String userId;
    ArrayList trashList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setPadding(10, 0, 0, 0);
        mToolbar.setTitle(" Trash");
        // mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.m_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // b.mylist.remove(b.mylist.size() - 1);

                finish();

            }
        });

        userId = SharePreferenceUtils.getInstance().getString(Constant.USER_id);
       // Toast.makeText(this, "" + userId, Toast.LENGTH_SHORT).show();
        Log.i("userid", userId);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);
        trashList=new ArrayList();

        trashReq();


    }

    private void trashReq() {

        TrashRequestBean body = new TrashRequestBean();
        Data data = new Data();
        body.setAction("trash_list");

        data.setUserId(userId);

        body.setData(data);

        Gson gson = new Gson();

        Log.i("abc", gson.toJson(body));


        Call<TrashResponseBean> call = serviceInterface.trash(body);
        call.enqueue(new Callback<TrashResponseBean>() {
            @Override
            public void onResponse(Call<TrashResponseBean> call, Response<TrashResponseBean> response) {
                if (response.body().getStatus().equals("1")) {
                    // Toast.makeText(TrashActivity.this, "sucess", Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < response.body().getData().size(); i++) {

                        trashList.add(response.body().getData().get(i).getId());
                        trashList.add(response.body().getData().get(i).getTitle());
                        trashList.add(response.body().getData().get(i).getDesc());
                        trashList.add(response.body().getData().get(i).getCatName());
                        trashList.add(response.body().getData().get(i).getCreateDate());

                    }
                    Gson gson1 =new Gson();
                    Log.i("xyz",gson1.toJson(trashList));


                } else {
                    Log.i("error", response.body().getMessage());

                }
            }

            @Override
            public void onFailure(Call<TrashResponseBean> call, Throwable t) {
                Toast.makeText(TrashActivity.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });


    }


}
