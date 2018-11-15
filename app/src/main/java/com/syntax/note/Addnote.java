package com.syntax.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Addnote extends AppCompatActivity {
    DatabaseHelper db;
    EditText editTextnote,editTexttitle;
    ImageView back,delete,share,restore;
    String time,id,name,status,type,titles ;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        getSupportActionBar().hide();
        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
        share=(ImageView)findViewById(R.id.share);
        restore=(ImageView)findViewById(R.id.restore);
        back = (ImageView) findViewById(R.id.back);
        delete = (ImageView) findViewById(R.id.delete);
        done = (Button) findViewById(R.id.done);

        time=settings.getString("time","");
        id=settings.getString("id","");
        name=settings.getString("name","");
        status=settings.getString("status","");
        titles=settings.getString("title","");
        type= getIntent().getStringExtra("type");
       // Toast.makeText(this, "qqqqqqqqqq"+type, Toast.LENGTH_SHORT).show();
        db = new DatabaseHelper(this);
        editTextnote = (EditText) findViewById(R.id.edtnewnote);
        editTexttitle = (EditText) findViewById(R.id.edttitle);
      //  if(name.equals("")||name.equals(null)){}else{editText.setText(name);}
        if (type.equals("T"))
        {
//            delete.setVisibility(View.GONE);
            restore.setVisibility(View.VISIBLE);
            editTexttitle.setEnabled(false);
            editTexttitle.setText(titles);
            editTextnote.setEnabled(false);
            editTextnote.setText(name);
            done.setVisibility(View.GONE);

         }
        if (type.equals("A"))
            {
//                restore.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                editTexttitle.setText(titles);
                editTextnote.setText(name);

            }
        if (type.equals("N"))
        {
//                restore.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
            editTexttitle.setText("");
            editTextnote.setText("");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                String formattedDate = df.format(c);
//                if (editTexttitle.getText().toString().equals(""))
//                {
//                    Intent i = new Intent(Addnote.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                    SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
//                    settings.edit().clear().commit();
//
//                }
//                else {
//                    //String note,String timestamp, String status, String title)
//                    if (type.equals("N")) {
//
//                        db.InsertData(editTextnote.getText().toString(), formattedDate, "A",editTexttitle.getText().toString());
//                        Intent i = new Intent(Addnote.this, MainActivity.class);
//                        startActivity(i);
//                        finish();
//                        SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
//                        settings.edit().clear().commit();
//
//                    } else {
//                        SQLiteDatabase db1 = db.getWritableDatabase();
//                        String strSQL;
//
//
//                        if (status.equals("A")) {
//
//                            String strSQL1;
//                            String strSQL2;
//                            String strSQL3;
//
//                            Integer idi = Integer.parseInt(id);
//                            String a = ""+editTextnote.getText().toString();
//                            String b = ""+editTexttitle.getText().toString();
//                            String t = ""+formattedDate;
//
//                            strSQL = "UPDATE mynote SET status = 'A' WHERE id = " + id;
//                            strSQL1 = "UPDATE mynote SET note = '"+a+"' WHERE id = " + id;
//                            strSQL2 = "UPDATE mynote SET timestamp = '"+t+"' WHERE id = " + id;
//                            strSQL3 = "UPDATE mynote SET title = '"+b+"' WHERE id = " + id;
//                            db1.execSQL(strSQL1);
//                            db1.execSQL(strSQL2);
//                            db1.execSQL(strSQL3);
//
//
//                          //  strSQL = "UPDATE mynote SET note = ''+a,timestamp = ''+t,status ='A',title = ''+b WHERE id = " + id;
//
//                           // strSQL = "UPDATE mynote SET(note,timestamp,status,title) values('"+editTextnote.getText().toString()+"',formattedDate,'A','"+editTexttitle.getText().toString()+"') WHERE id = " + id;
//                          //  db.updateNote(idi,editTextnote.getText().toString(),formattedDate,"A",editTexttitle.getText().toString());
//
//
//
//
//
//                        } else {
//                            strSQL = "UPDATE mynote SET status ='B' WHERE id = " + id;
//                        }
//                        editTexttitle.setText("");
//                        editTextnote.setText("");
//                        db1.execSQL(strSQL);
//                        Intent i = new Intent(Addnote.this, MainActivity.class);
//                        startActivity(i);
//                        finish();
//                        SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
//                        settings.edit().clear().commit();
//
//                    }
//                }
                Intent i = new Intent(Addnote.this,MainActivity.class);
                startActivity(i);
                finish();


            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                if (editTexttitle.getText().toString().equals(""))
                {
                    Intent i = new Intent(Addnote.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();

                }
                else {
                    //String note,String timestamp, String status, String title)
                    if (type.equals("N")) {

                        db.InsertData(editTextnote.getText().toString(), formattedDate, "A",editTexttitle.getText().toString());
                        Intent i = new Intent(Addnote.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();

                    } else {
                        SQLiteDatabase db1 = db.getWritableDatabase();
                        String strSQL;


                        if (status.equals("A")) {

                            String strSQL1;
                            String strSQL2;
                            String strSQL3;

                            Integer idi = Integer.parseInt(id);
                            String a = ""+editTextnote.getText().toString();
                            String b = ""+editTexttitle.getText().toString();
                            String t = ""+formattedDate;

                            strSQL = "UPDATE mynote SET status = 'A' WHERE id = " + id;
                            strSQL1 = "UPDATE mynote SET note = '"+a+"' WHERE id = " + id;
                            strSQL2 = "UPDATE mynote SET timestamp = '"+t+"' WHERE id = " + id;
                            strSQL3 = "UPDATE mynote SET title = '"+b+"' WHERE id = " + id;
                            db1.execSQL(strSQL1);
                            db1.execSQL(strSQL2);
                            db1.execSQL(strSQL3);


                            //  strSQL = "UPDATE mynote SET note = ''+a,timestamp = ''+t,status ='A',title = ''+b WHERE id = " + id;

                            // strSQL = "UPDATE mynote SET(note,timestamp,status,title) values('"+editTextnote.getText().toString()+"',formattedDate,'A','"+editTexttitle.getText().toString()+"') WHERE id = " + id;
                            //  db.updateNote(idi,editTextnote.getText().toString(),formattedDate,"A",editTexttitle.getText().toString());





                        } else {
                            strSQL = "UPDATE mynote SET status ='B' WHERE id = " + id;
                        }
                        editTexttitle.setText("");
                        editTextnote.setText("");
                        db1.execSQL(strSQL);
                        Intent i = new Intent(Addnote.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();

                    }
                }

            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, editTextnote.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                if (type.equals("N"))
                {

                    db.InsertData(editTextnote.getText().toString(), formattedDate, "B",editTexttitle.getText().toString());
                    Intent i = new Intent(Addnote.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();

                }
                if (type.equals("A"))
                {
                    SQLiteDatabase db1 = db.getWritableDatabase();
                    String strSQL;
                    if(status.equals("A")){
                        strSQL = "UPDATE mynote SET status ='B' WHERE id = "+ id;
                    }else{ strSQL = "UPDATE mynote SET status ='A' WHERE id = "+ id;}
                    editTextnote.setText("");
                    db1.execSQL(strSQL);
                    Intent i = new Intent(Addnote.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();

                }


            }
        });
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db1 = db.getWritableDatabase();
                String strSQL;
                if(status.equals("A")){
                    strSQL = "UPDATE mynote SET status ='B' WHERE id = "+ id;
                }else{ strSQL = "UPDATE mynote SET status ='A' WHERE id = "+ id;}
                editTextnote.setText("");
                db1.execSQL(strSQL);
                Intent i = new Intent(Addnote.this, MainActivity.class);
                startActivity(i);
                finish();
                SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                settings.edit().clear().commit();

            }
        });
    }
}
