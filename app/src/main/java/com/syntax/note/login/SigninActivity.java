package com.syntax.note.login;

import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

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
    private CallbackManager mCallbackManager;
    TextView forgotPassword,signupNow;


    Retrofit retrofit;
    ServiceInterface serviceInterface;
    boolean isValid=false;
    private ProgressBar pBar;

    GoogleSignInClient mGoogleSignInClient;

    Button google , facebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                facebooklogin(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(SigninActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(SigninActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logInWithReadPermissions(SigninActivity.this, Arrays.asList("public_profile"));

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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


    public void facebooklogin(final LoginResult loginResult) {

        Log.d("Success", "Login");
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response1) {
                        Log.i("MainActivity", "@@@response: " + response1.toString());
                        String email;

                        try {

                            final String name = object.getString("name");
                            final String id = object.getString("id");
                            if (object.has("email")) {
                                email = object.getString("email");
                            } else {
                                email = id;


                            }

                            Log.d("email" , email);
                            Log.d("pid" , id);

                            pBar.setVisibility(View.VISIBLE);

                            socialRequestBean body = new socialRequestBean();
                            body.setAction("social_login");
                            com.syntax.note.socialRequestPOJO.Data data = new com.syntax.note.socialRequestPOJO.Data();

                            data.setEmail(email);
                            data.setPid(id);
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
                                    Log.d("error" , t.toString());
                                }
                            });



                            //Toast.makeText(CreateAccount.this, pic, Toast.LENGTH_SHORT).show();


                            //socialSignin(name , id , email);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();


    }


    private void setUpWidget() {
        syntaxSigninEmail = findViewById(R.id.signin_email);
        syntaxSigninPassword = findViewById(R.id.signin_password);
        syntaxSigninBtn = findViewById(R.id.signin_button);
        forgotPassword = findViewById(R.id.forgotPassword);
        signupNow = findViewById(R.id.signupNow);
        rootlayout = findViewById(R.id.rootlayout);
        google = findViewById(R.id.google);
        facebook = findViewById(R.id.facebook);

        pBar = findViewById(R.id.progressBar);
    }

    private void getData() {
        syntax_email = syntaxSigninEmail.getText().toString().trim();
        syntax_password = syntaxSigninPassword.getText().toString().trim();
    }

    private void dataValidation() {
        isValid = true;

        if (syntaxSigninEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {

        }

        if (syntaxSigninPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {

        }



        if (isValid) {
            //  Toast.makeText(AddPropertyActivity.this, "Datum Verified", Toast.LENGTH_SHORT).show();
        }
    }

}
