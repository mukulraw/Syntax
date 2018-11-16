package com.syntax.note.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.syntax.note.R;
import com.syntax.note.home.HomeActivity;
import com.syntax.note.signupRequestPOJO.Data;
import com.syntax.note.signupRequestPOJO.signupRequestBean;
import com.syntax.note.signupResponsePOJO.signupResponseBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {
    Retrofit retrofit;

    ServiceInterface serviceInterface;

    EditText syntaxName,syntaxEmail,syntaxPhone,syntaxPassword,syntaxRePassword;
    String syntax_name,syntax_email,syntax_phone,syntax_password,syntax_repassword;
    Button syntaxSignupBtn;
    TextView syntaxSigninNow;
    RelativeLayout rootlayout;
    private TextInputLayout inuptLayoutName,inuptLayoutEmail,inuptLayoutPhone,inuptLayoutPassword,
            inuptLayoutRepassword;

    boolean isValid=false;

    ProgressBar pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setUpWidget();
        pBar.setVisibility(View.GONE);

        //Retrofit

         retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         serviceInterface = retrofit.create(ServiceInterface.class);

        syntaxSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                dataValidation();
                if (isValid)
                {
                    if(syntax_password.equals(syntax_repassword)) {
                        signup();
                        pBar.setVisibility(View.VISIBLE);
                    }else
                    {
                        Toast.makeText(SignupActivity.this, "password mismatch", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        syntaxSigninNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(signinIntent);
                finish();
            }
        });



    }

    private void signup() {

        signupRequestBean body = new signupRequestBean();
        body.setAction("register");
        Data data = new Data();

        data.setEmail(syntax_email);
        data.setName(syntax_name);
        data.setPassword(syntax_password);
        data.setPhone(syntax_phone);
        body.setData(data);

        // Call<signupResponseBean> call =



        Call<signupResponseBean> call = serviceInterface.signup(body);

        call.enqueue(new Callback<signupResponseBean>() {
            @Override
            public void onResponse(Call<signupResponseBean> call, Response<signupResponseBean> response) {
                if (response.body().getStatus().equals("1"))
                {
                   pBar.setVisibility(View.GONE);
                    Toast.makeText(SignupActivity.this, ""+response.body().getData().getName(), Toast.LENGTH_SHORT).show();

                    SharePreferenceUtils.getInstance().saveString(Constant.USER_id,response.body().getData().getUserId());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_email,response.body().getData().getEmail());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_name,response.body().getData().getName());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_phone,response.body().getData().getMobile());
                    Intent homeIntent = new Intent(SignupActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    finish();

                   /* Intent signinIntent = new Intent(SignupActivity.this,SigninActivity.class);
                    startActivity(signinIntent);*/
                }
                else
                {
                    pBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(rootlayout,"Signup Failed Already Registered",Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.seaGreen));
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<signupResponseBean> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(SignupActivity.this, ""+"failed", Toast.LENGTH_SHORT).show();

            }
        });




    }


    private void setUpWidget() {

        syntaxName = findViewById(R.id.syntax_name);
        syntaxEmail = findViewById(R.id.syntax_email);
        syntaxPhone =findViewById(R.id.syntax_phone);
        syntaxPassword =  findViewById(R.id.syntax_password);
        syntaxRePassword = findViewById(R.id.syntax_rePassword);
        syntaxSignupBtn = findViewById(R.id.syntax_signupBtn);
        syntaxSigninNow = findViewById(R.id.syntax_signinNow);
        rootlayout = findViewById(R.id.rootlayout);

        inuptLayoutName =findViewById(R.id.inuptLayoutName);
        inuptLayoutEmail = findViewById(R.id.inuptLayoutEmail);
        inuptLayoutPhone = findViewById(R.id.inuptLayoutPhone);
        inuptLayoutPassword = findViewById(R.id.inuptLayoutPassword);
        inuptLayoutRepassword = findViewById(R.id.inuptLayoutRepassword);
        pBar = findViewById(R.id.progressBar);


    }
    private void getData() {
        syntax_name = syntaxName.getText().toString().trim();
        syntax_email = syntaxEmail.getText().toString().trim();
        syntax_password = syntaxPassword.getText().toString().trim();
        syntax_repassword = syntaxRePassword.getText().toString().trim();
        syntax_phone = syntaxPhone.getText().toString().trim();
    }


    private void dataValidation() {
        isValid = true;

        if (syntaxName.getText().toString().isEmpty()) {
            inuptLayoutName.setError("Name Missing");
            isValid = false;
        } else {
            inuptLayoutName.setErrorEnabled(false);
        }

        if (syntaxEmail.getText().toString().isEmpty()) {
            inuptLayoutEmail.setError("Email Missing");
            isValid = false;
        } else {
            inuptLayoutEmail.setErrorEnabled(false);
        }

        if (syntaxPassword.getText().toString().isEmpty()) {
            inuptLayoutPassword.setError("Input Password");
            isValid = false;
        } else {
            inuptLayoutPassword.setErrorEnabled(false);
        }
        if (syntaxRePassword.getText().toString().isEmpty()) {
            inuptLayoutRepassword.setError("Input Password");
            isValid = false;
        } else {
            inuptLayoutRepassword.setErrorEnabled(false);
        }

        if (syntaxPhone.getText().toString().isEmpty()) {
            inuptLayoutPhone.setError("Mobile Number Missing");
            isValid = false;
        } else {
            inuptLayoutPhone.setErrorEnabled(false);
        }



        if (isValid) {
            //  Toast.makeText(AddPropertyActivity.this, "Datum Verified", Toast.LENGTH_SHORT).show();
        }
    }

}
