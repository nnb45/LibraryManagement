package com.example.librarymanagement.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.librarymanagement.database.DBHelper;

public class AdminDAO {
    DBHelper dbHelper;
    SharedPreferences sharedPreferences;

    public AdminDAO(Context context){
        dbHelper = new DBHelper(context);
        sharedPreferences = context.getSharedPreferences("THONGTIN", Context.MODE_PRIVATE);
    }
    // kiem tra thong tin dang nhap
    public boolean checkLogin(String matt, String matkhau){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM THUTHU WHERE matt = ? AND matkhau = ?", new String[]{matt, matkhau});
        if(cursor.getCount() !=0){
            cursor.moveToFirst();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id", cursor.getInt(0));
            editor.apply();
            return true;
        }
        return false;
    }
}