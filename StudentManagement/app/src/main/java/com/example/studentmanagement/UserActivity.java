package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    private Button myinfo;
    private Button classes;
    private TextView title;
    private Button ADD;

    String name,pwd,age,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        myinfo = (Button) findViewById(R.id.button);
        classes = (Button) findViewById(R.id.button3);
        title = (TextView) findViewById(R.id.title);
        ADD = (Button) findViewById(R.id.button2);
        Intent intent = getIntent();
        if(intent.getStringExtra("name")!=null&&intent.getStringExtra("pwd")!=null&&intent.getStringExtra("age")!=null) {
            title.setText("欢迎您:" + intent.getStringExtra("name"));
            name=intent.getStringExtra("name");
            pwd=intent.getStringExtra("pwd");
            age=intent.getStringExtra("age");
            uid=intent.getStringExtra("uid");
        }
        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("name",name);
                intent.putExtra("pwd",pwd);
                intent.putExtra("age",age);
                intent.setClass(UserActivity.this,MyInfoActivity.class);
                startActivity(intent);
            }
        });
        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("uid",uid);
                intent.setClass(UserActivity.this, MyClassesActivity.class);
                startActivity(intent);
            }
        });
        ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("uid",uid);
                intent.setClass(UserActivity.this, AddClassesActivity.class);
                startActivity(intent);
            }
        });
    }
}
