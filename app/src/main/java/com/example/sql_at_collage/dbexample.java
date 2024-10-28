package com.example.sql_at_collage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbexample extends SQLiteOpenHelper {
    public dbexample(Context context) {
        super(context, "information.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE test (ID INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS test");
        onCreate(db);
    }

    public void insert(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("INSERT INTO test (Name) VALUES ('"+name+"');");
    }
    //check if name is in the database or not, make it into a toast (search)
    public boolean delete(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM test WHERE Name='"+name+"';",null);
        if(cursor.getCount()==0){
            return false;
        }
        db.execSQL("DELETE FROM test WHERE Name='"+name+"';");
        return true;
    }

    public Cursor show(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM test;",null);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // Start transaction
        try {
            db.execSQL("DELETE FROM test;");
            db.setTransactionSuccessful(); // Mark transaction as successful
        } finally {
            db.endTransaction(); // End transaction
            db.close(); // Close the database
        }
    }

}