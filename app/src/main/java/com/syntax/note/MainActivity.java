package com.syntax.note;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    FragmentManager fragmentManager;
    FragmentTransaction ftalllist,fttrash;
    Addnotelist alllist = new Addnotelist();
    TextView addnote,trash,txttool,txthome;
    String mypage="Note";
    ImageView deleteall;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(MainActivity.this,Addnote.class);
                editor.putString("type","");
                editor.putString("id","");
                editor.putString("name","");
                editor.putString("time","");
                editor.putString("status","");
                i.putExtra("type","N");
                startActivity(i);
                finish();

            }
        });
        db = new DatabaseHelper(this);
        txttool = (TextView) findViewById(R.id.txttoolbar);
        addnote = (TextView) findViewById(R.id.addnote) ;
        trash = (TextView) findViewById(R.id.trash) ;
        txthome = (TextView) findViewById(R.id.txthome) ;
        deleteall = (ImageView) findViewById(R.id.thrash_delete) ;
        txttool.setText("Notes");

        fragmentManager = getFragmentManager();
        ftalllist = fragmentManager.beginTransaction();
        ftalllist.add(R.id.ll,alllist);
        ftalllist.commit();

        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteid();
                deleteall.setVisibility(View.VISIBLE);
                txttool.setText("Trash");
                Traashlist t = new Traashlist();
                ftalllist = fragmentManager.beginTransaction();
                ftalllist.replace(R.id.ll,t);
                ftalllist.commit();
                drawer.closeDrawer(Gravity.LEFT);

            }
        });
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                Intent i = new Intent(MainActivity.this,Addnote.class);
                editor.putString("type","");
                editor.putString("id","");
                editor.putString("name","");
                editor.putString("time","");
                editor.putString("status","");
                i.putExtra("type","N");
                startActivity(i);
                finish();
            }
        });


        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteall.setVisibility(View.VISIBLE);
                txttool.setText("Trash");
                Traashlist t = new Traashlist();
                ftalllist = fragmentManager.beginTransaction();
                ftalllist.replace(R.id.ll,t);
                ftalllist.commit();
                drawer.closeDrawer(Gravity.LEFT);
            }
        });


        txthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteall.setVisibility(View.GONE);
                txttool.setText("Notes");
                ftalllist = fragmentManager.beginTransaction();
                ftalllist.replace(R.id.ll,alllist);
                ftalllist.commit();
                drawer.closeDrawer(Gravity.LEFT);

            }
        });





        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
