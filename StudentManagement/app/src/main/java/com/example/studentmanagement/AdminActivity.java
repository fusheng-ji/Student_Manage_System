package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {
    private Button UserManage, ClassManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        UserManage = (Button) findViewById(R.id.UserManage);
        ClassManage = (Button) findViewById(R.id.ClassManage);
        UserManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AdminActivity.this, UserManageActivity.class);
                startActivity(intent);
            }
        });
        ClassManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AdminActivity.this, ManagerActivity.class);
                startActivity(intent);
            }
        });
    }
}