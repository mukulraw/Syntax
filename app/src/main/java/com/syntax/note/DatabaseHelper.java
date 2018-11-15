package com.syntax.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by USER on 16-05-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "Mytable", null, 1);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mynote(id Integer PRIMARY KEY AUTOINCREMENT,note text,timestamp text,status text,title text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS mynote");
    }

    public void deleteid()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String insert="delete from  mynote where status='"+"B"+"'";
        db.execSQL(insert);
        db.close();
    }

    public void InsertData(String note,String timestamp, String status, String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String insert="insert into mynote(note,timestamp,status,title) values('"+note+"','"+timestamp+"','"+status+"','"+title+"')";
        db.execSQL(insert);
        db.close();
    }
    //db.execSQL("UPDATE DB_TABLE SET YOUR_COLUMN='newValue' WHERE id=6 ");
    public void updateNote(Integer id,String note,String time,String status,String title) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String update="UPDATE mynote(note,timestamp,status,title) VALUES('"+note+"','"+time+"','"+status+"','"+title+"') WHERE id = "+id;
//        db.execSQL(update);
//        db.close();


//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("note",note);
//        values.put("time", time);
//        values.put("status", status);
//        values.put("title", title);
//        database.update("mynote", values, "id= "+id);
//        database.close();

//        ContentValues cv = new ContentValues();
//        cv.put("note",""+note); //These Fields should be your String values of actual column names
//        cv.put("timestamp",""+time);
//        cv.put("status",""+status);
//
//        db.update("mynote", cv, "id="+id, null);


    }
}
