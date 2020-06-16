package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MyInfoActivity extends AppCompatActivity {
    private TextView name;
    private TextView pwd;
    private TextView age;
    private ImageView Avatar;
    dbHelper dbHelper;

    SQLiteDatabase db;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        name = (TextView) findViewById(R.id.name);
        pwd = (TextView) findViewById(R.id.pwd);
        age = (TextView) findViewById(R.id.age);
        Avatar = (ImageView) findViewById(R.id.Avatar);
        //创建连接数据库
        dbHelper = new dbHelper(this, "userinfo", null, 1);
        db = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        if(intent.getStringExtra("name")!=null&&intent.getStringExtra("pwd")!=null&&intent.getStringExtra("age")!=null) {
            name.setText("用户名："+intent.getStringExtra("name"));
            pwd.setText("密码："+intent.getStringExtra("pwd"));
            age.setText("年龄："+intent.getStringExtra("age"));
        }
        String[] Args={intent.getStringExtra("name")};
        cursor = db.rawQuery("select * from userinfo where uname = ?", new String[] {intent.getStringExtra("name")});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(cursor.getString(4)!=null){
                Bitmap bitmap= BitmapFactory.decodeFile(cursor.getString(4));
                Avatar.setImageBitmap(bitmap);
            }
            cursor.moveToNext();
        }
    }
}
