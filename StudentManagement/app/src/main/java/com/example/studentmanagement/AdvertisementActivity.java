package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class AdvertisementActivity extends AppCompatActivity {
    //倒计时
    private TextView timeout;

    Intent intent=new Intent();

    private RelativeLayout WelcomeLayout;

    Timer timer = new Timer();//计时器内置对象
    int num = 6;//打开程序的一瞬间显示6，而用户能看到的是5
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        WelcomeLayout=(RelativeLayout)findViewById(R.id.WelcomeLayout);
        WelcomeLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timer.cancel();
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://fusheng-ji.github.io/"));
                startActivity(intent);
            }
        });
        //多线程
        timeout = (TextView) findViewById(R.id.timeout);
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //线程执行内容\
                        num--;
                        timeout.setText("-"+num+"秒 -\n跳过");
                        if(num<1) {
                            timer.cancel();
                            intent.setClass(AdvertisementActivity.this, LoginActivity.class);
                            startActivity(intent);
                            AdvertisementActivity.this.finish();//把广告杀死
                        }
                    }
                });
            }
        };
        timer.schedule(task,1000,1000);
        timeout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                timer.cancel();
                intent.setClass(AdvertisementActivity.this, LoginActivity.class);
                startActivity(intent);
                AdvertisementActivity.this.finish();//把广告杀死
            }
        });
    }

}
