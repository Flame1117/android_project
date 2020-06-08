package com.example.administrator.myapp3;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Welcome2Activity extends AppCompatActivity {

    private ViewPager vp;
    private TextView input;
    private RadioGroup radioGroup1;
    private RadioButton one;
    private RadioButton two;
    private RadioButton three;

    ArrayList<View> vpList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);

        vp = (ViewPager) findViewById(R.id.vp);
        input = (TextView) findViewById(R.id.input);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        one = (RadioButton) findViewById(R.id.radioButton1);
        two = (RadioButton) findViewById(R.id.radioButton2);
        three = (RadioButton) findViewById(R.id.radioButton3);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Welcome2Activity.this,LoginActivity.class);
                startActivity(intent);
                Welcome2Activity.this.finish();
            }
        });

        //设置某一个界面的填充布局
        LayoutInflater lif = LayoutInflater.from(this);
        View v1 = lif.inflate(R.layout.s1,null);
        View v2 = lif.inflate(R.layout.s2,null);
        View v3 = lif.inflate(R.layout.s3,null);
        vpList = new ArrayList<View>();
        vpList.add(v1);
        vpList.add(v2);
        vpList.add(v3);

        //适配器Adapter
        PagerAdapter pAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return vpList.size();
            }

            @Override
            //判断是否是由对象生成的界面
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            //获取当前界面的位置
            public Object instantiateItem(ViewGroup vg,int postion){
                vg.addView(vpList.get(postion));
                return vpList.get(postion);
            }

            //销毁上一个显示界面
            public void destroyItem(ViewGroup vg,int postion,Object object){
                vg.removeView(vpList.get(postion));
            }
        };

        vp.setAdapter(pAdapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        one.setChecked(true);
                        input.setVisibility(View.GONE);
                        break;
                    case 1:
                        two.setChecked(true);
                        input.setVisibility(View.GONE);
                        break;
                    case 2:
                        three.setChecked(true);
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
                if(one.getId()==checkedId){
                    vp.setCurrentItem(0);
                }else if(two.getId()==checkedId){
                    vp.setCurrentItem(1);
                }else{
                    vp.setCurrentItem(2);
                }
            }
        });

        //每一个视图点击的时候都可以分别打开网页
        //第一个打开淘宝
        //第二个打开苏宁易购
        //第三个打开京东
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.taobao.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri2 = Uri.parse("http://www.suning.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri2);
                startActivity(intent);

            }
        });

        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri3 = Uri.parse("http://www.jd.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri3);
                startActivity(intent);

            }
        });

    }
}
