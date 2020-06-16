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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class AddClassesActivity extends AppCompatActivity {
    private TextView name;
    private Button add;
    private ListView lv;
    private TextView teach;
    private TextView info;

    private String uid="";

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
        setContentView(R.layout.activity_add_classes);
        name = (TextView) findViewById(R.id.name);
        teach = (TextView) findViewById(R.id.teach);
        info = (TextView) findViewById(R.id.info);
        add = (Button) findViewById(R.id.add);
        lv = (ListView) findViewById(R.id.lv);
        Intent intent = getIntent();
        if (intent.getStringExtra("uid") != null ) {
            uid = intent.getStringExtra("uid");
        }
        dbHelper = new dbHelper(this, "class", null, 1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();


        dbHelpersclass= new dbHelper(this, "sclass", null, 1);
        dbsclass = dbHelper.getWritableDatabase();

        dbFindAll();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAdd();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> listItem = (Map<String, Object>) lv.getItemAtPosition(position);
                selId = (String) listItem.get("id");
                name.setText((String) listItem.get("name"));
                teach.setText((String) listItem.get("teach"));
                info.setText((String) listItem.get("info"));
            }
        });
    }
    private void dbAdd() {
        selCV = new ContentValues();
            selCV.put("uid", uid);
            selCV.put("cid", selId);
            int check=findClassesByUid(selId);
            if (check==1) {
                long rowId = db.insert("sclass", null, selCV);
                if (rowId == -1) {
                    Toast.makeText(AddClassesActivity.this, "发生未知错误，增加失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddClassesActivity.this, "增加成功！", Toast.LENGTH_SHORT).show();
                }
            }

    }
    private void dbFindAll() {
        data.clear();
        cursor = db.query("class", null, null, null, null, null, null);
        cursor.moveToFirst();
        //int num=1;
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String teach = cursor.getString(2);
            String info = cursor.getString(3);
            item = new HashMap<String, Object>();
            item.put("id", id);
            item.put("name", name);
            item.put("teach", teach);
            item.put("info", info);
            data.add(item);
            cursor.moveToNext();
        }
        showList();
    }
    private int findClassesByUid(String setId) {

            String[] selcetionArgs = {uid,setId};
            String[] Argsclass = {};
            cursorsclass = dbsclass.query("sclass", null, "uid=? and cid=?", selcetionArgs, null, null, null);
            cursorsclass.moveToFirst();
            while (!cursorsclass.isAfterLast()) {
                if (cursorsclass!=null) {
                    Toast.makeText(AddClassesActivity.this, "不能重复选课!", Toast.LENGTH_SHORT).show();
                    return 0;
                }
            }
            cursorsclass.moveToNext();
            return 1;

    }

    private void showList() {
        listAdapter = new SimpleAdapter(this, data,
                R.layout.sqllist,
                new String[]{"id", "name", "teach", "info"},
                new int[]{R.id.textView14, R.id.textView19, R.id.textView20, R.id.textView21});
        lv.setAdapter(listAdapter);
    }

}
