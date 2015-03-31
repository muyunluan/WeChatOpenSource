package com.wechat.fdeng.wechat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    /* store all Fragment */
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    /* title for each Fragment */
    private String[] mTitles = new String[]{"First Fragment", "Second Fragment", "Third Fragment", "Fourth Fragment"};
    private FragmentPagerAdapter mAdapter;

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setOverflowButtonAlways();
        /* disable actionbar icon */
        getActionBar().setDisplayShowHomeEnabled(false);

        initView();
        initData();

        mViewPager.setAdapter(mAdapter);

        initEvent();
    }

    private void initEvent() {
        mViewPager.setOnPageChangeListener(this);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);
        ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        /* start from Fragment one */
        one.setIconAlpha(1.0f);

    }

    private void initData() {
        /* init title for each Fragment */
        for (String title : mTitles) {
            TabFragment tabFragment = new TabFragment();
            /* use Bundle to transfer data to Fragment */
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.TITLE, title);
            tabFragment.setArguments(bundle);
            mTabs.add(tabFragment);
        }

        /* init FragmentPagerAdapter */
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mTabs.get(i);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        /* use reflection to display icon of menu item */
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * always display overflow button
     */
    private void setOverflowButtonAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(config, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        clickTab(v);

    }

    private void clickTab(View v) {
        resetOtherTabs();

        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
        if (v > 0) {
            ChangeColorIconWithText left = mTabIndicators.get(i);
            ChangeColorIconWithText right = mTabIndicators.get(i + 1);
            left.setIconAlpha(1 - v);
            right.setIconAlpha(v);
        }
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
