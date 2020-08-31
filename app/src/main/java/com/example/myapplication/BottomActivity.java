package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.bottom.AbnormalFragment;
import com.example.myapplication.bottom.DetailFragment;
import com.example.myapplication.bottom.ShopStatisticalFragment;
import com.example.myapplication.bottom.StatisticsFragment;
import com.example.myapplication.util.FragmentToFragment;
import com.example.myapplication.util.MyFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BottomActivity extends AppCompatActivity implements FragmentToFragment {


    private ViewPager viewPager;//声明ViewPager控件
    SharedPreferences sharedPreferences;

    //实例化控件参数
    private void initView() {
        String[] arrayOfString = getResources().getStringArray(R.array.tab_titles);//从项目资源目录获取底部选项卡文字
        ArrayList<Fragment> arrayList = new ArrayList();//声明一个Fragment页面组
        arrayList.add(new ShopStatisticalFragment());//向Fragment页面组中添加Fragment页面
        arrayList.add(new StatisticsFragment());//向Fragment页面组中添加Fragment页面
        arrayList.add(new DetailFragment());//向Fragment页面组中添加Fragment页面
        arrayList.add(new AbnormalFragment());//向Fragment页面组中添加Fragment页面
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        this.viewPager.setAdapter((PagerAdapter) new MyFragmentPagerAdapter(getSupportFragmentManager(), arrayList, arrayOfString));
        this.viewPager.setOffscreenPageLimit(4);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {//设置VirePager变化监听
            public void onPageScrollStateChanged(int param1Int) {
            }

            public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {
            }

            public void onPageSelected(int param1Int) {
            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab1);//实例化页面底部选项卡控件
        tabLayout.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) new TabLayout.OnTabSelectedListener() {//设置选项卡被选中监听
            public void onTabReselected(TabLayout.Tab param1Tab) {
            }

            public void onTabSelected(TabLayout.Tab param1Tab) {
            }

            public void onTabUnselected(TabLayout.Tab param1Tab) {
            }
        });
        tabLayout.setupWithViewPager(this.viewPager);//将ViewPager控件跟底部选项卡控件关联
        Drawable drawable = null;//声明图片资源
        for (byte b = 0; b < tabLayout.getTabCount(); b++) {//循环选项卡数量
            TabLayout.Tab tab = tabLayout.getTabAt(b);
            if (b != 0) {
                if (b != 1) {
                    if (b != 2) {
                        if (b == 3)
                            drawable = getResources().getDrawable(R.drawable.aborder);//实例化图片资源
                    } else {
                        drawable = getResources().getDrawable(R.drawable.ccc);//实例化图片资源
                    }
                } else {
                    drawable = getResources().getDrawable(R.drawable.detail);//实例化图片资源
                }
            } else {
                drawable = getResources().getDrawable(R.drawable.account);//实例化图片资源
            }
            tab.setIcon(drawable);//给底部选项卡设置图片
        }
    }

    //杀死进程，结束应用程序
    private void showExitConfirm() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    //Activity声明周期，创建方法
    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.bottom_activity);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        initView();
    }

    //设置按键监听
    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == 4)//如果按得是返回键
            showExitConfirm();//调用结束程序方法
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    //实现FragmentToFragment接口
    @Override
    public void shopStatisticalToStatistical(HashMap<String, String> paramHashMap) {
        new StatisticsFragment();
        Intent intent = new Intent("android.intent.action.CART_BROADCAST");//获取广播事件Intent
        for (Map.Entry<String, String> entry : paramHashMap.entrySet())//从广播事件中获取参数
            intent.putExtra((String) entry.getKey(), (String) entry.getValue());//向Intent中放入参数
        LocalBroadcastManager.getInstance((Context) this).sendBroadcast(intent);//开始广播
        this.viewPager.setCurrentItem(1);//将页面定位到第二个Fragment
    }
}
