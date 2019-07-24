package com.yzy.pj

import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.yzy.baselibrary.base.activity.BaseActivity
import com.yzy.baselibrary.extention.*
import com.yzy.pj.ui.IndexFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    /** 点击退出的时间  */
    private var mExitTime: Long = 0
    private lateinit var indexFragment: IndexFragment
    override fun layoutResId(): Int = R.layout.activity_main;
    override fun initView() {
        finishAllActivity()
    }

    override fun initStatus() {
        immersionBar {
            statusBarColor(R.color.white)
            statusBarDarkFont(true)
            fitsSystemWindows(true)
        }
    }


    override fun initDate() {
        val defaultInfo = "屏幕宽度:$mScreenWidth\n屏幕高度:$mScreenHeight\n状态栏高度:$mStatusBarHeight"
        //键盘监听
        addListerKeyboard(naHeight = {
            LogUtils.e(
                "contentHeight" + String.format(
                    "%s\n虚拟导航键高度:%d\n键盘高度:%d",
                    defaultInfo,
                    it,
                    getHeightKeyboard()
                )
            )
        }, keyboardHeight = {
            LogUtils.e(
                "contentHeight" + String.format(
                    "%s\n虚拟导航键高度:%d\n键盘高度:%d",
                    defaultInfo,
                    getHeightNavigationBar(),
                    it
                )
            )
        })

        indexFragment = IndexFragment.newInstance()

        //添加到fm
        if (!indexFragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .add(R.id.itemLayoutView, indexFragment)
                .commitAllowingStateLoss()
        }
        supportFragmentManager.beginTransaction()
            .hide(indexFragment)
            .show(indexFragment)
            .commitAllowingStateLoss()
//        startActivity(Intent(this, TasteVideoActivity::class.java))
        tv_main_height.setOnClickListener {
            KeyboardUtils.showSoftInput()
        }
    }

    private fun finishAllActivity() {
        // 先关闭其他activity
        ActivityUtils.finishOtherActivities(this@MainActivity::class.java, false)
        //清理奔溃前的fragment
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2 * TimeConstants.SEC) {
            mContext.toast(R.string.exit_app)
            mExitTime = System.currentTimeMillis()
        } else {
            ActivityUtils.finishAllActivities()
            finish()
        }
    }
}
