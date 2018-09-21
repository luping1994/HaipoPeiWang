package net.suntrans.suntranscomponents

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.blankj.utilcode.util.BarUtils
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

/**
 * Created by Looney on 2018/8/9.
 * Des:
 */
abstract class CBaseActivity : AppCompatActivity()  {


    protected var dialog: QMUITipDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //半透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }

        //全透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = Color.TRANSPARENT
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        }

        setContentView(getLayoutResId())
        fixStatusBar()
        setUpToolBar()
    }

    public open fun setUpToolBar(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null)
            setSupportActionBar(toolbar)
    }

    //设置返回按钮是否显示
    fun setBackArrowEnable(enable: Boolean) {
        supportActionBar!!.setDisplayShowHomeEnabled(enable)
        supportActionBar!!.setDisplayHomeAsUpEnabled(enable)
    }

    fun setToolBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    abstract fun getLayoutResId(): Int

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fixStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val statusBarFix = findViewById<View>(R.id.statusBarFix)
            val statusBarHeight = BarUtils.getStatusBarHeight()
            if (statusBarFix != null)
                statusBarFix!!.layoutParams.height = statusBarHeight
        }
    }

}