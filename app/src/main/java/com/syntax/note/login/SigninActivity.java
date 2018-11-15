package com.syntax.note.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.syntax.note.R;
import com.syntax.note.home.HomeActivity;
import com.syntax.note.signinRequestPOJO.signinRequestBean;
import com.syntax.note.signinRequestPOJO.Data;
import com.syntax.note.signinResponsePOJO.signinResponseBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SigninActivity extends AppCompatActivity {
    EditText syntaxSigninEmail,syntaxSigninPassword;
    Button syntaxSigninBtn;
    String syntax_email,syntax_password;
    RelativeLayout rootlayout;

    TextView forgotPassword,signupNow;
    private TextInputLayout inputLayoutSignin,inputLayoutPassword;

    Retrofit retrofit;
    ServiceInterface serviceInterface;
    boolean isValid=false;
    private ProgressBar pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        setUpWidget();
        pBar.setVisibility(View.GONE);
        getData();

        //Retrofit

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);

        syntaxSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                dataValidation();
                if(isValid)
                {
                    signin();
                    pBar.setVisibility(View.VISIBLE);
                }


            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotIntent = new Intent(SigninActivity.this,ForgotPassword.class);
                startActivity(forgotIntent);

            }
        });

        signupNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(SigninActivity.this,SignupActivity.class);
                startActivity(signupIntent);
                finish();
            }
        });

    }

    private void signin() {
        signinRequestBean body = new signinRequestBean();
        body.setAction("login");
        Data data = new Data();

        data.setEmail(syntax_email);
        data.setPassword(syntax_password);
        body.setData(data);


        Call<signinResponseBean> call = serviceInterface.signin(body);
        call.enqueue(new Callback<signinResponseBean>() {
            @Override
            public void onResponse(Call<signinResponseBean> call, Response<signinResponseBean> response) {
                if (response.body().getStatus().equals("1"))
                {
                    pBar.setVisibility(View.GONE);
                   // Toast.makeText(SigninActivity.this, ""+response.body()
                          //  .getData().getName(), Toast.LENGTH_SHORT).show();
                    Log.i("signin",response.body().getData().getEmail());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_id,response.body().getData().getUserId());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_email,response.body().getData().getEmail());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_name,response.body().getData().getName());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_phone,response.body().getData().getMobile());
                    Intent homeIntent = new Intent(SigninActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
                else
                {
                    pBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(rootlayout,"Login Failed Check Login Credential",Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.seaGreen));
                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<signinResponseBean> call, Throwable t) {
                pBar.setVisibility(View.GONE);

            }
        });


    }


    private void setUpWidget() {
        syntaxSigninEmail = findViewById(R.id.signin_email);
        syntaxSigninPassword = findViewById(R.id.signin_password);
        syntaxSigninBtn = findViewById(R.id.signin_button);
        forgotPassword = findViewById(R.id.forgotPassword);
        signupNow = findViewById(R.id.signupNow);
        rootlayout = findViewById(R.id.rootlayout);
        inputLayoutSignin = findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);

        pBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    private void getData() {
        syntax_email = syntaxSigninEmail.getText().toString().trim();
        syntax_password = syntaxSigninPassword.getText().toString().trim();
    }

    private void dataValidation() {
        isValid = true;

        if (syntaxSigninEmail.getText().toString().isEmpty()) {
            inputLayoutSignin.setError("Email id Missing");
            isValid = false;
        } else {
            inputLayoutSignin.setErrorEnabled(false);
        }

        if (syntaxSigninPassword.getText().toString().isEmpty()) {
            inputLayoutPassword.setError("Password Missing");
            isValid = false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }



        if (isValid) {
            //  Toast.makeText(AddPropertyActivity.this, "Data Verified", Toast.LENGTH_SHORT).show();
        }
    }

}
