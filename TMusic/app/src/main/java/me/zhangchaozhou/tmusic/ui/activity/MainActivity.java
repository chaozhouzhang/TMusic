package me.zhangchaozhou.tmusic.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhangchaozhou.base.activity.BaseActivity;
import me.zhangchaozhou.tmusic.R;
import me.zhangchaozhou.tmusic.ui.fragment.AccountFragment;
import me.zhangchaozhou.tmusic.ui.fragment.DiscoverFragment;
import me.zhangchaozhou.tmusic.ui.fragment.IMusicFragment;
import me.zhangchaozhou.tmusic.ui.fragment.TMusicFragment;

public class MainActivity extends BaseActivity implements OnSelectedItemChangeListener {


    private TMusicFragment mTMusicFragment;
    private IMusicFragment mIMusicFragment;
    private DiscoverFragment mDiscoverFragment;
    private AccountFragment mAccountFragment;
    private Fragment mCurrentFragment = null;

    static {
        System.loadLibrary("native-lib");
    }

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.bottom_navigation)
    BottomNavigation mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragments();
        setListeners();


    }


    /**
     * 初始化fragment
     */
    private void initFragments() {
        mTMusicFragment = new TMusicFragment();
        mIMusicFragment = new IMusicFragment();
        mDiscoverFragment = new DiscoverFragment();
        mAccountFragment = new AccountFragment();
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        mBottomNavigation.setOnSelectedItemChangeListener(this);
    }

    /**
     * 切换fragment，避免切换一直new fragment对象
     *
     * @param from
     * @param to
     */
    public void switchFragment(Fragment from, Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment == null) {
            transaction.add(R.id.container, to).commit();
            mCurrentFragment = to;
            return;
        }
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            if (!to.isAdded()) {
                if (from.isAdded()) {
                    transaction.hide(from).add(R.id.container, to).commit();
                } else {
                    transaction.add(R.id.container, to).commit();
                }
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }


    /**
     * 底部导航的选择响应
     *
     * @param id
     */
    @Override
    public void onSelectedItemChanged(int id) {
        switch (id) {
            case R.id.tab_tmusic:
                switchFragment(mCurrentFragment, mTMusicFragment);
                break;
            case R.id.tab_imusic:
                switchFragment(mCurrentFragment, mIMusicFragment);
                break;
            case R.id.tab_discover:
                switchFragment(mCurrentFragment, mDiscoverFragment);
                break;
            case R.id.tab_account:
                switchFragment(mCurrentFragment, mAccountFragment);
                break;
        }
    }


    /**
     * jni调用
     *
     * @return
     */
    public native String stringFromJNI();

}
