package com.syntax.note;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class databaseadapter extends BaseAdapter implements Filterable {

    Context context;
    DatabaseHelper db;
    String status="A",type="A";
    SQLiteDatabase sql;

    Cursor cursor;

    ArrayList<database_model> Listname;
    ArrayList<database_model> orig;
    private  SparseBooleanArray mSelectedItemsIds=new SparseBooleanArray();
    public databaseadapter(Context context2,ArrayList<database_model> Listname ) {
        this.context = context2;
        this.Listname = Listname;
        this.orig= new ArrayList<database_model>();
        this.orig.addAll(Listname);
    }


    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<database_model> results = new ArrayList<database_model>();
                if (orig == null)
                    orig = Listname;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final database_model g : orig) {
                            if (g.getTitle().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                Listname = (ArrayList<database_model>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public  void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public database_model getItem(int position) {
        return Listname.get(position);
    }

    @Override
    public int getCount() {
        return Listname.size();
    }
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        LayoutInflater layoutInflater;
        View v;
        System.out.println("your getview method");
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.listitem, null);
        holder = new Holder();
        holder.cname = (TextView) v.findViewById(R.id.item);
        v.setTag(holder);
        holder = (Holder) v.getTag();
        holder.cname.setText(Listname.get(position).getTitle());
        return v;
    }
    public class Holder {
           TextView cname;
       }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Listname.clear();
        if (charText.length() == 0) {
            Listname.addAll(orig);
        }
        else
        {
            for (database_model wp : orig)
            {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    Listname.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void remove(database_model object) {
        db = new DatabaseHelper(context);
        sql = db.getWritableDatabase();
        SharedPreferences settings = context.getSharedPreferences("mypref", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("id",object.getId());
        editor.putString("name",object.getName());
        editor.putString("time",object.getTime());
        editor.putString("status",object.getStatus());
        editor.putString("title",object.getTitle());
        SQLiteDatabase db1 = db.getWritableDatabase();
            String strSQL;
            if(status.equals("A")){
                strSQL = "UPDATE mynote SET status ='B' WHERE id = "+ object.getId();
            }else{ strSQL = "UPDATE mynote SET status ='A' WHERE id = "+ object.getId();}
            db1.execSQL(strSQL);
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
             ((MainActivity)context).finish();
            SharedPreferences settings1 = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
            settings1.edit().clear().commit();
            Intent intent=new Intent(context,MainActivity.class);
            context.startActivity(intent);







        db.close();
        notifyDataSetChanged();

    }


    public List<database_model> getWorldPopulation() {
        return Listname;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }



}
