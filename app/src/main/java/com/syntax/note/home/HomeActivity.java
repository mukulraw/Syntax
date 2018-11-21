package com.syntax.note.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.syntax.note.MyApplication;
import com.syntax.note.R;
import com.syntax.note.Search;
import com.syntax.note.SingleNote;
import com.syntax.note.allNoteResponsePOJO.Datum;
import com.syntax.note.allNoteResponsePOJO.NoteList;
import com.syntax.note.allNoteResponsePOJO.allNoteResponseBean;
import com.syntax.note.login.SigninActivity;
import com.syntax.note.model.Note;
import com.syntax.note.note.AddNoteActivity;
import com.syntax.note.note.TrashActivity;
import com.syntax.note.trashRequestPOJO.Data;
import com.syntax.note.trashRequestPOJO.TrashRequestBean;
import com.syntax.note.trashResponsePOJO.TrashResponseBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Drawer Layout
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton fab;
    AlertDialog.Builder builder1;
    SwipeRefreshLayout swipe;
    RecyclerView grid;
    GridLayoutManager manager;
    ServiceInterface serviceInterface;
    Retrofit retrofit;
    List<Datum> list;
    HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        list = new ArrayList<>();

        // Toolbar
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(" Note");

        setSupportActionBar(mToolbar);

        //fab button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNoteIntent = new Intent(HomeActivity.this, AddNoteActivity.class);
                startActivity(addNoteIntent);
            }
        });


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


        grid = findViewById(R.id.grid);
        swipe = findViewById(R.id.swipe);

        adapter = new HomeAdapter(this, list);
        manager = new GridLayoutManager(this, 1);
        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        serviceInterface = retrofit.create(ServiceInterface.class);


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadData();

            }
        });


        swipe.setColorSchemeResources(R.color.colorAccent);


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
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
                Intent addNoteIntent = new Intent(HomeActivity.this, AddNoteActivity.class);
                startActivity(addNoteIntent);
                break;
            case R.id.menu_trash:
               /* SharePreferenceUtils.getInstance().deletePref();
                Intent signIntent = new Intent(HomeActivity.this, SigninActivity.class);
                startActivity(signIntent);
                finish();*/

                Intent trashIntent = new Intent(HomeActivity.this, TrashActivity.class);
                startActivity(trashIntent);
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


    public void loadData() {

        swipe.setRefreshing(true);

        TrashRequestBean body = new TrashRequestBean();
        Data data = new Data();
        body.setAction("all_notes");

        String userId = SharePreferenceUtils.getInstance().getString(Constant.USER_id);

        data.setUserId(userId);

        body.setData(data);

        Gson gson = new Gson();

        Log.i("abc", gson.toJson(body));


        Call<allNoteResponseBean> call = serviceInterface.allNotes(body);

        call.enqueue(new Callback<allNoteResponseBean>() {
            @Override
            public void onResponse(Call<allNoteResponseBean> call, Response<allNoteResponseBean> response) {


                adapter.setGridData(response.body().getData());

                swipe.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<allNoteResponseBean> call, Throwable t) {
                swipe.setRefreshing(false);
            }
        });

    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

        Context context;
        List<Datum> list;

        HomeAdapter(Context context, List<Datum> list) {
            this.context = context;
            this.list = list;
        }

        void setGridData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.home_list_item1 , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Datum item = list.get(position);

            holder.title.setText(item.getCatName());

            holder.adapter2.setGridData(item.getNoteList());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;
            RecyclerView grid;
            LinearLayoutManager manager;
            HomeAdapter2 adapter2;
            List<NoteList> list;


            public ViewHolder(View itemView) {
                super(itemView);
                list = new ArrayList<>();
                title = itemView.findViewById(R.id.textView4);
                grid = itemView.findViewById(R.id.grid);

                manager = new LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL , false);
                adapter2 = new HomeAdapter2(context , list);
                grid.setAdapter(adapter2);
                grid.setLayoutManager(manager);

            }
        }

    }


    class HomeAdapter2 extends RecyclerView.Adapter<HomeAdapter2.ViewHolder>
    {
        Context context;
        List<NoteList> list;

        HomeAdapter2(Context context , List<NoteList> list)
        {
            this.context = context;
            this.list = list;
        }

        void setGridData(List<NoteList> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.home_list_item2 , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final NoteList item = list.get(position);
            holder.title.setText(item.getTitle());
            holder.note.setText(item.getDesc());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context , SingleNote.class);
                    intent.putExtra("note" , item.getDesc());
                    intent.putExtra("id" , item.getId());
                    intent.putExtra("title" , item.getTitle());
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            TextView title , note;

            public ViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.textView5);
                note = itemView.findViewById(R.id.textView6);

            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search)
        {

            Intent intent = new Intent(HomeActivity.this , Search.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);

    }
}
