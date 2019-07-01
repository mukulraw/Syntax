package com.syntax.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.syntax.note.changePasswordPOJO.Data;
import com.syntax.note.changePasswordPOJO.changePasswordBean;
import com.syntax.note.signinResponsePOJO.signinResponseBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePassword extends AppCompatActivity {

    Toolbar toolbar;
    EditText password , confirm;
    Button submit;

    TextInputLayout pp , cc;

    ProgressBar progress;
    ServiceInterface serviceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = findViewById(R.id.toolbar2);
        password = findViewById(R.id.syntax_password);
        confirm = findViewById(R.id.syntax_rePassword);
        submit = findViewById(R.id.syntax_signupBtn);
        progress = findViewById(R.id.progressBar);
        pp = findViewById(R.id.inuptLayoutPassword);
        cc = findViewById(R.id.inuptLayoutRepassword);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setPadding(10, 0, 0, 0);
        toolbar.setTitle("Change Password");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.m_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // b.mylist.remove(b.mylist.size() - 1);

                finish();

            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String p = password.getText().toString();
                String c = confirm.getText().toString();

                if (p.length() > 0)
                {

                    if (c.equals(p))
                    {

                        progress.setVisibility(View.VISIBLE);

                        changePasswordBean body = new changePasswordBean();
                        body.setAction("reset_password");
                        Data data = new Data();

                        data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
                        data.setPassword(p);
                        body.setData(data);


                        Call<signinResponseBean> call = serviceInterface.resetPassword(body);

                        call.enqueue(new Callback<signinResponseBean>() {
                            @Override
                            public void onResponse(Call<signinResponseBean> call, Response<signinResponseBean> response) {

                                Toast.makeText(ChangePassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                progress.setVisibility(View.GONE);

                                finish();

                            }

                            @Override
                            public void onFailure(Call<signinResponseBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });


                    }
                    else
                    {
                        cc.setError("Passwords did not match");
                    }

                }
                else
                {
                    pp.setError("Invalid password");
                }

            }
        });



    }
}
