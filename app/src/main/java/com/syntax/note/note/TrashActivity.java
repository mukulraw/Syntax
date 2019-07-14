package com.syntax.note.note;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.syntax.note.R;
import com.syntax.note.TrashDetails;
import com.syntax.note.home.HomeActivity;
import com.syntax.note.multiDeletePOJO.multiDeleteBean;
import com.syntax.note.signinResponsePOJO.signinResponseBean;
import com.syntax.note.trashRequestPOJO.Data;
import com.syntax.note.trashRequestPOJO.TrashRequestBean;
import com.syntax.note.trashResponsePOJO.Datum;
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

public class TrashActivity extends AppCompatActivity {
    ServiceInterface serviceInterface;
    Retrofit retrofit;
    String userId;
    ArrayList trashList;
    ProgressBar progress;
    RecyclerView grid;
    GridLayoutManager manager;
    List<Datum> list;
    TrashAdapter adapter;

    Button deletebutton , restorebutton , cancel;

    List<String> delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        list = new ArrayList<>();
        delete = new ArrayList<>();

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setPadding(10, 0, 0, 0);
        mToolbar.setTitle(" Trash");
        // mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.m_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // b.mylist.remove(b.mylist.size() - 1);

                finish();

            }
        });

        progress = findViewById(R.id.progressBar3);
        deletebutton = findViewById(R.id.button2);
        cancel = findViewById(R.id.button3);
        restorebutton = findViewById(R.id.button);
        grid = findViewById(R.id.grid);
        manager = new GridLayoutManager(this, 2);
        adapter = new TrashAdapter(this, list);

        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);


        userId = SharePreferenceUtils.getInstance().getString(Constant.USER_id);
        // Toast.makeText(this, "" + userId, Toast.LENGTH_SHORT).show();
        Log.i("userid", userId);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);
        trashList = new ArrayList();

        trashReq();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trashReq();

            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                String deletess = TextUtils.join(",", delete);

                Log.d("delete" , deletess);

                multiDeleteBean body = new multiDeleteBean();


                com.syntax.note.multiDeletePOJO.Data data = new com.syntax.note.multiDeletePOJO.Data();

                data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
                data.setNoteId(deletess);

                body.setAction("delete_trash_multiple_note");
                body.setData(data);

                Call<signinResponseBean> call = serviceInterface.multiDelete(body);

                call.enqueue(new Callback<signinResponseBean>() {
                    @Override
                    public void onResponse(Call<signinResponseBean> call, Response<signinResponseBean> response) {

                        Toast.makeText(TrashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        progress.setVisibility(View.GONE);
                        trashReq();
                    }

                    @Override
                    public void onFailure(Call<signinResponseBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });

            }
        });

        restorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);

                String deletess = TextUtils.join(",", delete);

                Log.d("delete" , deletess);

                multiDeleteBean body = new multiDeleteBean();


                com.syntax.note.multiDeletePOJO.Data data = new com.syntax.note.multiDeletePOJO.Data();

                data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
                data.setNoteId(deletess);

                body.setAction("recover_multiple_note");
                body.setData(data);

                Call<signinResponseBean> call = serviceInterface.multiDelete(body);

                call.enqueue(new Callback<signinResponseBean>() {
                    @Override
                    public void onResponse(Call<signinResponseBean> call, Response<signinResponseBean> response) {

                        Toast.makeText(TrashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        progress.setVisibility(View.GONE);
                        trashReq();
                    }

                    @Override
                    public void onFailure(Call<signinResponseBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });

            }
        });

    }

    private void trashReq() {

        delete.clear();

        deletebutton.setVisibility(View.GONE);
        restorebutton.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

        progress.setVisibility(View.VISIBLE);

        TrashRequestBean body = new TrashRequestBean();
        Data data = new Data();
        body.setAction("trash_list");

        data.setUserId(userId);

        body.setData(data);

        Gson gson = new Gson();

        Log.i("abc", gson.toJson(body));


        Call<TrashResponseBean> call = serviceInterface.trash(body);
        call.enqueue(new Callback<TrashResponseBean>() {
            @Override
            public void onResponse(Call<TrashResponseBean> call, Response<TrashResponseBean> response) {
                if (response.body().getStatus().equals("1")) {
                    // Toast.makeText(TrashActivity.this, "sucess", Toast.LENGTH_SHORT).show();


                    if (response.body().getStatus().equals("1")) {

                        adapter.setGridData(response.body().getData());

                    }

                } else {
                    Log.i("error", response.body().getMessage());

                }

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TrashResponseBean> call, Throwable t) {
                //Toast.makeText(TrashActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        });


    }


    class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.ViewHolder> {

        Context context;
        List<Datum> list = new ArrayList<>();

        TrashAdapter(Context context, List<Datum> list) {
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.trash_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Datum item = list.get(position);

            holder.title.setText(item.getTitle());
            holder.note.setText(item.getDesc());
            holder.date.setText(item.getCreateDate());

            holder.check.setChecked(item.getCheck());

            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {
                        delete.add(item.getId());
                        item.setCheck(true);
                        if (delete.size() > 0) {
                            deletebutton.setVisibility(View.VISIBLE);
                            restorebutton.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                        } else {
                            deletebutton.setVisibility(View.GONE);
                            restorebutton.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                        }
                    } else {
                        delete.remove(item.getId());
                        item.setCheck(false);

                        if (delete.size() > 0) {
                            deletebutton.setVisibility(View.VISIBLE);
                            restorebutton.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                        } else {
                            deletebutton.setVisibility(View.GONE);
                            restorebutton.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                        }

                    }

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context , TrashDetails.class);
                    intent.putExtra("note" , item.getDesc());
                    intent.putExtra("id" , item.getId());
                    intent.putExtra("title" , item.getTitle());
                    intent.putExtra("cat" , item.getCatName());
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, note , date;
            CheckBox check;

            ViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.textView7);
                note = itemView.findViewById(R.id.textView8);
                date = itemView.findViewById(R.id.textView9);
                check = itemView.findViewById(R.id.checkBox);

            }
        }
    }


}
