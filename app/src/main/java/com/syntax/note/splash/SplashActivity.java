package com.syntax.note.splash;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.syntax.note.HomeActivity2;
import com.syntax.note.R;
import com.syntax.note.home.HomeActivity;
import com.syntax.note.login.SigninActivity;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash3);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);


                    if (SharePreferenceUtils.getInstance().getString(Constant.USER_id).equalsIgnoreCase("")){
                        // not registted user  so show login screen
                        Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        // home sscreen
                        Intent intent = new Intent(SplashActivity.this, HomeActivity2.class);
                        startActivity(intent);
                        finish();
                    }

                   /* Intent i = new Intent(SplashActivity.this, SignupActivity.class);
                    startActivity(i);
                    finish();
*/


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
