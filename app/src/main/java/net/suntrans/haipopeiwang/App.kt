package net.suntrans.haipopeiwang

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.support.multidex.MultiDex
import com.blankj.utilcode.util.Utils
import me.jessyan.autosize.AutoSizeConfig
import net.suntrans.haipopeiwang.store.RetrofitHelper
import net.suntrans.suntranscomponents.STComponents

/**
 * Created by Looney on 2018/8/9.
 * Des:
 */
class App : Application() {


    companion object {
        var app: Application? = null
        var sp: SharedPreferences? = null

        fun getSharedPreferences(): SharedPreferences? {
            if (sp == null) {
                sp = app!!.getSharedPreferences("st_pw", Context.MODE_PRIVATE)
            }
            return sp
        }

        fun getApplication(): Application? {
            return app
        }

    }


    override fun onCreate() {
        super.onCreate()
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return
//        }
//        LeakCanary.install(this)
        app = this
//        PgyCrashManager.register(this)
        Utils.init(this)
        STComponents.init(this, RetrofitHelper.BASE_URL3, "st_pw")
        AutoSizeConfig.getInstance().isUseDeviceSize = true
//        val screenDensity = ScreenUtils.getScreenDensity()
//        val screenDensityDpi = ScreenUtils.getScreenDensityDpi()
//        println("density=$screenDensity")
//        println("screenDensityDpi=$screenDensityDpi")


    }

    /**
     * 分割 Dex 支持 * @param base
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)


        MultiDex.install(this)
    }
}