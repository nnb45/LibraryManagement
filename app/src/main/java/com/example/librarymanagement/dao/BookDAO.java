package com.example.librarymanagement.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.librarymanagement.database.DBHelper;
import com.example.librarymanagement.model.Book;

import java.util.ArrayList;

public class BookDAO {
    DBHelper dbHelper;
    public BookDAO(Context context){
        dbHelper = new DBHelper(context);
    }
    // get all 'sach' from 'thuvien'
    public ArrayList<Book> getListBook(){
        ArrayList<Book> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM SACH", null);
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            do{
                list.add(new Book(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
            }while (cursor.moveToNext());
        }

        return list;
    }
}
