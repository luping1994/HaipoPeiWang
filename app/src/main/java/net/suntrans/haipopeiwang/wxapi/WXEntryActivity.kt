package net.suntrans.haipopeiwang.wxapi


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.disposables.CompositeDisposable
import net.suntrans.haipopeiwang.App

import net.suntrans.haipopeiwang.Config
import net.suntrans.haipopeiwang.MainActivity
import net.suntrans.haipopeiwang.R
import net.suntrans.haipopeiwang.bean.LoginData
import net.suntrans.haipopeiwang.bean.RespondBody
import net.suntrans.haipopeiwang.bean.WXLoginData
import net.suntrans.haipopeiwang.store.Store
import net.suntrans.haipopeiwang.utils.DialogUtils
import net.suntrans.haipopeiwang.utils.LogUtil
import net.suntrans.haipopeiwang.utils.UiUtils
import android.support.v4.media.session.MediaButtonReceiver.handleIntent
import com.alibaba.fastjson.JSON


class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {

    private var api: IWXAPI? = null
    private val tips: TextView? = null
    val mDisposable = CompositeDisposable()
    protected var dialog: QMUITipDialog? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, Config.WX_APP_ID)
        api!!.registerApp(Config.WX_APP_ID)
        try {
            val result = api!!.handleIntent(intent, this)
            if (!result) {
                LogUtil.d("参数不合法，未被SDK处理，退出")
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent);
        api!!.handleIntent(intent, this)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        api!!.handleIntent(data,this)
    }
    override fun onReq(req: BaseReq) {
        LogUtil.i("WXEntryActivity","BaseReq:"+ req.transaction)

    }

    override fun onResp(resp: BaseResp) {
        LogUtil.d("WXEntryActivity","baseResp:"+ JSON.toJSONString(resp))
        var result = ""
        when (resp.errCode) {

            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                result = "发送取消"

                finish()
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                result = "发送被拒绝"

                finish()
            }
            BaseResp.ErrCode.ERR_OK -> {
                handleResp(resp)
            }
            else -> {
                result = "发送返回"
                finish()
            }
        }


    }


    private fun handleResp(resp: BaseResp){
        if (resp is SendAuth.Resp) {

            if (resp.state == "login") {
                if (dialog == null) {
                    dialog = DialogUtils.showLoadingDialog(this, QMUITipDialog.Builder.ICON_TYPE_LOADING, getString(R.string.tips_login))
                }
                dialog!!.show()
                Store.loginByWX(Config.WX_APP_ID, resp.code, Config.GRANT_TYPE, Config.SECRET,object :Store.StoreCallBack<RespondBody<LoginData>> {
                    override fun onNext(t: RespondBody<LoginData>) {
                        if (dialog != null)
                            dialog!!.dismiss()

                        if (t.isOk) {
                            UiUtils.showToast(getString(R.string.tips_login_success))
                            val currentTimeMillis = System.currentTimeMillis()
                            App.Companion.getSharedPreferences()?.edit()
                                    ?.putString("access_token", t.data.token.access_token)
                                    ?.putString("refresh_token", t.data.token.refresh_token)
                                    ?.putString("username", t.data.user.username)
                                    ?.putString("phone", t.data.user.phone)
                                    ?.putString("weixin_id", t.data.user.weixin_id)
                                    ?.putLong("expires_in", t.data.token.expires_in)
                                    ?.putLong("currentTimeMillis", currentTimeMillis)
                                    ?.apply()
                            MainActivity.start(this@WXEntryActivity)
                            finish()
                        } else {
                            UiUtils.showToast(t.msg)
                            finish()
                        }


                    }

                    override fun onError(e: Throwable) {
                        if (dialog != null)
                            dialog!!.dismiss()
                        finish()
                    }

                })
//                Store.getOpenId(Config.WX_APP_ID, resp.code, Config.GRANT_TYPE, Config.SECRET, object : Store.StoreCallBack<WXLoginData> {
//                    override fun onNext(s: WXLoginData) {
//                        Store.loginByWX(s.openid, object : Store.StoreCallBack<RespondBody<LoginData>> {
//                            override fun onNext(t: RespondBody<LoginData>) {
//                                if (dialog != null)
//                                    dialog!!.dismiss()
//
//                                if (t.isOk) {
//                                    UiUtils.showToast(getString(R.string.tips_login_success))
//                                    val currentTimeMillis = System.currentTimeMillis()
//                                    App.Companion.getSharedPreferences()?.edit()
//                                            ?.putString("access_token", t.data.token.access_token)
//                                            ?.putString("refresh_token", t.data.token.refresh_token)
//                                            ?.putString("username", t.data.user.username)
//                                            ?.putString("phone", t.data.user.phone)
//                                            ?.putString("weixin_id", t.data.user.weixin_id)
//                                            ?.putLong("expires_in", t.data.token.expires_in)
//                                            ?.putLong("currentTimeMillis", currentTimeMillis)
//                                            ?.apply()
//                                    MainActivity.start(this@WXEntryActivity)
//                                    finish()
//                                } else {
//                                    UiUtils.showToast(t.msg)
//                                    finish()
//                                }
//
//
//                            }
//
//                            override fun onError(e: Throwable) {
//                                if (dialog != null)
//                                    dialog!!.dismiss()
//                                finish()
//
//                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                            }
//
//                        })
//
//                    }
//
//                    override fun onError(e: Throwable) {
//                        e.printStackTrace()
//                        if (dialog != null)
//                            dialog!!.dismiss()
//                        UiUtils.showToast(e.localizedMessage)
//                        finish()
//                    }
//                })
            } else if (resp.state == "bind") {
                if (dialog == null) {
                    dialog = DialogUtils.showLoadingDialog(this, QMUITipDialog.Builder.ICON_TYPE_LOADING, getString(R.string.tips_login))
                }
                dialog!!.show()
                Store.getOpenId(Config.WX_APP_ID, resp.code, Config.GRANT_TYPE, Config.SECRET, object : Store.StoreCallBack<WXLoginData> {
                    override fun onNext(s: WXLoginData) {
                        Store.bindWxRaw(s.openid, object : Store.StoreCallBack<RespondBody<*>> {
                            override fun onNext(t: RespondBody<*>) {
                                if (dialog != null)
                                    dialog!!.dismiss()
                                UiUtils.showToast(t.msg)
                                finish()
                            }

                            override fun onError(e: Throwable) {
                                if (dialog != null)
                                    dialog!!.dismiss()
                                finish()
                            }

                        })
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        if (dialog != null)
                            dialog!!.dismiss()
                        UiUtils.showToast(e.localizedMessage)
                        finish()

                    }
                })

            }
            //获取微信传回的code
        } else {
            finish()
        }
    }
    companion object {

        private val TAG = "WXPayEntryActivity"
    }
}