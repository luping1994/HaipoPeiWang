package net.suntrans.haipopeiwang

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.pgyersdk.update.PgyUpdateManager
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_me.*
import net.suntrans.haipopeiwang.activity.AboutActivity
import net.suntrans.haipopeiwang.activity.AutoLinkActivity
import net.suntrans.haipopeiwang.activity.LoginActivity
import net.suntrans.haipopeiwang.bean.RespondBody
import net.suntrans.haipopeiwang.fragment.SearchDeviceFragment
import net.suntrans.haipopeiwang.fragment.TabFragment
import net.suntrans.haipopeiwang.me.MeFragment
import net.suntrans.haipopeiwang.store.Store
import net.suntrans.haipopeiwang.utils.LogUtil
import net.suntrans.haipopeiwang.utils.UiUtils


class MainActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    private var wxapi: IWXAPI? = null
    private val requectCode = 101
    private val perssions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setBackArrowEnable(true)
//        setToolBarTitle(getString(R.string.app_name))
//        supportActionBar!!.setDisplayUseLogoEnabled(true)
//        supportActionBar!!.setLogo(R.drawable.action_logo)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(toggle)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perssions, requectCode)
        }

        toggle.syncState()
        val fragment = TabFragment()
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment, "MainFragment")
                .commit()
        wxapi = WXAPIFactory.createWXAPI(this, Config.WX_APP_ID)
//        if (!BuildConfig.DEBUG)

        setNavClickListener()

        checkUpdate()

    }

    public override fun setUpToolBar() {
//        super.setUpToolBar()
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        toolbar!!.setLogo(R.drawable.action_logo)
//        toolbar.inflateMenu(R.menu.menu_add)
//        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
//        collapsingToolbar.setScrimsShown(false)
//        collapsingToolbar.isTitleEnabled=true
//        collapsingToolbar.setExpandedTitleColor(Color.WHITE)
//        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE)
////        collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER)
//
//        setSupportActionBar(toolbar)
    }

    val INSTALL_PERMISS_CODE = 202
    private var qmuiDialog: QMUIDialog? = null

    private fun checkUpdate() {
        if (!BuildConfig.DEBUG)
            PgyUpdateManager.register(this)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val haveInstallPermission = applicationContext.getPackageManager().canRequestPackageInstalls()
//            if (!haveInstallPermission) {
//                if (qmuiDialog == null)
//                //直接进入手机中的wifi网络设置界面
//                    qmuiDialog = QMUIDialog.MessageDialogBuilder(this)
//                            .setMessage("为了保证获取最新版App,请允许安装应用")
//                            .setTitle(getString(R.string.tips))
//                            .addAction(R.string.cancel) { dialog, index ->
//                                dialog?.dismiss()
//                            }
//                            .addAction(R.string.ok) { dialog, index ->
//                                //权限没有打开则提示用户去手动打开
//                                var packageURI = Uri.parse("package:$packageName")
//                                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
//                                startActivityForResult(intent, INSTALL_PERMISS_CODE)
//                            }
//                            .create()
//                qmuiDialog!!.show()
//
//            }else{
//                //            if (!BuildConfig.DEBUG)
//                PgyUpdateManager.register(this)
//            }
//        } else {
////            if (!BuildConfig.DEBUG)
//                PgyUpdateManager.register(this)
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == INSTALL_PERMISS_CODE) {
            if (!BuildConfig.DEBUG)
                PgyUpdateManager.register(this)
        }
    }


    private fun setNavClickListener() {
        nav_view.findViewById<View>(R.id.exit)
                .setOnClickListener {
                    App.Companion.getSharedPreferences()
                            ?.edit()
                            ?.clear()
                            ?.apply()
                    ActivityUtils.finishAllActivities()
                    LoginActivity.start(this)
                }
        nav_view.findViewById<View>(R.id.about)
                .setOnClickListener {
                    AboutActivity.start(this)
                }

        Glide.with(this)
                .load(MeFragment.tempAvatar)
                .into(avatar)

        val bind = nav_view.findViewById<TextView>(R.id.bind)
        val usernameTx = nav_view.findViewById<TextView>(R.id.username)
        val username = App.getSharedPreferences()?.getString("username", "")
        usernameTx.text = username

        val wx_id = App.getSharedPreferences()?.getString("weixin_id", "")
        bind.setOnClickListener {
            if (!wx_id.equals("")) {
                QMUIDialog.MessageDialogBuilder(this)
                        .setMessage(getString(R.string.tips_binding_unbind))
                        .setTitle(getString(R.string.tips))
                        .addAction(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                        .addAction(R.string.ok) { dialog, _ ->
                            dialog!!.dismiss()
                            unBindWx()
                        }
                        .create()
                        .show()
                return@setOnClickListener
            }
            if (!wxapi!!.isWXAppInstalled) {
                UiUtils.showToast(getString(R.string.tips_not_find_wx))
                return@setOnClickListener
            }
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = "bind"
            wxapi!!.sendReq(req)
        }

        fab.setOnClickListener {

            val req = WXLaunchMiniProgram.Req()
            req.userName = "gh_2851b03100c0" // 填小程序原始id
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE// 可选打开 开发版，体验版和正式版
            wxapi!!.sendReq(req)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.add -> {
                AutoLinkActivity.start(this)
//                var path = Environment.getExternalStorageDirectory().absolutePath+File.separator+"Download"+File.separator+"abc.apk"
//                AppUtils.installApp(path)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            QMUIDialog.MessageDialogBuilder(this)
//                    .setMessage(getString(R.string.tips_exit))
//                    .setTitle(getString(R.string.tips))
//                    .addAction(R.string.cancel) { dialog, _ -> dialog.dismiss() }
//                    .addAction(R.string.ok) { dialog, _ ->
//                        dialog.dismiss()
//                        finish()
//                        Process.killProcess(Process.myPid())
//                    }
//                    .create()
//                    .show()
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onStop() {
        mDisposable.clear()
        super.onStop()
    }

    private fun unBindWx() {
        Store.unBindWx(object : Store.StoreCallBack<RespondBody<*>> {
            override fun onNext(t: RespondBody<*>) {
                if (t.isOk) {
                    App.getSharedPreferences()!!.edit()
                            .putString("weixin_id", "")
                            .commit()
                }
                UiUtils.showToast(t.msg)
            }

            override fun onError(e: Throwable) {
                UiUtils.showToast(e.localizedMessage)
            }

        })
    }

    override fun onRestart() {
        super.onRestart()
        LogUtil.e("MainActivity","onRestart")
    }
    override fun onDestroy() {
        if (!BuildConfig.DEBUG)
            PgyUpdateManager.unregister()
        super.onDestroy()
    }
}
