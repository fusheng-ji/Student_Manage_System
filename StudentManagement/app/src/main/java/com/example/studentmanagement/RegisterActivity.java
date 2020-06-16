package com.example.studentmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Target;

public class RegisterActivity extends AppCompatActivity {
    private Button register,Avatar,exitsystem,select;
    private EditText name;
    private EditText pwd;
    private TextView back;
    private EditText age;
    private ImageView AvatarView;
    private Uri imageUri;
    private String ImagePath;
    dbHelper dbHelper;

    SQLiteDatabase db;
    Cursor cursor;

    boolean flag = true;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Avatar = (Button) findViewById(R.id.Avatar);
        register = (Button) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.name);
        pwd = (EditText) findViewById(R.id.pwd);
        back = (TextView) findViewById(R.id.back);
        exitsystem = (Button) findViewById(R.id.exitsystem);
        age = (EditText) findViewById(R.id.age);
        AvatarView = (ImageView) findViewById(R.id.AvatarView);
        select = (Button) findViewById(R.id.select);

        //拍照上传头像
        Avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.canExecute()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imageUri= FileProvider.getUriForFile(RegisterActivity.this,"com.example.cameraalbumtest.fileprovider",outputImage);
                }else {
                    imageUri=Uri.fromFile(outputImage);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });
        //从相册选择头像
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
                Intent intent=new Intent();
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        //创建连接数据库
        dbHelper = new dbHelper(this, "userinfo", null, 1);
        db = dbHelper.getWritableDatabase();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().trim().equals("admin")){
                    Toast.makeText(RegisterActivity.this,"不允许使用admin作为用户名！！！",Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues values = new ContentValues();
                    cursor = db.query("userinfo",null,null,null,null,null,"uid ASC");
                    cursor.moveToFirst();
                    while(!cursor.isAfterLast()){
                        if(name.getText().toString().trim().equals(cursor.getString(1))){
                            flag = false;
                        }
                        cursor.moveToNext();
                    }
                    if(flag==true){
                        values.put("uname",name.getText().toString().trim());
                        values.put("pwd",pwd.getText().toString().trim());
                        values.put("age",age.getText().toString().trim());
                        values.put("imagePath",ImagePath);
                        long rowId = db.insert("userinfo",null,values);
                        if(rowId==-1){
                            Toast.makeText(RegisterActivity.this,"发生未知错误，注册失败",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                            RegisterActivity.this.finish();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this,"用户名已存在！", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                }
            }

        });


        exitsystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab=new AlertDialog.Builder(RegisterActivity.this);
                ab.setIcon(R.mipmap.logo);
                ab.setTitle("警告");
                ab.setMessage("您是否要退出？");
                ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RegisterActivity.this.finish();
                        //System.exit(0);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                ab.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(RegisterActivity.this,"明智的选择",Toast.LENGTH_SHORT).show();
                    }
                });
                ab.create().show();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
        protected void onActivityResult(int requestCode,int resultCode,Intent data){
            switch (requestCode){
                case TAKE_PHOTO:
                    if(resultCode==RESULT_OK){
                        try{
                            Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                            AvatarView.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                    break;
                case CHOOSE_PHOTO:
                    if(resultCode==RESULT_OK){
                        if(Build.VERSION.SDK_INT>=19){
                            handleImageOnKitKat(data);
                        }else{
                            handleImageBeforeKitKat(data);
                        }
                    }
                    break;
                default:
                    break;
            }
    }
    private void openAlbum(){
            Intent intent=new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            startActivityForResult(intent,CHOOSE_PHOTO);
    }
    @Override
        public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
            switch (requestCode){
                case 1:
                    if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        openAlbum();
                    }else{
                        Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection =MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);

        }}else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private  void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            AvatarView.setImageBitmap(bitmap);
            ImagePath=imagePath;
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
}
