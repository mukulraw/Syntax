package com.syntax.note;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Traashlist extends Fragment{
    ListView trashlist;
    DatabaseHelper db;
    SQLiteDatabase sql;
    Cursor cursor;
    ArrayList<database_model> a;
    ArrayList id,name,time,status,title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_traashlist,container,false);
        trashlist = (ListView) v.findViewById(R.id.trashlist);
        db = new DatabaseHelper(getActivity());
        sql = db.getReadableDatabase();
        a = new ArrayList();
        id = new ArrayList();
        name = new ArrayList();
        time = new ArrayList();
        status = new ArrayList();
        title = new ArrayList();
        cursor = sql.rawQuery("SELECT * FROM mynote Where status='"+"B"+"'", null);
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
        databaseadapter ad = new databaseadapter(getActivity(),a);
        trashlist.setAdapter(ad);
        trashlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ii = new Intent(getActivity(), Addnote.class);
                ii.putExtra("type","T");
                SharedPreferences settings = getActivity().getSharedPreferences("mypref", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("type","A");
                editor.putString("id",a.get(i).getId());
                editor.putString("name",a.get(i).getName());
                editor.putString("time",a.get(i).getTime());
                editor.putString("status",a.get(i).getStatus());
                editor.putString("title",a.get(i).getTitle());
                editor.commit();
                startActivity(ii);

            }
        });
        return v;
    }
}
