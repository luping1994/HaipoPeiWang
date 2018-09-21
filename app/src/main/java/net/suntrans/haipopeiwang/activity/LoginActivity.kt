package net.suntrans.haipopeiwang.activity

import android.Manifest
import android.os.Bundle
import android.view.animation.Animation
import com.blankj.utilcode.util.ActivityUtils
import com.qmuiteam.qmui.alpha.QMUIAlphaViewHelper
import com.qmuiteam.qmui.util.QMUIDirection
import com.qmuiteam.qmui.util.QMUIViewHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_login.*
import net.suntrans.haipopeiwang.bean.LoginData
import net.suntrans.haipopeiwang.bean.RespondBody
import net.suntrans.haipopeiwang.store.Store
import net.suntrans.haipopeiwang.utils.DialogUtils
import net.suntrans.haipopeiwang.utils.UiUtils
import java.util.regex.Pattern
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.pgyersdk.update.PgyUpdateManager
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import net.suntrans.haipopeiwang.*
import net.suntrans.haipopeiwang.BuildConfig.DEBUG

/**
 * Created by Looney on 2018/8/16.
 * Des:
 */
class LoginActivity : BaseActivity() {

    companion object {
        const val LOGIN_TYPE_PASSWORD = 1
        const val LOGIN_TYPE_CODE = 2
        fun start(ct: Context) {
            val starter = Intent(ct, LoginActivity::class.java)
            ct.startActivity(starter)
        }

    }

    private val requectCode = 101
    private val perssions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)
    private var wxapi: IWXAPI? = null

    private var loginType = LOGIN_TYPE_CODE

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wxapi = WXAPIFactory.createWXAPI(this, Config.WX_APP_ID)
        setToolBarTitle("")
        setBackArrowEnable(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perssions, requectCode)
        }

        getCode!!.setOnClickListener { getPhoneCode() }

        login!!.setOnClickListener {
            login()
        }
        wxLogin!!.setOnClickListener {
            wxLogin()
        }

        changeLoginType.setOnClickListener {
            if (loginType == LOGIN_TYPE_PASSWORD) {
                changeLoginType.setText(R.string.title_password_login)

                QMUIViewHelper.fadeOut(loginPassword, 500, object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }

                }, true)
                QMUIViewHelper.fadeIn(loginCode, 500, object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }

                }, true)
                loginType = LOGIN_TYPE_CODE
                phone.setText(phone1.text.toString())

            } else {
                changeLoginType.setText(R.string.title_account_login)

                QMUIViewHelper.fadeOut(loginCode, 400, object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }

                }, true)
                QMUIViewHelper.fadeIn(loginPassword, 400, object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }

                }, true)
                loginType = LOGIN_TYPE_PASSWORD
                phone1.setText(phone.text.toString())

            }

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perssions, requectCode)
        }

        protocolTx.setOnClickListener {
            ProtocolActivity.start(this)
        }
        checkUpdate();
        /** 可选配置集成方式 **/
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
//                if (!BuildConfig.DEBUG)
//                    PgyUpdateManager.register(this)
//            }
//        } else {
//            if (!BuildConfig.DEBUG)
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
    private fun login() {

        val phoneNum = phone1!!.text.toString()
        val pattern = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$"
        val isPhoneNum = Pattern.matches(pattern, phoneNum)

        val password1 = password.text.toString()
        if (!isPhoneNum) {
            phone1!!.error = getString(R.string.tips_error_phone_num)
            return
        }
        login.isEnabled = false
        if (dialog == null) {
            dialog = DialogUtils.showLoadingDialog(this, QMUITipDialog.Builder.ICON_TYPE_LOADING, getString(R.string.tips_login))
        }
        dialog!!.setOnDismissListener { mDisposable.clear();login.isEnabled = true; }
        dialog!!.show()
        mDisposable.add(Store.loginByPassword(phoneNum, password1, object : Store.StoreCallBack<RespondBody<LoginData>> {
            override fun onNext(s: RespondBody<LoginData>) {
                dialog!!.dismiss()
                login.isEnabled = true

                if (s.isOk) {
                    UiUtils.showToast(getString(R.string.tips_login_success))

                    val currentTimeMillis = System.currentTimeMillis()

                    val wx_id = if (s.data.user.weixin_id == null) "" else s.data.user.weixin_id

                    App.getSharedPreferences()?.edit()
                            ?.putString("access_token", s.data.token.access_token)
                            ?.putString("refresh_token", s.data.token.refresh_token)
                            ?.putString("username", s.data.user.username)
                            ?.putString("name", s.data.user.name)
                            ?.putString("phone", s.data.user.phone)
                            ?.putString("weixin_id", wx_id)
                            ?.putLong("expires_in", s.data.token.expires_in)
                            ?.putLong("currentTimeMillis", currentTimeMillis)
                            ?.apply()

                    MainActivity.start(this@LoginActivity)
                    finish()
                } else {

                    UiUtils.showToast(s.msg)
                }
            }

            override fun onError(e: Throwable) {
                login.isEnabled = true
                dialog!!.dismiss()
                UiUtils.showToast(e.localizedMessage)
            }
        }))

    }

    //获取手机验证码
    private fun getPhoneCode() {

        val phoneNum = phone!!.text.toString()
        val pattern = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$"
        val isPhoneNum = Pattern.matches(pattern, phoneNum)

        if (!isPhoneNum) {
            phone!!.error = getString(R.string.tips_error_phone_num)
            return
        }
        getCode.isEnabled = false
        if (dialog == null) {
            dialog = DialogUtils.showLoadingDialog(this, QMUITipDialog.Builder.ICON_TYPE_LOADING, getString(R.string.tips_login))
        }
        dialog!!.show()
        dialog!!.setOnDismissListener { mDisposable.clear();getCode.isEnabled = true; }
        mDisposable.add(Store.getPhoneCode(phoneNum, object : Store.StoreCallBack<RespondBody<*>> {
            override fun onNext(s: RespondBody<*>) {
                dialog!!.dismiss()
                getCode.isEnabled = true
                if (s.isOk) {
                    LoginStep2.start(this@LoginActivity, phoneNum)
                } else {
                    UiUtils.showToast(s.msg)
                }
            }

            override fun onError(e: Throwable) {
                dialog!!.dismiss()
                getCode.isEnabled = true
                UiUtils.showToast(e.localizedMessage)
            }
        }))
    }

    private fun wxLogin() {
        if (!wxapi!!.isWXAppInstalled) {
            UiUtils.showToast(getString(R.string.tips_not_find_wx))
            return
        }
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "login"
        wxapi!!.sendReq(req)
    }


    override fun onDestroy() {
        mDisposable.clear()
        if (!DEBUG)
            PgyUpdateManager.unregister()
        super.onDestroy()
    }
}
