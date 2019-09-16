package com.syntax.note.login;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syntax.note.HomeActivity2;
import com.syntax.note.R;
import com.syntax.note.home.HomeActivity;
import com.syntax.note.signinRequestPOJO.signinRequestBean;
import com.syntax.note.signinRequestPOJO.Data;
import com.syntax.note.signinResponsePOJO.signinResponseBean;
import com.syntax.note.socialRequestPOJO.socialRequestBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SigninActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 121;
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

    GoogleSignInClient mGoogleSignInClient;

    ImageButton google;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        setUpWidget();
        pBar.setVisibility(View.GONE);
        getData();


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



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
                //finish();
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            String email = account.getEmail();
            String pid = account.getId();


            Log.d("email" , email);
            Log.d("pid" , pid);

            pBar.setVisibility(View.VISIBLE);

            socialRequestBean body = new socialRequestBean();
            body.setAction("social_login");
            com.syntax.note.socialRequestPOJO.Data data = new com.syntax.note.socialRequestPOJO.Data();

            data.setEmail(email);
            data.setPid(pid);
            body.setData(data);


            Call<signinResponseBean> call = serviceInterface.socialsignin(body);


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
                        Intent homeIntent = new Intent(SigninActivity.this, HomeActivity2.class);
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


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("asdad", "signInResult:failed code=" + e.getStatusCode());

        }
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
                    Intent homeIntent = new Intent(SigninActivity.this,HomeActivity2.class);
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
        google = findViewById(R.id.google);

        pBar = findViewById(R.id.progressBar);
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
            //  Toast.makeText(AddPropertyActivity.this, "Datum Verified", Toast.LENGTH_SHORT).show();
        }
    }

}
