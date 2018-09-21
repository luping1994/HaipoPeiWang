package net.suntrans.haipopeiwang.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.PermissionUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_LOADING
import net.suntrans.haipopeiwang.*
import net.suntrans.haipopeiwang.Config.*
import net.suntrans.haipopeiwang.activity.*
import net.suntrans.haipopeiwang.bean.DeviceInfo
import net.suntrans.haipopeiwang.bean.RespondBody
import net.suntrans.haipopeiwang.mdns.api.MDNS
import net.suntrans.haipopeiwang.mdns.helper.SearchDeviceCallBack
import net.suntrans.haipopeiwang.store.Store
import net.suntrans.haipopeiwang.utils.DialogUtils
import net.suntrans.haipopeiwang.utils.LogUtil
import net.suntrans.haipopeiwang.utils.UiUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.Map
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Looney on 2018/8/15.
 * Des:
 */
class SearchDeviceFragment : BaseFragment() {
    var findDeviceMap: MutableMap<String, DeviceInfo> = ConcurrentHashMap()
    private var datas: MutableList<DeviceInfo>? = null
    var recyclerView: RecyclerView? = null
    var refreshLayout: SwipeRefreshLayout? = null
    private var adapter: MyAdapter? = null
    private var mdns: MDNS? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return layoutInflater.inflate(R.layout.fragment_main, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerView)
        refreshLayout = view.findViewById(R.id.refreshlayout)
        setUpRecyclerView()
        listenwifichange()
//        searchThreadStart()
//        updateUI()
    }



    /**
     * as the methods name, set up the recyclerView
     */
    private fun setUpRecyclerView() {
        datas = ArrayList()
        refreshLayout!!.setColorSchemeColors(context!!.resources.getColor(R.color.colorPrimary))
        refreshLayout!!.setOnRefreshListener {
            handler.postDelayed({ refreshLayout!!.isRefreshing = false }, 500)
        }
        adapter = MyAdapter(R.layout.item_devices, datas)

        recyclerView!!.adapter = adapter

        recyclerView!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        adapter!!.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            if (position == -1) {
                UiUtils.showToast(getString(R.string.tips_waiting))
                return@OnItemChildClickListener
            }
            when (view.id) {
                R.id.host -> {
                    val intent = Intent(context, HostActivity::class.java)
                    intent.putExtra("title", datas!!.get(position).Name)
                    intent.putExtra("ip", datas!!.get(position).IP)
                    intent.putExtra("port", datas!!.get(position).Port)
                    if (datas!!.get(position).Name.contains(Config.ST_SLC_6)) {
                        intent.putExtra("type", Config.ST_SLC_6)
                        intent.putExtra("code", Config.CODE_ST_SLC_6)
                    } else if (datas!!.get(position).Name.contains(Config.ST_SLC_10)) {
                        intent.putExtra("type", Config.ST_SLC_10)
                        intent.putExtra("code", Config.CODE_ST_SLC_10)
                    } else if (datas!!.get(position).Name.contains(Config.ST_Sensus)) {
                        intent.putExtra("type", Config.ST_Sensus)
                        intent.putExtra("code", Config.CODE_SENSUS)
                    } else if (datas!!.get(position).Name.contains(Config.ST_SLC_3_2)) {
                        intent.putExtra("type", Config.ST_SLC_3_2)
                        intent.putExtra("code", Config.CODE_ST_SLC_3_2)
                    } else if (datas!!.get(position).Name.contains(Config.ST_SRD)) {
                        intent.putExtra("type", Config.ST_SRD)
                        intent.putExtra("code", Config.CODE_ST_SRD)
                    } else if (datas!!.get(position).Name.contains(Config.ST_ITL)) {
                        intent.putExtra("type", Config.ST_ITL)

                    } else if (datas!!.get(position).Name.contains(Config.ST_SECC)) {
                        intent.putExtra("type", Config.ST_SECC)
                        intent.putExtra("code", Config.CODE_ST_SECC)
                    } else if (datas!!.get(position).Name.contains(Config.ST_SECC)) {
                        intent.putExtra("type", Config.ST_SLC_2)
                        intent.putExtra("code", Config.CODE_ST_SLC_2)
                    } else if (datas!!.get(position).Name.contains(Config.ST_SLC_2PLUS)) {
                        intent.putExtra("type", Config.ST_SLC_2PLUS)
                        intent.putExtra("code", Config.CODE_ST_SLC_2PLUS)
                    } else {
                        return@OnItemChildClickListener
                    }
                    startActivity(intent)
                }
                R.id.canshu -> {
                    val intent = Intent(context, ParamActivity::class.java)
                    intent.putExtra("title", datas!!.get(position).Name)
                    intent.putExtra("ip", datas!!.get(position).IP)
                    intent.putExtra("port", datas!!.get(position).Port)
                    if (datas!!.get(position).Name.contains(Config.ST_SLC_6)) {
                        intent.putExtra("type", Config.ST_SLC_6)
                    } else if (datas!!.get(position).Name.contains(Config.ST_SLC_10)) {
                        intent.putExtra("type", Config.ST_SLC_10)
                    }
                    startActivity(intent)
                }
                R.id.bind -> {
                    bind(datas!![position])
                }
                else -> UiUtils.showToast(getString(R.string.tips_not_sup_device))
            }
        }

        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            if (position == -1) {
                UiUtils.showToast("请稍后再试!")
                return@OnItemClickListener
            }
            if (datas!!.get(position).Name.contains(ST_SLC_6)) {

                val intent = Intent(context, SLC6ControlActivity::class.java)
                intent.putExtra("type", Config.CODE_ST_SLC_6)
                intent.putExtra("ip", datas!!.get(position).IP)
                intent.putExtra("port", datas!!.get(position).Port)
                intent.putExtra("title", datas!!.get(position).Name)
                startActivity(intent)

            } else if (datas!!.get(position).Name.contains(ST_SLC_10)) {

                val intent = Intent(context, SLC6ControlActivity::class.java)
                intent.putExtra("type", Config.CODE_ST_SLC_10)
                intent.putExtra("ip", datas!!.get(position).IP)
                intent.putExtra("port", datas!!.get(position).Port)
                intent.putExtra("title", datas!!.get(position).Name)
                startActivity(intent)

            } else if (datas!!.get(position).Name.contains(ST_Sensus)) {
                val intent = Intent(context, SensusActivity::class.java)
                intent.putExtra("type", Config.CODE_SENSUS)
                intent.putExtra("ip", datas!!.get(position).IP)
                intent.putExtra("port", datas!!.get(position).Port)
                intent.putExtra("title", datas!!.get(position).Name)
                startActivity(intent)
            } else if (datas!!.get(position).Name.contains(Config.ST_SLC_3_2)) {
                val intent = Intent(context, SLC6ControlActivity::class.java)
                intent.putExtra("type", Config.CODE_ST_SLC_3_2)
                intent.putExtra("ip", datas!!.get(position).IP)
                intent.putExtra("port", datas!!.get(position).Port)
                intent.putExtra("title", datas!!.get(position).Name)
                startActivity(intent)
            } else if (datas!!.get(position).Name.contains(Config.ST_SLC_2PLUS)) {
                val intent = Intent(context, SLC6ControlActivity::class.java)
                intent.putExtra("type", Config.CODE_ST_SLC_2PLUS)
                intent.putExtra("ip", datas!!.get(position).IP)
                intent.putExtra("port", datas!!.get(position).Port)
                intent.putExtra("title", datas!!.get(position).Name)
                startActivity(intent)
            } else if (datas!!.get(position).Name.contains(Config.ST_SLC_LOCK)) {
                val intent = Intent(context, SLockActivity::class.java)
                intent.putExtra("type", Config.CODE_ST_SLOCK)
                intent.putExtra("ip", datas!!.get(position).IP)
                intent.putExtra("port", datas!!.get(position).Port)
                intent.putExtra("title", datas!!.get(position).Name)
                startActivity(intent)
            }
        }
    }

    private var dataSize = 0
    /**
     * start the devices search
     */
    private fun searchThreadStart() {

        mdns = MDNS(context!!.applicationContext)
        mdns!!.startSearchDevices(EWM_SERVICE, object : SearchDeviceCallBack() {
            override fun onSuccess(code: Int, message: String) {
                super.onSuccess(code, message)
            }

            override fun onFailure(code: Int, message: String) {
                super.onFailure(code, message)
            }

            override fun onDevicesFind(code: Int, deviceStatus: JSONArray) {
                super.onDevicesFind(code, deviceStatus)
                dataSize = datas!!.size
                for (i in 0 until deviceStatus.length()) {
                    try {
                        val s = deviceStatus.get(i).toString()
                        val deviceInfo = JSON.parseObject(s, DeviceInfo::class.java)
                        if (!findDeviceMap.containsKey(deviceInfo.MAC)) {
                            val value = deviceInfo
                            if (value.Name != null) {
                                if (value.Name.contains("#")) {
                                    value.Name = value.Name.split("#")[0]
                                }
                            }
                            if (value.Name.contains(ST_SLC_10)) {
                                value.vtype = Config.CODE_ST_SLC_10
                            } else if (value.Name.contains(ST_SLC_6)) {
                                value.vtype = Config.CODE_ST_SLC_6
                            } else if (value.Name.contains(ST_Sensus)) {
                                value.vtype = Config.CODE_SENSUS

                            } else if (value.Name.contains(ST_SRD)) {
                                value.vtype = Config.CODE_ST_SRD
                            } else if (value.Name.contains(Config.ST_SLC_3_2)) {
                                value.vtype = Config.CODE_ST_SLC_3_2
                            }
                            findDeviceMap[deviceInfo.MAC] = deviceInfo
                            datas!!.add(value)

                            if (dataSize != datas!!.size) {
                                datas!!.sort()
                            }

                           handler.post {
                               if (dataSize != datas!!.size) {
                                   if (recyclerView!!.scrollState == RecyclerView.SCROLL_STATE_IDLE ||
                                           !recyclerView!!.isComputingLayout) {
                                       adapter!!.notifyDataSetChanged()
                                   }
                               }
                               dataSize = datas!!.size
                           }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

            }
        })
    }


    private var isUpdateUI: Boolean = true
    /**
     * update the UI since the data has changed
     */
    private fun updateUI() {
//        Thread(Runnable {
//            while (isUpdateUI) {
//                datas!!.clear()
//                val localIterator = findDeviceMap.entries.iterator()
//                while (localIterator.hasNext()) {
//                    val next = localIterator.next() as Map.Entry<String, DeviceInfo>
//                    val value = next.value
//                    if (value.Name != null) {
//                        if (value.Name.contains("#")) {
//                            value.Name = value.Name.split("#")[0]
//                        }
//                    }
//                    if (value.Name.contains(ST_SLC_10)) {
//                        value.vtype = Config.CODE_ST_SLC_10
//                    } else if (value.Name.contains(ST_SLC_6)) {
//                        value.vtype = Config.CODE_ST_SLC_6
//                    } else if (value.Name.contains(ST_Sensus)) {
//                        value.vtype = Config.CODE_SENSUS
//
//                    } else if (value.Name.contains(ST_SRD)) {
//                        value.vtype = Config.CODE_ST_SRD
//                    } else if (value.Name.contains(Config.ST_SLC_3_2)) {
//                        value.vtype = Config.CODE_ST_SLC_3_2
//                    }
//
//                    datas!!.add(value)
//                }
//
//                datas!!.sort()
//                activity!!.runOnUiThread {
//                    if (recyclerView!!.scrollState == RecyclerView.SCROLL_STATE_IDLE ||
//                            !recyclerView!!.isComputingLayout) {
//                        adapter!!.notifyDataSetChanged()
//                    }
//
//                }
//                Thread.sleep(2000)
//            }
//        }).start()
    }

    override fun onDestroyView() {
        isUpdateUI = false
        stopSearchThread()
        activity!!.unregisterReceiver(broadcastReceiver)
        super.onDestroyView()
    }


    //the recyclerView adapter based on BaseQuickAdapter
    internal inner class MyAdapter(layoutResId: Int, data: List<DeviceInfo>?) : BaseQuickAdapter<DeviceInfo, BaseViewHolder>(layoutResId, data) {
        var size = ConvertUtils.dp2px(48F)

        protected override fun convert(helper: BaseViewHolder, item: DeviceInfo) {
            val imageView = helper.getView<ImageView>(R.id.imageView)
            val name = item.Name

            helper.setText(R.id.name, name)
                    .setText(R.id.ip, "IP:" + item.IP)
                    .setText(R.id.mac, "MAC:" + item.MAC)
            var resID = R.mipmap.ic_launcher
            if (item.Name.contains(ST_SLC_10)) {
                resID = R.drawable.ic_shitongdao
            } else if (item.Name.contains(ST_SLC_6)) {
                resID = R.drawable.ic_liutongdao
            } else if (item.Name.contains(ST_Sensus)) {
                resID = R.drawable.diliugan
            } else if (item.Name.contains(ST_SRD)) {
                resID = R.drawable.srd

            } else if (item.Name.contains(Config.ST_SLC_3_2)) {
                resID = R.drawable.ic_32

            }

            helper.addOnClickListener(R.id.host)
                    .addOnClickListener(R.id.canshu)
                    .addOnClickListener(R.id.bind)
            Glide.with(context)
                    .load(resID)
                    .dontTransform()
                    .crossFade()
                    .override(size, size)
                    .into(imageView)
        }
    }

    private fun bind(device: DeviceInfo) {
        var s = device.Name
        var username = App.Companion.getSharedPreferences()!!.getString("username", "")
        QMUIDialog.MessageDialogBuilder(context)
                .setMessage("${getString(R.string.tips_bind_device)}$s 到 $username"+"账户?")
                .setTitle(getString(R.string.tips))
                .addAction(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .addAction(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    bindTrue(device)
                }
                .create()
                .show()

    }

    override fun onDestroy() {
        super.onDestroy()
        handler!!.removeCallbacksAndMessages(null)
    }

    private fun bindTrue(device: DeviceInfo) {
        var addr = device.Name.substring(device.Name.length - 8, device.Name.length)
        addr = Integer.parseInt(addr).toString()
        var json = JSONArray()
        var obj = JSONObject()
        obj.put("name", device.Name)
        obj.put("vtype", device.vtype)
        obj.put("micoOSRev", device.MICOOSRev)
        obj.put("ssid", "")
        obj.put("model", "")
        obj.put("addr", addr)
        obj.put("firmwareRev", device.FirmwareRev)
        obj.put("mac", device.MAC)
        obj.put("ipAddress", device.IP)
        obj.put("hardwareRev", "")
        obj.put("seed", device.Seed)
        obj.put("protocol", device.Protocol)
        obj.put("port", device.Port)
        obj.put("manufacturer", device.Manufacturer)

        json.put(obj)
        var d = json.toString()
        LogUtil.i("MainFragment", "==deivce==$d")
        if (dialog == null) {
            dialog = DialogUtils.showLoadingDialog(context, ICON_TYPE_LOADING, getString(R.string.tips_loading))
        }
        dialog!!.show()

        (activity as BaseActivity).mDisposable.add(Store.bindDevices(d, object : Store.StoreCallBack<RespondBody<*>> {
            override fun onNext(t: RespondBody<*>) {
                if (dialog != null) {
                    dialog!!.dismiss()
                }
                UiUtils.showToast(t.msg)
            }

            override fun onError(e: Throwable) {
                if (dialog != null) {
                    dialog!!.dismiss()
                }
                UiUtils.showToast(e.localizedMessage)
            }
        }))
    }

    private fun listenwifichange() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        //        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context!!.registerReceiver(broadcastReceiver, intentFilter)
    }


    private var dialog: QMUITipDialog? = null
    private var qmuiDialog: QMUIDialog? = null


    internal var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ConnectivityManager.CONNECTIVITY_ACTION == action) {
                val mConnectivityManager = context!!.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = mConnectivityManager.activeNetworkInfo
                if (netInfo != null && netInfo.isAvailable) {

                    /////////////网络连接
                    val name = netInfo.typeName
                    if (netInfo.type == ConnectivityManager.TYPE_WIFI) {//wifi网络

                        if(qmuiDialog!=null){
                            if (qmuiDialog!!.isShowing){
                                qmuiDialog!!.dismiss()
                            }
                        }
                        if (netInfo.detailedState == NetworkInfo.DetailedState.CONNECTED) {
                            if (null != mdns) {
                                stopSearchThread()
                            }
                            searchThreadStart()
                        }
                    } else {//不是wifi提示打开wifi
                        noWifiAlert()
                    }
                }
            }

        }
    }

    private fun noWifiAlert() {
        if (qmuiDialog == null)
          //直接进入手机中的wifi网络设置界面
            qmuiDialog = QMUIDialog.MessageDialogBuilder(context)
                    .setMessage(getString(R.string.tips_no_wifi))
                    .setTitle(getString(R.string.tips))
                    .addAction(R.string.cancel) { dialog, index ->
                        dialog?.dismiss()
                    }
                    .addAction(R.string.ok) { dialog, index ->
                        dialog?.dismiss()
                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) //直接进入手机中的wifi网络设置界面
                    }
                    .create()
        qmuiDialog!!.show()
    }


    private fun stopSearchThread() {
        if (mdns != null) {
            mdns!!.stopSearchDevices(object : SearchDeviceCallBack() {
               override fun onSuccess(code: Int, message: String) {
                    super.onSuccess(code, message)
                }
            })
            mdns = null
        }
    }


}