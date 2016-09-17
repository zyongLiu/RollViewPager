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
        uriList.add("http://h.hiphotos.baidu.com/album/w%3D2048/sign=730e7fdf95eef01f4d141fc5d4c69825/94cad1c8a786c917b8bf9482c83d70cf3ac757c9.jpg");
        uriList.add("http://g.hiphotos.baidu.com/album/w%3D2048/sign=00d4819db8014a90813e41bd9d4f3812/562c11dfa9ec8a137de469cff603918fa0ecc026.jpg");
        uriList.add("http://c.hiphotos.baidu.com/album/w%3D2048/sign=a8631adb342ac65c67056173cfcab011/b8389b504fc2d56217d11656e61190ef77c66cb4.jpg");
        uriList.add("http://e.hiphotos.baidu.com/album/w%3D2048/sign=ffac8994a71ea8d38a227304a332314e/1ad5ad6eddc451da4d9d32c4b7fd5266d01632b1.jpg");
        uriList.add("http://a.hiphotos.baidu.com/album/w%3D2048/sign=afbe93839a504fc2a25fb705d1e5e611/d058ccbf6c81800a99489685b03533fa838b478f.jpg");

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
