package com.example.studentmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetActivity extends AppCompatActivity {
    private Button button,exitsystem;
    private EditText name, pwd;
    private TextView back;

    dbHelper dbHelper;
    ContentValues selCV;
    SQLiteDatabase db;
    Cursor cursor;
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);


        button = (Button) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.name);
        pwd = (EditText) findViewById(R.id.pwd);
        back = (TextView) findViewById(R.id.back);
        exitsystem = (Button) findViewById(R.id.exitsystem);

        dbHelper = new dbHelper(this, "userinfo", null, 1);
        db = dbHelper.getWritableDatabase();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新条件
                String whereClause = "uname=?";
                selCV = new ContentValues();//写入值
                if (!name.getText().toString().equals("")
                        && !pwd.getText().toString().equals("")) {
                    //更新的值
                    String[] whereArgs = {name.getText().toString()};
                    selCV.put("pwd", pwd.getText().toString());
                    db.update("userinfo", selCV, whereClause, whereArgs);
                    Toast.makeText(ForgetActivity.this, "修改密码成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgetActivity.this, "更新内容不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetActivity.this.finish();
                Intent intent=new Intent();
                intent.setClass(ForgetActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        exitsystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab=new AlertDialog.Builder(ForgetActivity.this);
                ab.setIcon(R.mipmap.logo);
                ab.setTitle("警告");
                ab.setMessage("您是否要退出？");
                ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ForgetActivity.this.finish();
                        //System.exit(0);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                ab.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ForgetActivity.this,"明智的选择",Toast.LENGTH_SHORT).show();
                    }
                });
                ab.create().show();
            }
        });
    }
}