package com.syntax.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.syntax.note.login.SigninActivity;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;

public class Profile extends AppCompatActivity {

    Toolbar toolbar;

    EditText name , email , phone;

    TextView logout , change;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        toolbar = findViewById(R.id.toolbar2);

        name = findViewById(R.id.textView11);
        email = findViewById(R.id.textView13);
        phone = findViewById(R.id.textView15);
        logout = findViewById(R.id.textView17);
        change = findViewById(R.id.textView16);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setPadding(10, 0, 0, 0);
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.m_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // b.mylist.remove(b.mylist.size() - 1);

                finish();

            }
        });

        name.setText(SharePreferenceUtils.getInstance().getString(Constant.USER_name));
        email.setText(SharePreferenceUtils.getInstance().getString(Constant.USER_email));
        phone.setText(SharePreferenceUtils.getInstance().getString(Constant.USER_phone));


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(Profile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.quit_dialog_layout);
                dialog.show();

                Button ookk = dialog.findViewById(R.id.button2);
                Button canc = dialog.findViewById(R.id.button4);

                canc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ookk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(Profile.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        SharePreferenceUtils.getInstance().deletePref();
                        Intent intent = new Intent(Profile.this , SigninActivity.class);
                        startActivity(intent);
                        finishAffinity();

                    }
                });




            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile.this , ChangePassword.class);
                startActivity(intent);

            }
        });

    }
}
