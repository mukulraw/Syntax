package com.syntax.note;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.syntax.note.addNoteRequestPOJO.addNoteRequestBean;
import com.syntax.note.addNoteResponsePOJO.addNoteResponseBean;
import com.syntax.note.categoryRequestPOJO.CategoryRequestBean;
import com.syntax.note.categoryRequestPOJO.Data;
import com.syntax.note.categoryResponsePOJO.CategoryResponseBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ADdNote extends Fragment {

    private Spinner spinCategory;
    private ArrayList<String> catId = new ArrayList<>();
    private ArrayList<String> catName = new ArrayList<>();
    private ServiceInterface serviceInterface;
    private EditText title, desc;
    private ImageButton submit;
    private String mTitle, mDesc, mCatId;
    private String mUserId;
    private ConstraintLayout rootlayout;
    //  private TextInputLayout inputLayoutTitle,inputLayoutDesc;
    private boolean isValid = false;
    private ProgressBar pBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_note2 , container , false);

        catId = new ArrayList<>();
        catName = new ArrayList<>();

        setupWidget(view);
        pBar.setVisibility(View.GONE);
        getData();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);

        spinCategory = view.findViewById(R.id.spinCategory);

        getCategory();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                dataValidation();
                if (isValid) {
                    addNoteReq();
                    pBar.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Fill All Details First..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //if you want to set any action you can do in this listener
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {

                mCatId = catId.get(position);
                // Toast.makeText(AddNoteActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        return view;
    }

    private void addNoteReq() {
        addNoteRequestBean body = new addNoteRequestBean();
        body.setAction("create_note");
        com.syntax.note.addNoteRequestPOJO.Data data = new com.syntax.note.addNoteRequestPOJO.Data();
        data.setCatId(mCatId);
        data.setTitle(mTitle);
        data.setDesc(mDesc);
        data.setUserId(mUserId);

        body.setData(data);
        Gson gson = new Gson();

        Log.i("abc", gson.toJson(body));




        Call<addNoteResponseBean> call = serviceInterface.addNote(body);
        call.enqueue(new Callback<addNoteResponseBean>() {
            @Override
            public void onResponse(Call<addNoteResponseBean> call, Response<addNoteResponseBean> response) {
                assert response.body() != null;
                if (response.body().getStatus().equals("1")) {
                    pBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(rootlayout, "" + response.body().getMessage(), Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.seaGreen));
                    snackbar.show();
                    title.setText("");
                    desc.setText("");

                } else {
                    pBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(rootlayout, "" + response.body().getMessage(), Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.seaGreen));
                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<addNoteResponseBean> call, Throwable t) {
                pBar.setVisibility(View.GONE);

            }
        });
    }


    private void getCategory() {

        CategoryRequestBean body = new CategoryRequestBean();
        body.setAction("category_list");
        Data data = new Data();

        mUserId = SharePreferenceUtils.getInstance().getString(Constant.USER_id);

        data.setUserId(SharePreferenceUtils.getInstance().getString(Constant.USER_id));
        //  Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
        body.setData(data);

        Call<CategoryResponseBean> call = serviceInterface.getCategory(body);
        call.enqueue(new Callback<CategoryResponseBean>() {
            @Override
            public void onResponse(Call<CategoryResponseBean> call, Response<CategoryResponseBean> response) {
                assert response.body() != null;
                if (response.body().getStatus().equals("1")) {

                    catId.clear();
                    catName.clear();

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        catId.add(response.body().getData().get(i).getId());
                        catName.add(response.body().getData().get(i).getCatName());
                    }

                    try {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                                android.R.layout.simple_spinner_item, catName);//setting the country_array to spinner
                        // string value
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinCategory.setAdapter(adapter);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                }

            }

            @Override
            public void onFailure(Call<CategoryResponseBean> call, Throwable t) {
                //Toast.makeText(getContext(), "api response fail" + t, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void setupWidget(View view) {
        title = view.findViewById(R.id.title_text);
        desc = view.findViewById(R.id.desc);
        // inputLayoutTitle = findViewById(R.id.inputLayoutTitle);
        // inputLayoutDesc = findViewById(R.id.inputLayoutDesc);
        submit = view.findViewById(R.id.submit);
        rootlayout = view.findViewById(R.id.rootlayout);
        pBar = view.findViewById(R.id.progressBar);


    }

    private void getData() {
        mTitle = title.getText().toString().trim();
        mDesc = desc.getText().toString().trim();
    }

    private void dataValidation() {

        // title.setError("Title Missing");
        //inputLayoutTitle.setErrorEnabled(false);
        isValid = !title.getText().toString().isEmpty();

        if (desc.getText().toString().isEmpty()) {
            // inputLayoutDesc.setError("Description Missing");
            isValid = false;
        }  // inputLayoutDesc.setErrorEnabled(false);


    }


}
