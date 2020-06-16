package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagerActivity extends AppCompatActivity {
    private EditText name;
    private EditText teach;
    private EditText info;
    private Button add;
    private Button delete;
    private Button change;
    private ListView lv;


    boolean isflag = true;

    dbHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ContentValues selCV;

    private ArrayList<Map<String, Object>> data;
    private Map<String, Object> item;
    private SimpleAdapter listAdapter;
    View view;
    String selId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        name = (EditText) findViewById(R.id.name);
        teach = (EditText) findViewById(R.id.teach);
        info = (EditText) findViewById(R.id.info);
        add = (Button) findViewById(R.id.add);
        delete = (Button) findViewById(R.id.delete);
        change = (Button) findViewById(R.id.change);
        lv = (ListView) findViewById(R.id.lv);
        dbHelper = new dbHelper(this, "class", null, 1);
        db=dbHelper.getWritableDatabase();

        data = new ArrayList<Map<String, Object>>();

        dbFindAll();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAdd();
                dbFindAll();
                name.setText("");
                teach.setText("");
                info.setText("");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbDelete();
                dbFindAll();
                name.setText("");
                teach.setText("");
                info.setText("");
                delete.setEnabled(false);
                change.setEnabled(false);
                name.setEnabled(true);
                Toast.makeText(ManagerActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUpdate();
                dbFindAll();
                name.setText("");
                teach.setText("");
                info.setText("");
                delete.setEnabled(false);
                change.setEnabled(false);
                name.setEnabled(true);
                Toast.makeText(ManagerActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delete.setEnabled(true);
                change.setEnabled(true);
                name.setEnabled(false);
                Map<String, Object> listItem = (Map<String, Object>) lv.getItemAtPosition(position);
                name.setText((String) listItem.get("name"));
                teach.setText((String) listItem.get("teach"));
                info.setText((String) listItem.get("info"));
                selId = (String) listItem.get("id");
            }
        });
    }

    private void dbUpdate() {
        //更新条件
        String whereClause = "id=?";
        //更新的值
        String[] whereArgs = {String.valueOf(selId)};

        selCV = new ContentValues();//写入值
        //selCV.put("name",name.getText().toString());
        if (!name.getText().toString().equals("")
                && !teach.getText().toString().equals("")) {
            selCV.put("teach", teach.getText().toString());
            selCV.put("name", name.getText().toString());

            db.update("class", selCV, whereClause, whereArgs);
        } else {
            Toast.makeText(ManagerActivity.this, "更新内容不能为空！", Toast.LENGTH_SHORT).show();
        }


    }

    private void dbDelete() {
        //删除条件
        String whereClause = "id=?";
        //删除的值
        String[] whereArgs = {String.valueOf(selId)};
        db.delete("class", whereClause, whereArgs);
    }

    private void dbAdd() {
        boolean euqal = false;
        selCV = new ContentValues();
        if (!name.getText().toString().equals("") && !teach.getText().toString().equals("")
                && !info.getText().toString().equals("")) {
            selCV.put("name", name.getText().toString());
            selCV.put("teach", teach.getText().toString());
            selCV.put("info", info.getText().toString());
            cursor = db.query("class", null, null, null, null, null, "id ASC");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (name.getText().toString().trim().equals(cursor.getString(1))) {
                    euqal = true;
                }
                cursor.moveToNext();
            }
            if (euqal == false) {
                long rowId = db.insert("class", null, selCV);
                if (rowId == -1) {
                    Toast.makeText(ManagerActivity.this, "发生未知错误，增加失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ManagerActivity.this, "增加成功！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ManagerActivity.this, "增加项用户名已存在！", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(ManagerActivity.this, "添加内容不能为空！", Toast.LENGTH_SHORT).show();
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
            //num++;
        }
        showList();
    }

    private void showList() {
        listAdapter = new SimpleAdapter(this, data,
                R.layout.sqllist,
                new String[]{"id", "name", "teach", "info"},
                new int[]{R.id.textView14, R.id.textView19, R.id.textView20, R.id.textView21});
        lv.setAdapter(listAdapter);
    }
}
