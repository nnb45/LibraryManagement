package com.example.librarymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.librarymanagement.dao.BookDAO;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}