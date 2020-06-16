package com.example.studentmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private ImageView dropdown;
    MenuItem  gMenuItem=null;
   private  String waitname="";
    private EditText  name_text;
    EditText username,pwd,inCode;
    Button login,exitsystem,forget;
    TextView register,us,showCode;//实例化对象在函数外
    dbHelper dbHelper;

    SQLiteDatabase db;
    Cursor cursor;
    private CheckBox saveName,savePwd;
    private String sname="",spwd="";
    private EditText inputPhone;
    boolean flag = false;
    char[] checkCode={'0','1','2','3','4','5','6','7','8','9'};
    String word="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences sp=getSharedPreferences("sname",MODE_PRIVATE);
        final SharedPreferences sp1=getSharedPreferences("spwd",MODE_PRIVATE);
        waitname=sp.getString("sname","");
        //创建连接数据库
        dbHelper = new dbHelper(this, "userinfo", null, 1);
        db = dbHelper.getWritableDatabase();

        dropdown=(ImageView)findViewById(R.id.dropdown);
        name_text=(EditText) findViewById(R.id.name_text);
        forget = (Button) findViewById(R.id.forget);
        savePwd = (CheckBox) findViewById(R.id.savePwd);
        saveName = (CheckBox) findViewById(R.id.saveName);
        /*username = (EditText) findViewById(R.id.editText);*/
        pwd = (EditText) findViewById(R.id.editText2);
        inCode = (EditText) findViewById(R.id.editText3);
        login = (Button) findViewById(R.id.button);
        exitsystem = (Button) findViewById(R.id.exitsystem);
        register = (TextView) findViewById(R.id.textView4);
        us = (TextView) findViewById(R.id.textView6);
        showCode = (TextView) findViewById(R.id.textView9);

        sname=sp.getString("sname","");
        spwd=sp1.getString("spwd","");

        if(sp1!=null&&!sp1.equals("")){
            pwd.setText(sp1.getString("spwd",""));
            savePwd.setChecked(true);
        }
        if(sp!=null&&!sp.equals("")){
            /*username.setText(sp.getString("sname",""));*/
            name_text.setText(sp.getString("sname",""));
            saveName.setChecked(true);
        }
        showCode.setText(yzm());
        showCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCode.setText(yzm());
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String uid="";
                String un = name_text.getText().toString();
                String ps = pwd.getText().toString();
                String age ="";
                int count=0;
                cursor = db.query("userinfo", null, null, null, null, null, "uid ASC");
                cursor.moveToFirst();
                if (!inCode.getText().toString().equals(showCode.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
                }else{
                    // 超级管理员用户 admin 123会进入ManagerActivity
                    if(un.equals("admin")&&ps.equals("123")){
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                        if(saveName.isChecked()==true){
                            sname = name_text.getText().toString();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("sname",sname);
                            editor.commit();
                        }else{
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.commit();
                        }
                        if(savePwd.isChecked()==true){
                            spwd = pwd.getText().toString();
                            SharedPreferences.Editor editor = sp1.edit();
                            editor.putString("spwd",spwd);
                            editor.commit();
                        }else{
                            SharedPreferences.Editor editor = sp1.edit();
                            editor.clear();
                            editor.commit();
                        }
                    }else if(un.equals("admin")&&!ps.equals("123")){
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }else{
                        while (!cursor.isAfterLast()) {
                            if (un.trim().equals(cursor.getString(1)
                            ) && ps.trim().equals(cursor.getString(2))) {
                                age=cursor.getString(3);
                                uid=cursor.getString(0);
                                flag = true;

                            }else if(!un.trim().equals(cursor.getString(1)
                            ) && ps.trim().equals(cursor.getString(2))){
                                count =1;
                            }else if(un.trim().equals(cursor.getString(1)
                            ) && !ps.trim().equals(cursor.getString(2))){
                                count=2;
                            }
                            cursor.moveToNext();
                        }
                        if (flag == true) {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            flag = false;
                            Intent intent = new Intent();
                            intent.putExtra("name", un);
                            intent.putExtra("pwd",ps);
                            intent.putExtra("age",age);
                            intent.putExtra("uid",uid);
                            intent.setClass(LoginActivity.this, UserActivity.class);
                            startActivity(intent);
                            if(saveName.isChecked()==true){
                                //SharedPreferences sp2 = getSharedPreferences("sname",MODE_PRIVATE);
                                sname = name_text.getText().toString();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("sname",sname);
                                editor.commit();
                            }else{
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                            }
                            if(savePwd.isChecked()==true){
                                //SharedPreferences sp2 = getSharedPreferences("sname",MODE_PRIVATE);
                                spwd = pwd.getText().toString();
                                SharedPreferences.Editor editor = sp1.edit();
                                editor.putString("spwd",spwd);
                                editor.commit();
                            }else{
                                SharedPreferences.Editor editor = sp1.edit();
                                editor.clear();
                                editor.commit();
                            }
                        } else if (count==1) {
                            Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                        } else if (count==2) {
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                    count=0;
                }


            }
        });
        //注册
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //联系我们
        us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:18262604858");
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });
        //退出系统
        exitsystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                ab.setIcon(R.mipmap.logo);
                ab.setTitle("警告");
                ab.setMessage("您是否要退出？");
                ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.this.finish();
                        //System.exit(0);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                ab.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoginActivity.this, "明智的选择", Toast.LENGTH_SHORT).show();
                    }
                });
                ab.create().show();
            }
        });
        dropdown.setOnClickListener(this);

    }
    public  String yzm(){
        String str ="0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
        String str2[]=str.split(",");
        Random rand = new Random();
        int index = 0;
        String randStr ="";
        for(int i=0;i<4;i++){
            index = rand.nextInt(str2.length);
            randStr+=str2[index];
        }
        return randStr;
    }
    long firstTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event){
        long secondTime = System.currentTimeMillis();
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(secondTime-firstTime<2000){
                System.exit(0);
            }else{
                Toast.makeText(LoginActivity.this,"再按一次程序退出",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }
            return  true;
        }
        return super.onKeyDown(keyCode,event);
    }
    public void username_onClick(View v) {
        PopupMenu pm=new PopupMenu(this, name_text);

        pm.getMenuInflater().inflate(R.menu.menu_name, pm.getMenu());

        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                name_text.setText(item.getTitle());
                return false;
            }
        });
       pm.setOnMenuItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
//创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(this, v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.menu_name, popup.getMenu());
        gMenuItem=popup.getMenu().findItem(R.id.item01);
        gMenuItem.setTitle(waitname);
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);
        //显示(这一行代码不要忘记了)
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item01:
                name_text.setText(item.getTitle());
            default:
                break;
        }
        return false;
}
}
