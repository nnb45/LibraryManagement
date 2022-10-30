package com.example.librarymanagement.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        super(context, "LIBRARY", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String dbThuThu = "CREATE TABLE THUTHU(matt text primary key, hoten text, matkhau text)";
        sqLiteDatabase.execSQL(dbThuThu);

        String dbThanhVien = "CREATE TABLE THANHVIEN(matv integer primary key autoincrement, hoten text, namsinh text)";
        sqLiteDatabase.execSQL(dbThanhVien);

        String dbLoai = "CREATE TABLE LOAISACH(maloai integer primary key autoincrement, tenloai text)";
        sqLiteDatabase.execSQL(dbLoai);

        String dbSach = "CREATE TABLE SACH(masach integer primary key autoincrement, tensach text, giathue integer, maloai integer references LOAISACH(maloai))";
        sqLiteDatabase.execSQL(dbSach);

        String dbPhieuMuon = "CREATE TABLE PHIEUMUON(mapm integer primary key autoincrement, matv integer references THANHVIEN(matv), matt text references THUTHU(matt), masach integer references SACH(masach), ngay text, trasach integer, tienthue integer)";
        sqLiteDatabase.execSQL(dbPhieuMuon);

        //data mau
        sqLiteDatabase.execSQL("INSERT INTO LOAISACH VALUES (1, 'Thiếu nhi'), (2, 'Tiểu thuyết'), (3, 'Tâm lý'), (4, 'Khoa học')");
        sqLiteDatabase.execSQL("INSERT INTO SACH VALUES (1, 'Doraemon', 20000, 1), (2, 'Harry Potter', 150000, 2), (3, 'Tâm lý học tội phạm', 95000, 3), (4, 'Mười vạn câu hỏi vì sao', 250000, 4)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i != i1){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS THUTHU");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS THANHVIEN");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LOAISACH");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SACH");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PHIEUMUON");
            onCreate(sqLiteDatabase);
        }
    }
}
