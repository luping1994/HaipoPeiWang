package net.suntrans.haipopeiwang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_LOADING
import kotlinx.android.synthetic.main.activity_login_step2.*
import net.suntrans.haipopeiwang.App

import net.suntrans.haipopeiwang.BaseActivity
import net.suntrans.haipopeiwang.MainActivity
import net.suntrans.haipopeiwang.R
import net.suntrans.haipopeiwang.bean.LoginData
import net.suntrans.haipopeiwang.bean.RespondBody
import net.suntrans.haipopeiwang.store.Store
import net.suntrans.haipopeiwang.utils.DialogUtils
import net.suntrans.haipopeiwang.utils.UiUtils

/**
 * Created by Looney on 2018/8/16.
 * Des:
 */
class LoginStep2 : BaseActivity() {

    private var phone: String = ""


    override fun getLayoutResId(): Int {
        return R.layout.activity_login_step2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        setSupportActionBar(toolbar)
        setBackArrowEnable(true)
        setToolBarTitle("")

        phone = intent.getStringExtra("phone")

        vCodeInput.setOnTextFinishListener {
            //            print(it)
            if (dialog == null) {
                dialog = DialogUtils.showLoadingDialog(this, ICON_TYPE_LOADING, getString(R.string.tips_login))
            }
            dialog!!.show()
            mDisposable.add(Store.loginByCode(phone, it, object : Store.StoreCallBack<RespondBody<LoginData>> {
                override fun onNext(s: RespondBody<LoginData>) {
                    dialog!!.dismiss()

                    if (s.isOk) {

                        UiUtils.showToast(getString(R.string.tips_login_success))

                        val currentTimeMillis = System.currentTimeMillis()

                        val wx_id = if (s.data.user.weixin_id == null) "" else s.data.user.weixin_id

                        App.Companion.getSharedPreferences()?.edit()
                                ?.putString("access_token", s.data.token.access_token)
                                ?.putString("refresh_token", s.data.token.refresh_token)
                                ?.putString("username", s.data.user.username)
                                ?.putString("phone", s.data.user.phone)
                                ?.putString("weixin_id", wx_id)
                                ?.putLong("expires_in", s.data.token.expires_in)
                                ?.putLong("currentTimeMillis", currentTimeMillis)
                                ?.apply()

                        MainActivity.start(this@LoginStep2)
                        ActivityUtils.finishAllActivities()
                    } else {
                        UiUtils.showToast(s.msg)
                    }
                }

                override fun onError(e: Throwable) {
                    dialog!!.dismiss()
                    UiUtils.showToast(e.localizedMessage)
                }
            }))
        }
    }

    companion object {
        fun start(context: Context, phone: String) {
            val starter = Intent(context, LoginStep2::class.java)
            starter.putExtra("phone", phone)
            context.startActivity(starter)
        }

        const val RESULT_CODE = 101

    }
}
