package net.suntrans.haipopeiwang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about.*

import net.suntrans.haipopeiwang.BaseActivity
import net.suntrans.haipopeiwang.BuildConfig
import net.suntrans.haipopeiwang.R

/**
 * Created by Looney on 2018/8/21.
 * Des:
 */
class AboutActivity : BaseActivity() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_about
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackArrowEnable(true)
        setToolBarTitle(getString(R.string.title_about))
        version.text = BuildConfig.VERSION_NAME
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, AboutActivity::class.java)
            context.startActivity(starter)
        }
    }
}
