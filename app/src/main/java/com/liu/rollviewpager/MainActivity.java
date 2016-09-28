package com.liu.rollviewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.rollviewpager.pagetransformer.DepthPageTransformer;
import com.liu.rollviewpager.pagetransformer.RotateDownPageTransformer;
import com.liu.rollviewpager.pagetransformer.ZoomOutPageTransformer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] titles;
    private ArrayList<View> dots;
    private TextView title;
    private LinearLayout mViewPagerLay;// 只需要一个layout即可,ViewPager是动态加载的
    private ArrayList<String> uriList;

    private RollViewPager mRollViewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        title = (TextView) findViewById(R.id.title);
        mViewPagerLay = (LinearLayout) findViewById(R.id.viewpager);
    }

    private void initListener() {
    }

    private void initData() {
        // 图片名称
        titles = new String[] { "测试1", "测试2", "测试3", "测试4", "测试5" };

        // 用来显示的点
        dots = new ArrayList<View>();
        dots.add(findViewById(R.id.dot_0));
        dots.add(findViewById(R.id.dot_1));
        dots.add(findViewById(R.id.dot_2));
        dots.add(findViewById(R.id.dot_3));
        dots.add(findViewById(R.id.dot_4));

        // 构造网络图片
        uriList = new ArrayList<String>();
        uriList.add("http://desk.fd.zol-img.com.cn/t_s960x600c5/g4/M01/0D/04/Cg-4WVP_npmIY6GRAKcKYPPMR3wAAQ8LgNIuTMApwp4015.jpg");
        uriList.add("http://tupian.enterdesk.com/2016/hxj/08/16/1602/ChMkJ1exsKSIaKyoAA0KjNDwWssAAUdNgCW6Z0ADQqk521.jpg");
        uriList.add("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1609/27/c0/27587202_1474952311163_800x600.jpg");
        uriList.add("http://bbs.crsky.com/1236983883/Mon_1208/25_187069_d7a7e318889e475.jpg");
        uriList.add("http://bbs.crsky.com/1236983883/Mon_1208/25_187069_c732fd838ff4ce7.jpg");

        mRollViewPager = new RollViewPager(this, dots,
                R.drawable.dot_focus, R.drawable.dot_normal,
                new RollViewPager.OnPagerClickCallback() {
                    @Override
                    public void onPagerClick(int position) {
                        Toast.makeText(MainActivity.this,
                                "第" + position + "张图片被点击了", Toast.LENGTH_SHORT).show();
                    }});

        mRollViewPager.setUriList(uriList);//设置网络图片的url
        mRollViewPager.setTitle(title, titles);//不需要显示标题，可以不设置
        mRollViewPager.startRoll();//不调用的话不滚动

        mViewPagerLay.addView(mRollViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Toast.makeText(this,item.getTitle(),Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.menu_cycle:
                mRollViewPager.setCycle(true);
                break;
            case R.id.menu_uncycle:
                mRollViewPager.setCycle(false);
                break;
            case R.id.menu_depth:
                mRollViewPager.setPageTransformer(true,new DepthPageTransformer());
                break;
            case R.id.menu_rotate:
                mRollViewPager.setPageTransformer(true,new RotateDownPageTransformer());
                break;
            case R.id.menu_zoomout:
                mRollViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        }
        return super.onOptionsItemSelected(item);
    }

}
