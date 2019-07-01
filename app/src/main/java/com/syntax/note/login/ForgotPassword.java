package com.syntax.note.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.syntax.note.R;
import com.syntax.note.forgotpassRequestPOJO.Data;
import com.syntax.note.forgotpassRequestPOJO.ForgotpassRequestBean;
import com.syntax.note.forgotpassResponsePOJO.ForgotpassResponseBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.webServices.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPassword extends AppCompatActivity {

    Retrofit retrofit;
    ServiceInterface serviceInterface;

    EditText syntax_forgotpass;
    String forgotemail;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setUpWidget();
        getdata();


        //Retrofit

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
                forgotpassword();
            }
        });
    }

    private void forgotpassword() {

        ForgotpassRequestBean body = new ForgotpassRequestBean();
        Data data = new Data();

        body.setAction("forgot_password");
        data.setEmail(forgotemail);

        body.setData(data);

        Call<ForgotpassResponseBean> call = serviceInterface.forgotpassword(body);

        call.enqueue(new Callback<ForgotpassResponseBean>() {
            @Override
            public void onResponse(Call<ForgotpassResponseBean> call, Response<ForgotpassResponseBean> response) {
                if (response.body().getStatus().equals("1"))
                {

                    Toast.makeText(ForgotPassword.this, "mail send on  "+response.body().getData().getEmail(), Toast.LENGTH_SHORT).show();

                    Intent signinIntent =new Intent(ForgotPassword.this,SigninActivity.class);
                    startActivity(signinIntent);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ForgotpassResponseBean> call, Throwable t) {

            }
        });

    }

    private void getdata() {
        forgotemail = syntax_forgotpass.getText().toString().trim();
    }

    private void setUpWidget() {

        syntax_forgotpass = findViewById(R.id.forgot_password);
        submit = findViewById(R.id.submitBtn);


    }
}
