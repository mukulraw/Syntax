package com.syntax.note;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Addnotelist extends Fragment implements SearchView.OnQueryTextListener{
    ListView alllist;
    DatabaseHelper db;
    SQLiteDatabase sql;
    Cursor cursor;
    ArrayList<database_model> a;
    ArrayList id,name,time,status,title;
    databaseadapter ad;
    databaseadapter ad1;
   // EditText search;
    SearchView mSearchView;
    EditText editsearch;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_addnotelist,container,false);
        db = new DatabaseHelper(getActivity());
        alllist = (ListView) v.findViewById(R.id.alllist);
       // search = (EditText) v.findViewById(R.id.search);
        mSearchView=(SearchView) v.findViewById(R.id.searchView1);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sql = db.getReadableDatabase();
        a = new ArrayList();
        id = new ArrayList();
        name = new ArrayList();
        time = new ArrayList();
        status = new ArrayList();
        title = new ArrayList();
        a.clear();
        cursor = sql.rawQuery("SELECT * FROM mynote Where status='"+"A"+"'", null);
        if (cursor.moveToFirst()) {
            do {
                String ids=cursor.getString(0);
                String names=cursor.getString(1);
                String times=cursor.getString(2);
                String statuss=cursor.getString(3);
                String titles=cursor.getString(4);
                a.add(new database_model(ids,names,times,statuss,titles));
            } while (cursor.moveToNext());
        }db.close();
        ad = new databaseadapter(getActivity(),a);
        alllist.setAdapter(ad);
        alllist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ii = new Intent(getActivity(), Addnote.class);
                SharedPreferences settings = getActivity().getSharedPreferences("mypref", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("id",a.get(i).getId());
                editor.putString("name",a.get(i).getName());
                editor.putString("time",a.get(i).getTime());
                editor.putString("status",a.get(i).getStatus());
                editor.putString("title",a.get(i).getTitle());
                ii.putExtra("type","A");
                editor.commit();
                startActivity(ii);
            }
        });
        alllist.setTextFilterEnabled(true);
        alllist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        alllist.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                final int checkedCount = alllist.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                ad.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.mainmenu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = ad.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {

                                database_model selecteditem = ad.getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                ad.remove(selecteditem);
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ad.removeSelection();
            }
        });

       // setupSearchView();


        // Locate the EditText in listview_main.xml
        editsearch = (EditText) v.findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                ad.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                int textlength = s.length();
//                number2=new ArrayList<String>();
//                name2=new ArrayList<String>();
//
//                String ss = search.getText().toString();
//                for (int i = 0; i < name.size(); i++)
//                {
//                    String mp=name.get(i).toString();
//                    for(int j=0;j<textlength;j++)
//                    {
//                        if(ss.equalsIgnoreCase(mp.substring(j,textlength)))
//                        {
//                            name2.add(name.get(i).toString());
//                            number2.add(number.get(i).toString());
//                        }
//
//                    }
//                }
//                if(name2.size()==0)
//                {
//                    adapter=new HelplineAdapter(getActivity(),number2,name2);
//                    recyclerView.setAdapter(adapter);
//                    if (ed_search.getText().toString().equals("")) {
//
//                        adapter=new HelplineAdapter(getActivity(),number,name);
//                        recyclerView.setAdapter(adapter);
//                    }
//                    else {
//                        Toast toast = Toast.makeText(getActivity(), "No Items Matched", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
//                }
//                else {
//                    adapter=new HelplineAdapter(getActivity(),number2,name2);
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        return v;
    }

    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");

    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        if (TextUtils.isEmpty(newText)) {
            alllist.clearTextFilter();
        } else {
            alllist.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    public void finish() {
        finish();
    }
}
