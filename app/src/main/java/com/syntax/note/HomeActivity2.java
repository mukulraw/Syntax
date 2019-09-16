package com.syntax.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity2 extends AppCompatActivity {

    AHBottomNavigation bottom;
    TextView title;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        bottom = findViewById(R.id.view2);
        title = findViewById(R.id.imageView5);
        fab = findViewById(R.id.floatingActionButton);


        final AHBottomNavigationItem item1 = new AHBottomNavigationItem("HOME", R.drawable.m_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("SEARCH", R.drawable.ic_search_black);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.m_note);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("TRASH", R.drawable.m_garbage);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("PROFILE", R.drawable.ic_man_user);

        bottom.addItem(item1);
        bottom.addItem(item2);
        bottom.addItem(item3);
        bottom.addItem(item4);
        bottom.addItem(item5);
        bottom.setBehaviorTranslationEnabled(false);
        bottom.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottom.setAccentColor(Color.parseColor("#6495ED"));

        bottom.setElevation(2);
        bottom.setDefaultBackgroundColor(Color.parseColor("#f2f2f2"));

        bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (position == 0)
                {

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Home test = new Home();
                    ft.replace(R.id.replace, test);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    //ft.addToBackStack(null);
                    ft.commit();

                    title.setText("HOME");

                    return true;
                }
                else if (position == 1)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Sear test = new Sear();
                    ft.replace(R.id.replace, test);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    //ft.addToBackStack(null);
                    ft.commit();
                    title.setText("SEARCH");
                    return true;
                }
                else if (position == 2)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ADdNote test = new ADdNote();
                    ft.replace(R.id.replace, test);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    //ft.addToBackStack(null);
                    ft.commit();
                    title.setText("ADD NOTE");
                    return true;
                }
                else if (position == 3)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Trash test = new Trash();
                    ft.replace(R.id.replace, test);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    //ft.addToBackStack(null);
                    ft.commit();
                    title.setText("TRASH");
                    return true;
                }
                else
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Prof test = new Prof();
                    ft.replace(R.id.replace, test);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    //ft.addToBackStack(null);
                    ft.commit();
                    title.setText("PROFILE");
                    return true;
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ADdNote test = new ADdNote();
                ft.replace(R.id.replace, test);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                //ft.addToBackStack(null);
                ft.commit();
                title.setText("ADD NOTE");
                bottom.setCurrentItem(2);
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Home test = new Home();
        ft.replace(R.id.replace, test);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        //ft.addToBackStack(null);
        ft.commit();
        title.setText("HOME");
        bottom.setCurrentItem(0);

    }
}
