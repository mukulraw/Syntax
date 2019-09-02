package com.syntax.note;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.syntax.note.allNoteResponsePOJO.Datum;
import com.syntax.note.allNoteResponsePOJO.NoteList;
import com.syntax.note.allNoteResponsePOJO.allNoteResponseBean;
import com.syntax.note.home.HomeActivity;
import com.syntax.note.login.SigninActivity;
import com.syntax.note.multiDeletePOJO.multiDeleteBean;
import com.syntax.note.note.AddNoteActivity;
import com.syntax.note.note.TrashActivity;
import com.syntax.note.signinResponsePOJO.signinResponseBean;
import com.syntax.note.trashRequestPOJO.Data;
import com.syntax.note.trashRequestPOJO.TrashRequestBean;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Home extends Fragment {

    SwipeRefreshLayout swipe;
    RecyclerView grid;
    GridLayoutManager manager;
    ServiceInterface serviceInterface;
    Retrofit retrofit;
    List<Datum> list;
    HomeAdapter adapter;

    String layout;

    Button deleteButton, calcel;

    GoogleSignInClient mGoogleSignInClient;

    List<String> delete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout , container , false);

        layout = SharePreferenceUtils.getInstance().getString("layout");

        list = new ArrayList<>();
        delete = new ArrayList<>();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        grid = view.findViewById(R.id.grid);
        swipe = view.findViewById(R.id.swipe);
        deleteButton = view.findViewById(R.id.delete);
        calcel = view.findViewById(R.id.cancel);

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

        calcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadData();

            }
        });


        swipe.setColorSchemeResources(R.color.colorAccent);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                swipe.setRefreshing(true);

                String deletess = TextUtils.join(",", delete);

                Log.d("delete", deletess);

                multiDeleteBean body = new multiDeleteBean();


                com.syntax.note.multiDeletePOJO.Data data = new com.syntax.note.multiDeletePOJO.Data();

                data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
                data.setNoteId(deletess);

                body.setAction("delete_multiple_note");
                body.setData(data);

                Call<signinResponseBean> call = serviceInterface.multiDelete(body);

                call.enqueue(new Callback<signinResponseBean>() {
                    @Override
                    public void onResponse(Call<signinResponseBean> call, Response<signinResponseBean> response) {

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        swipe.setRefreshing(false);

                        loadData();
                    }

                    @Override
                    public void onFailure(Call<signinResponseBean> call, Throwable t) {
                        swipe.setRefreshing(false);
                    }
                });


            }
        });

        loadData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData() {

        deleteButton.setVisibility(View.GONE);
        calcel.setVisibility(View.GONE);

        swipe.setRefreshing(true);

        delete.clear();

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

                adapter = new HomeAdapter(getContext(), response.body().getData());
                manager = new GridLayoutManager(getContext(), 1);
                grid.setAdapter(adapter);
                grid.setLayoutManager(manager);
                //adapter.setGridData(response.body().getData());

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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.home_list_item1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Datum item = list.get(position);

            holder.title.setText(item.getCatName());

            List<NoteList> ll = new ArrayList<>();

            for (int i = 0; i < item.getNoteList().size(); i++) {

                NoteList n = new NoteList();
                n.setCatId(item.getNoteList().get(i).getCatId());
                n.setCatName(item.getNoteList().get(i).getCatName());
                n.setCreateDate(item.getNoteList().get(i).getCreateDate());
                n.setDesc(item.getNoteList().get(i).getDesc());
                n.setId(item.getNoteList().get(i).getId());
                n.setTitle(item.getNoteList().get(i).getTitle());
                n.setCheck(false);

                ll.add(n);

            }

            holder.adapter2.setGridData(ll);

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


            ViewHolder(View itemView) {
                super(itemView);
                list = new ArrayList<>();
                title = itemView.findViewById(R.id.textView4);
                grid = itemView.findViewById(R.id.grid);

                if (layout.equals("list")) {
                    manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                } else {
                    manager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                }

                adapter2 = new HomeAdapter2(context, list);
                grid.setAdapter(adapter2);
                grid.setLayoutManager(manager);

            }
        }

    }


    class HomeAdapter2 extends RecyclerView.Adapter<HomeAdapter2.ViewHolder> {
        Context context;
        List<NoteList> list;

        HomeAdapter2(Context context, List<NoteList> list) {
            this.context = context;
            this.list = list;
        }

        void setGridData(List<NoteList> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            View view;

            if (layout.equals("list")) {
                view = inflater.inflate(R.layout.home_list_item3, parent, false);
            } else {
                view = inflater.inflate(R.layout.home_list_item2, parent, false);
            }

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final NoteList item = list.get(position);
            holder.title.setText(item.getTitle());
            holder.note.setText(item.getDesc());


            holder.check.setChecked(item.getCheck());


            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {
                        delete.add(item.getId());
                        item.setCheck(true);
                        if (delete.size() > 0) {
                            deleteButton.setVisibility(View.VISIBLE);
                            calcel.setVisibility(View.VISIBLE);
                        } else {
                            deleteButton.setVisibility(View.GONE);
                            calcel.setVisibility(View.GONE);
                        }
                    } else {
                        delete.remove(item.getId());
                        item.setCheck(false);

                        if (delete.size() > 0) {
                            deleteButton.setVisibility(View.VISIBLE);
                            calcel.setVisibility(View.VISIBLE);
                        } else {
                            deleteButton.setVisibility(View.GONE);
                            calcel.setVisibility(View.GONE);
                        }

                    }

                }
            });


            holder.date.setText(item.getCreateDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SingleNote.class);
                    intent.putExtra("note", item.getDesc());
                    intent.putExtra("id", item.getId());
                    intent.putExtra("catid", item.getCatId());
                    intent.putExtra("title", item.getTitle());
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView title, note, date;
            CheckBox check;

            ViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.textView5);
                note = itemView.findViewById(R.id.textView6);
                date = itemView.findViewById(R.id.textView9);
                check = itemView.findViewById(R.id.checkBox);

            }
        }

    }






}
