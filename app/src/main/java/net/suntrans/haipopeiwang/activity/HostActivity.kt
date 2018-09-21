package net.suntrans.haipopeiwang.activity
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
import io.reactivex.schedulers.Schedulers
import net.suntrans.haipopeiwang.BaseActivity
import net.suntrans.haipopeiwang.R
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

class HostActivity : BaseActivity(), TcpHelper.OnReceivedListener {


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

        spinner = findViewById(R.id.spinner)
        refreshLayout = findViewById(R.id.refreshlayout)
        hostName = ArrayList()
        adapter = ArrayAdapter(this, R.layout.item_spinner, hostName!!)
        val ip = intent.getStringExtra("ip")
        val port = intent.getIntExtra("port", -1)
        connectToServer(ip, port)
        spinner!!.adapter = adapter
        findViewById<View>(R.id.save)
                .setOnClickListener(View.OnClickListener {
                    if (selectedPos == 0) {
                        UiUtils.showToast(getString(R.string.title_choose_project))
                        return@OnClickListener
                    }
                    AlertDialog.Builder(this@HostActivity)
                            .setMessage(String.format(getString(R.string.tips_host_setting), hostName!![selectedPos]))
                            .setPositiveButton(getString(R.string.ok)) { dialog1, which ->
                                setRemoteIpPort()
                            }
                            .setNegativeButton(getString(R.string.cancel), null)
                            .create().show()
                })

        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedPos = position
                println(selectedPos)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        refreshLayout!!.setOnRefreshListener {
            getData()
        }
    }

    //发送设置远程服务器socket的命令
    private fun setRemoteIpPort() {
//        order = "00 00 00 00 03"
        var ipHex = ""
        var portHex = ""
        var ipLength = ""

        var ip = ""

        var deviceBean = data!![selectedPos - 1].sub.filter {
            it.dev_type == code
        }
        if (deviceBean.isNotEmpty()) {
            ip = deviceBean[0].host
            currentType = deviceBean[0].dev_type
            order = deviceBean[0].dev_type + "00 00 00 00 03"
            portHex = Integer.toHexString(deviceBean[0].port)
        } else {
//          if(dialog!!.isShowing){ 补充
//              dialog!!.dismiss()
//           }
            UiUtils.showToast(getString(R.string.tips_not_sup_device))
            return
        }

//        ip = "192.168.0.127"
//        portHex = Integer.toHexString(9001)

//        LogUtil.i("IP:" + ip)
//        LogUtil.i("order:" + order)

        portHex = getStringFormat(portHex, 8)
        ipHex = Converts.strToASCIIHex(ip)

        ipLength = getStringFormat(Integer.toHexString(ip.length), 2)


        order = order!! + "09 00"//09 00  socket1   // 0a 00  socket2
        order += ipLength//域名长度
        order += ipHex
        order += portHex

        order = Converts.getOrderWithCrc(order)

        order = order!!.toLowerCase()
        if (helper!!.binder != null)
            helper!!.binder.sendOrder(order)

    }

    private fun getStringFormat(str: String, targetLength: Int): String {
        val sb = StringBuilder()
        for (i in 0 until targetLength - str.length) {
            sb.append("0")
        }
        sb.append(str)
        return sb.toString()
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
        if (api == null)
            api = RetrofitHelper.getApi2()

        mDisposable.add(api!!.proConfig
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    refreshLayout!!.isRefreshing = false
                    data = it.data
                    hostName!!.clear()
                    hostName!!.add("请选择工程")
                    for (su in data!!) {
                        hostName!!.add(su.name)
                    }
                    adapter!!.notifyDataSetChanged()
                }, {
                    it.printStackTrace()
                    refreshLayout!!.isRefreshing = false
                    if (it.localizedMessage != null) {
                        UiUtils.showToast(it.localizedMessage)
                    }
                }))
//
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
    }
}
