package com.syntax.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syntax.note.searchRequestPOJO.Data;
import com.syntax.note.searchRequestPOJO.searchRequestBean;
import com.syntax.note.searchResultPOJO.Datum;
import com.syntax.note.searchResultPOJO.searchResultBean;
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

public class Sear extends Fragment {


    EditText search;
    RecyclerView grid;
    ProgressBar progress;
    GridLayoutManager manager;
    List<Datum> list;
    TrashAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search , container , false);

        list = new ArrayList<>();

        search = view.findViewById(R.id.editText);
        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar4);
        manager = new GridLayoutManager(getContext(), 2);

        adapter = new TrashAdapter(getContext(), list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);



        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    searchQuery(textView.getText().toString());

                    return true;
                }

                return false;
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String query = charSequence.toString();


                searchQuery(query);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }


    public void searchQuery(String query) {

        progress.setVisibility(View.VISIBLE);

        searchRequestBean body = new searchRequestBean();
        body.setAction("search_note");

        final Data data = new Data();
        data.setKey(query);
        data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
        body.setData(data);

        ServiceInterface serviceInterface;
        Retrofit retrofit;

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);

        Call<searchResultBean> call = serviceInterface.search(body);

        call.enqueue(new Callback<searchResultBean>() {
            @Override
            public void onResponse(Call<searchResultBean> call, Response<searchResultBean> response) {

                if (response.body().getStatus().equals("1")) {
                    adapter.setGridData(response.body().getData());
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<searchResultBean> call, Throwable t) {
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
            View view = inflater.inflate(R.layout.trash_list_model1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Datum item = list.get(position);

            holder.title.setText(item.getTitle());
            holder.note.setText(item.getDescription());

            holder.date.setText(item.getCreateDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SingleNote.class);
                    intent.putExtra("note", item.getDescription());
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
            TextView title, note , date;

            ViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.textView7);
                note = itemView.findViewById(R.id.textView8);
                date = itemView.findViewById(R.id.textView9);

            }
        }
    }

}
