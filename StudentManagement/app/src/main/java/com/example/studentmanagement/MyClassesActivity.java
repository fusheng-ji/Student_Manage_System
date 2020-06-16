package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MyClassesActivity extends AppCompatActivity {
    private TextView name,teach,info;
    private ListView lv;
    private Button delete;
    private String uid="";
    boolean isflag = true;
    //class表对应
    dbHelper dbHelper;
    SQLiteDatabase db;

    Cursor cursor;
    ContentValues selCV;

    private ArrayList<Map<String, Object>> data;
    private Map<String, Object> item;
    private SimpleAdapter listAdapter;
    String selId;


    //sclass表对应
    dbHelper dbHelpersclass;
    SQLiteDatabase dbsclass;
    Cursor cursorsclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = (TextView) findViewById(R.id.name);
        teach = (TextView) findViewById(R.id.teach);
        info = (TextView) findViewById(R.id.info);

        lv = (ListView) findViewById(R.id.lv);
        delete = (Button) findViewById(R.id.delete);

        //获取用户名，并根据用户名查找该用户选取的课程
        Intent intent = getIntent();
        if (intent.getStringExtra("uid") != null ) {
            uid = intent.getStringExtra("uid");
        }
        dbHelper = new dbHelper(this, "class", null, 1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();


        dbHelpersclass= new dbHelper(this, "sclass", null, 1);
        dbsclass = dbHelper.getWritableDatabase();
        //找到课程编号
        findClassesByUid();


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbDelete();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delete.setEnabled(true);
                name.setEnabled(false);
                Map<String, Object> listItem = (Map<String, Object>) lv.getItemAtPosition(position);
                name.setText((String) listItem.get("name"));
                teach.setText((String) listItem.get("teach"));
                info.setText((String) listItem.get("info"));
                selId = (String) listItem.get("id");
            }
        });
    }

    private void dbDelete() {
        //删除条件
        String whereClause = "id=?";
        //删除的值
        String[] whereArgs = {String.valueOf(selId)};
        db.delete("sclass", whereClause, whereArgs);
        findClassesByUid();
        Toast.makeText(MyClassesActivity.this, "退课成功！", Toast.LENGTH_SHORT).show();
    }
    private void findClassesByUid() {
        data.clear();
        String[] selcetionArgs={uid};
        String[] Argsclass={};
        cursorsclass = dbsclass.query("sclass",null, "uid=?", selcetionArgs, null, null, null);;
        cursorsclass.moveToFirst();
        while (!cursorsclass.isAfterLast()) {
            Argsclass= new String[]{cursorsclass.getString(2)};
            cursor = db.query("class", null, "id=?", Argsclass, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                String teach = cursor.getString(2);
                String info = cursor.getString(3);
                item = new HashMap<String, Object>();
                item.put("id", cursorsclass.getString(0));
                item.put("name", name);
                item.put("teach", teach);
                item.put("info", info);
                data.add(item);
                cursor.moveToNext();
            }
            cursorsclass.moveToNext();
        }




        showList();
    }
        private void showList(){
            listAdapter = new SimpleAdapter(this, data,
                    R.layout.sqllist,
                    new String[]{"id", "name", "teach", "info"},
                    new int[]{R.id.textView14, R.id.textView19,
                            R.id.textView20, R.id.textView21});
            lv.setAdapter(listAdapter);
        }
    }

