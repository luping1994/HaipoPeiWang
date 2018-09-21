package net.suntrans.haipopeiwang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.suntrans.haipopeiwang.BaseActivity
import net.suntrans.haipopeiwang.R
import net.suntrans.haipopeiwang.activity.LoginActivity
import net.suntrans.haipopeiwang.bean.ProConfig
import net.suntrans.haipopeiwang.store.Api
import net.suntrans.haipopeiwang.store.RetrofitHelper
import net.suntrans.haipopeiwang.tcp.TcpHelper
import net.suntrans.haipopeiwang.utils.Converts
import net.suntrans.haipopeiwang.utils.LogUtil
import net.suntrans.haipopeiwang.utils.UiUtils
import java.util.*

/**
 * Created by Looney on 2018/3/2.
 * Des:
 */

class BindActivity : BaseActivity(), TcpHelper.OnReceivedListener {


    private var titleTx: TextView? = null
    private var adapter: ArrayAdapter<String>? = null
    private var hostName: MutableList<String>? = null
    private var spinner: Spinner? = null
    private var data: List<ProConfig.DataBean>? = null
    private var helper: TcpHelper? = null
    private var type: String? = null
    private var code: String? = null


    //wifi模块重启命令
    private var rebootOrder = "00 00 00 00 03 03 00 04 00 00 00"
    private var order: String? = null


    private var refreshLayout: SwipeRefreshLayout? = null
    private var selectedPos = 0
    private var currentType: String = ""

    private val handler = Handler()

    override fun getLayoutResId(): Int {
        return R.layout.activity_host
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBackArrowEnable(true)
        val title = intent.getStringExtra("title")
        type = intent.getStringExtra("type")
        code = intent.getStringExtra("code")
        setToolBarTitle(title)


    }


    fun connectToServer(ip: String, port: Int) {

        helper = TcpHelper(this, ip, port, null)
        helper!!.setOnReceivedListener(this)
    }


    override fun onResume() {
        super.onResume()
        getData()
    }


    var api: Api? = null

    private fun getData() {

    }


    fun disconnect() {
        if (helper != null)
            helper!!.unRegister()
        helper = null
    }

    override fun onPause() {
        mDisposable.clear()
        super.onPause()
    }
    override fun onDestroy() {

        handler.removeCallbacksAndMessages(null)
        disconnect()
        super.onDestroy()
    }

    override fun onReceive(content: String?) {
        if (content == null) {
            return
        }
        LogUtil.i(TAG, content)

        //ab6843000000000003090018b339b15ebe3dfb47ab3de61b1f2d63a51bb4702e63bb900004651f9ad0d0a
        //ab6843000000000003090018b339b15ebe3dfb47ab3de61b1f2d63a51bb4702e63bb900004651f9ad0d00d0a
        //ab6843000000000003090018b339b15ebe3dfb47ab3de61b1f2d63a51bb4702e63bb900004651f9ad0d00d0a
        //        if (Config.CODE_ST_SLC_10.equals(type)){
        //            if (content==null){
        //                return;
        //            }
        //            if (content.length()==(order.length()+3)){
        //                if (dialog!=null){
        //                    dialog.dismiss();
        //                }
        //                UiUtils.showToast("命令已发送");
        //
        //            }
        //        }else {
        //            if (content.equals(order)){
        //                if (dialog!=null){
        //                    dialog.dismiss();
        //                }
        //                handler.postDelayed(new Runnable() {
        //                    @Override
        //                    public void run() {
        //                        helper.binder.sendOrder(rebootOrder);
        //
        //                    }
        //                },500);
        //            }
        //        }

        try {
//            if (dialog != null)补充
//                dialog!!.dismiss()
            if (content != null && order!!.length == content.length
                    && order!!.substring(order!!.length - 4, order!!.length) == content.substring(content.length - 4, content.length)) {
                rebootOrder = currentType + rebootOrder
                rebootOrder = Converts.getOrderWithCrc(rebootOrder)
                handler.postDelayed({
                    if (helper!!.binder != null)
                        helper!!.binder.sendOrder(rebootOrder)
                }, 500)
                UiUtils.showToast(getString(R.string.tips_cmd_sent))

            }

            //ab684300000000000309000e3138332e3233362e32352e3139300000238df2a20d0a
            //ab684300000000000309000e3138332e3233362e32352e3139300000238df2a20d0a
            if (content == "与服务器连接失败,重连中..." || content == "发送失败" || content == "连接中断") {
                UiUtils.showToast(content)
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onConnected() {}

    companion object {

        private val TAG = "HostActivity"

        fun start(ct: Context){
            val starter = Intent(ct, BindActivity::class.java)
            ct.startActivity(starter)
        }
    }
}
