package com.syntax.note.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.syntax.note.MyApplication;
import com.syntax.note.R;
import com.syntax.note.login.SigninActivity;
import com.syntax.note.note.AddNoteActivity;
import com.syntax.note.note.TrashActivity;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Drawer Layout
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton fab;
    AlertDialog.Builder builder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(" Note");

        //fab button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNoteIntent = new Intent(HomeActivity.this,AddNoteActivity.class);
                startActivity(addNoteIntent);
            }
        });

        String userid=SharePreferenceUtils.getInstance().getString(Constant.USER_id);
       // Toast.makeText(this, ""+userid, Toast.LENGTH_SHORT).show();

       //DrawerLayout
        @SuppressLint("CutPasteId")
        NavigationView navigationView = findViewById(R.id.navigationView);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String itemName = (String) item.getTitle();
        // Toast.makeText(HomeActivity.this, ""+itemName, Toast.LENGTH_SHORT).show();
        closeDrawer();
        switch (item.getItemId()) {
           /* case R.id.save:
                break;*/
            case R.id.menu_home:
              /*  Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profileIntent);*/
                Toast.makeText(this, "clicked home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_note:
                Intent addNoteIntent = new Intent(HomeActivity.this,AddNoteActivity.class);
                startActivity(addNoteIntent);
                break;
            case R.id.menu_trash:
               /* SharePreferenceUtils.getInstance().deletePref();
                Intent signIntent = new Intent(HomeActivity.this, SigninActivity.class);
                startActivity(signIntent);
                finish();*/

               Intent trashIntent = new Intent(HomeActivity.this,TrashActivity.class);
               startActivity(trashIntent);
                Toast.makeText(this, "clicked Trash", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:

                SharePreferenceUtils.getInstance().deletePref();
                Intent signIntent = new Intent(HomeActivity.this, SigninActivity.class);
                startActivity(signIntent);
                finish();
                break;
        }
        return false;
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    // Open the Drawer
    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            closeDrawer();
        else
            super.onBackPressed();
    }

}
