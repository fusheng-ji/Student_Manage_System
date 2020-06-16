package com.example.studentmanagement;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TeachWelcomeActivity extends AppCompatActivity {
    private ViewPager vp;
    private TextView input;
    private RadioGroup radioGroup1;
    private RadioButton radioButton;
    private RadioButton radioButton2;
    private RadioButton radioButton3;

    ArrayList<View> vpList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_welcome);

        vp = (ViewPager) findViewById(R.id.vp);
        input = (TextView) findViewById(R.id.textView);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TeachWelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                TeachWelcomeActivity.this.finish();
            }
        });
        //设置某个界面填充布局
        LayoutInflater lif=LayoutInflater.from(this);
        View v1=lif.inflate(R.layout.p1,null);
        View v2=lif.inflate(R.layout.p2,null);
        View v3=lif.inflate(R.layout.p3,null);
        vpList = new ArrayList<View>();
        vpList.add(v1);
        vpList.add(v2);
        vpList.add(v3);
        //适配器adapter
        PagerAdapter pagerAdapter =new PagerAdapter() {
            @Override
            public int getCount() {
                return vpList.size();
            }

            @Override
            //判断是否由对象生成的界面
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            //获取当前界面的位置
            public Object instantiateItem(ViewGroup vg, int postion){
                vg.addView(vpList.get(postion));
                return vpList.get(postion);
            }
            //销毁上一个显示界面
            public void destroyItem(ViewGroup vg,int postion,Object object){
                vg.removeView(vpList.get(postion));
            }
        };
        vp.setAdapter(pagerAdapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        radioButton.setChecked(true);
                        input.setVisibility(View.GONE);
                        break;
                    case 1:
                        radioButton2.setChecked(true);
                        input.setVisibility(View.GONE);
                        break;
                    case  2:
                        radioButton3.setChecked(true);
                        input.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if(radioButton.getId()== checkedId){
                    vp.setCurrentItem(0);
                }else if(radioButton2.getId()== checkedId){
                    vp.setCurrentItem(1);
                }else {
                    vp.setCurrentItem(2);
                }
            }
        });
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://fusheng-ji.github.io/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://fusheng-ji.github.io/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://fusheng-ji.github.io/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }
}
