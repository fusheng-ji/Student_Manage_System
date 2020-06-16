package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserManageActivity extends AppCompatActivity {

    private EditText name;
    private EditText pwd;
    private EditText age;
    private Button add,delete,change;
    private ListView lv;
    private ImageButton pwdVisible;
    boolean isflag=true;

    dbHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ContentValues selCV;

    private ArrayList<Map<String,Object>> data;
    private  Map<String,Object> item;
    private SimpleAdapter listAdapter;
    View view;
    String selId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);

        name = (EditText) findViewById(R.id.name);
        pwd = (EditText) findViewById(R.id.pwd);
        age = (EditText) findViewById(R.id.age);
        add = (Button) findViewById(R.id.add);
        delete = (Button) findViewById(R.id.delete);
        change = (Button) findViewById(R.id.change);
        lv = (ListView) findViewById(R.id.lv);
        pwdVisible = (ImageButton) findViewById(R.id.pwdVisible);

        dbHelper = new dbHelper(this, "userinfo", null, 1);
        db = dbHelper.getWritableDatabase();
        data  = new ArrayList<Map<String,Object>>();

        dbFindAll();
        pwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isflag==true){
                    pwdVisible.setImageDrawable(getResources().getDrawable(R.drawable.eye_no));
                    isflag=false;
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    pwdVisible.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    isflag=true;
                    pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAdd();
                dbFindAll();
                name.setText("");
                pwd.setText("");
                age.setText("");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbDelete();
                dbFindAll();
                name.setText("");
                pwd.setText("");
                age.setText("");
                delete.setEnabled(false);
                change.setEnabled(false);
                name.setEnabled(true);
                Toast.makeText(UserManageActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUpdate();
                dbFindAll();
                name.setText("");
                pwd.setText("");
                age.setText("");
                delete.setEnabled(false);
                change.setEnabled(false);
                name.setEnabled(true);
                Toast.makeText(UserManageActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delete.setEnabled(true);
                change.setEnabled(true);
                name.setEnabled(false);
                Map<String,Object> listItem = (Map<String,Object>)lv.getItemAtPosition(position);
                name.setText((String)listItem.get("uname"));
                pwd.setText((String)listItem.get("pwd"));
                age.setText((String)listItem.get("age"));
                selId = (String) listItem.get("uid");
            }
        });
    }
    private  void  dbUpdate(){
        //更新条件
        String whereClause="uid=?";
        //更新的值
        String[] whereArgs = {String.valueOf(selId)};

        selCV= new ContentValues();//写入值
        //selCV.put("uname",name.getText().toString());
        if(!pwd.getText().toString().equals("")
                &&!age.getText().toString().equals("")){
            selCV.put("pwd",pwd.getText().toString());
            selCV.put("age",age.getText().toString());

            db.update("userinfo",selCV,whereClause,whereArgs);
        }else {
            Toast.makeText(UserManageActivity.this, "更新内容不能为空！", Toast.LENGTH_SHORT).show();
        }


    }
    private void  dbDelete(){
        //删除条件
        String whereClause="uid=?";
        //删除的值
        String[] whereArgs = {String.valueOf(selId)};
        db.delete("userinfo",whereClause,whereArgs);
    }
    private void  dbAdd(){
        boolean euqal=false;
        selCV = new ContentValues();
        if(!name.getText().toString().equals("")&&!pwd.getText().toString().equals("")
                &&!age.getText().toString().equals("")){
            selCV.put("uname",name.getText().toString());
            selCV.put("pwd",pwd.getText().toString());
            selCV.put("age",age.getText().toString());
            cursor = db.query("userinfo", null, null, null, null, null, "uid ASC");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (name.getText().toString().trim().equals(cursor.getString(1))) {
                    euqal=true;
                }
                cursor.moveToNext();
            }
            if(euqal==false){
                long rowId = db.insert("userinfo",null,selCV);
                if(rowId==-1){
                    Toast.makeText(UserManageActivity.this,"发生未知错误，增加失败",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(UserManageActivity.this, "增加成功！", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(UserManageActivity.this, "增加项用户名已存在！", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(UserManageActivity.this, "添加内容不能为空！", Toast.LENGTH_SHORT).show();
        }

    }
    private  void dbFindAll(){
        data.clear();
        cursor = db.query("userinfo",null,null,null,null,null,null);
        cursor.moveToFirst();
        //int num=1;
        while(!cursor.isAfterLast()){
            String uid=cursor.getString(0);
            String name=cursor.getString(1);
            String pwd = cursor.getString(2);
            String age = cursor.getString(3);
            item = new HashMap<String,Object>();
            item.put("uid",uid);
            item.put("name",name);
            item.put("pwd",pwd);
            item.put("age",age);
            data.add(item);
            cursor.moveToNext();
        }
        showList();
    }
    private void  showList(){
        listAdapter = new SimpleAdapter(this,data,
                R.layout.sqllist,
                new String[]{"uid","name","pwd","age"},
                new int[]{R.id.textView14,R.id.textView19,R.id.textView20,R.id.textView21});
        lv.setAdapter(listAdapter);
    }
}
