package net.suntrans.suntranscomponents.c4800.add

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.fragment_interaction_add.*
import net.suntrans.suntranscomponents.CBaseFragment
import net.suntrans.suntranscomponents.R
import net.suntrans.suntranscomponents.auto.ChannelSelectorActivity
import net.suntrans.suntranscomponents.auto.SrdSelectorActivity
import net.suntrans.suntranscomponents.bean.RoomSrd
import net.suntrans.suntranscomponents.c4800.InDo
import net.suntrans.suntranscomponents.c4800.InIf
import net.suntrans.suntranscomponents.c4800.Interaction
import net.suntrans.suntranscomponents.c4800.RoomChannel
import net.suntrans.suntranscomponents.c4800.selector.ChannelSelector
import net.suntrans.suntranscomponents.c4800.selector.DelayTimeSelector
import net.suntrans.suntranscomponents.c4800.selector.SrdSelector
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Looney on 2018/9/17.
 * Des:添加测控器感应
 */
open class InteractionAddFragment : CBaseFragment(), InteractionAddContract.View, DelayTimeSelector.numberSelectListener {


    companion object {
        const val deviceRequestCode = 101
        const val deviceResultCode = 102
        const val srdRequestCode = 103
        const val srdResultCode = 104

        const val FRAGMENT_ADD = "add"
        const val FRAGMENT_EDIT = "edit"

        const val START_TIME = "startTime"
        const val END_TIME = "endTime"

        fun newInstance(type: String, data: Interaction): InteractionAddFragment {
            val args = Bundle()
            args.putString("type", type)
            args.putParcelable("data", data)
            val fragment = InteractionAddFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var presenter: InteractionAddContract.Presenter? = null
    private var appCompatActivity: AppCompatActivity? = null
    private var delayTimeSelector: DelayTimeSelector? = null
    private var currentTimePosition = 0
    private var currentTimeType = START_TIME
    private var timeAdapter: TimeListAdapter? = null

    private var timeDialog: TimePickerDialog? = null

    var items: Array<String>? = null


    override fun setPresenter(presenter: InteractionAddContract.Presenter?) {
        this.presenter = presenter

    }


    override fun onDataError(code: Int, msg: String?) {
        if (msg != null) {
            showErr(msg)
        }
    }


    private var fragmentType = FRAGMENT_ADD
    private var intentData: Interaction? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intentData = arguments.getParcelable("data")
        fragmentType = arguments.getString("type")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_interaction_add, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val supportActionBar = (activity as AppCompatActivity).supportActionBar

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (fragmentType == FRAGMENT_ADD) {

            supportActionBar.setTitle(R.string.title_add_interaction)
        } else {
            supportActionBar.setTitle(R.string.title_edit_interaction)

        }

        timeAdapter = TimeListAdapter(R.layout.item_time, timeData)
        timeAdapter!!.setOnItemClickListener { adapter, view, position ->

        }
        val nowDate = TimeUtils.getNowDate()
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)


        timeAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            currentTimePosition = position
            if (timeDialog == null)
                timeDialog = TimePickerDialog(context, timePickListener, nowDate.hours, nowDate.minutes, true)
            if (view.id == R.id.startTime) {
                currentTimeType = START_TIME
            } else if (view.id == R.id.endTime) {
                currentTimeType = END_TIME
            }
            timeDialog!!.show()
        }

        timeAdapter!!.setOnItemClickListener { adapter, view, position ->
            QMUIDialog.MenuDialogBuilder(activity)
                    .addItems(items) { dialog, which ->
                        if (which == 0) {
                            timeData.removeAt(position)
                            adapter.notifyDataSetChanged()
                        }
                        dialog.dismiss()
                    }
                    .show()
        }

        timeList.adapter = timeAdapter
        timeList.isNestedScrollingEnabled = false
        items = arrayOf(getString(R.string.item_delete))
        initListener()

        init()


    }

    fun init() {
        if (fragmentType == FRAGMENT_EDIT) {
            if (intentData == null) {
                return
            }

            name.setText(intentData!!.name)//先设置名称

//            Log.e("whileX",intentData!!.whileX)
//            Log.e("doX",intentData!!.doX)

            val wh = JSON.parseObject(intentData!!.whileX, InIf::class.java)
            val d = JSON.parseArray(intentData!!.doX, InDo::class.java)

            d?.forEach {
                val c = RoomChannel.ChannelsBean()
                c.id = it.id
                c.area_id = it.area_id
                c.channel_id = it.channel_id
                c.title = it.title
                selectedChannel.add(c)
            }


            if (wh != null) {
                timeData.addAll(wh.systems)
                delayTimeValue = wh.systems[0].delay.toInt()

                youren = wh.conditions.youren
                wuren = wh.conditions.wuren

                val srd = RoomSrd.DeviceBean()
                srd.name = wh.device.title
                srd.title = wh.device.title
                srd.id = wh.device.dev_id
                srd.type = wh.device.device_type
                selectedSrd.add(srd)
            }


            updataUI()
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AppCompatActivity) {
            appCompatActivity = context
        } else {
            throw RuntimeException("The InteractionAddFragment must attach to a AppCompatActivity")
        }
    }


    fun initListener() {
        chooseSrd.setOnClickListener {
            val intent = Intent(activity, SrdSelectorActivity::class.java)
            intent.putParcelableArrayListExtra(SrdSelector.INTENT_SRD, selectedSrd)
            startActivityForResult(intent, srdRequestCode)
        }
        chooseDevices.setOnClickListener {
            val intent = Intent(activity, ChannelSelectorActivity::class.java)
            intent.putParcelableArrayListExtra(ChannelSelector.INTENT_CHANNEL, selectedChannel)
            startActivityForResult(intent, deviceRequestCode)
        }
        btYouren.setOnCheckedChangeListener { buttonView, isChecked ->
            youren = if (isChecked) {
                1
            } else {
                0
            }
        }

        btWuren.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                delayRoot.visibility = View.VISIBLE
                wuren = 1
            } else {
                delayRoot.visibility = View.GONE
                wuren = 0
            }
        }

        delayRoot.setOnClickListener {
            delayTimeSelector = childFragmentManager.findFragmentByTag("delayTimeSelector") as DelayTimeSelector?
            if (delayTimeSelector == null) {
                delayTimeSelector = DelayTimeSelector.newInstance(60)
                delayTimeSelector!!.setNumberSelectListener(this)
            }
            delayTimeSelector!!.show(childFragmentManager, "delayTimeSelector")
        }
        addTime.setOnClickListener {
            addTimeItem()
        }
        fab.setOnClickListener {
            commit()
        }
    }

    private val timePickListener = object : TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

            var h = ""
            var min = ""
            if (hourOfDay < 10) {
                h = "0${hourOfDay.toString()}"
            } else {
                h = hourOfDay.toString()
            }
            if (minute < 10) {
                min = "0$minute"
            } else {
                min = minute.toString()
            }


            if (currentTimeType == START_TIME) {
                timeData[currentTimePosition].startTime = "$h:$min"
            } else {
                timeData[currentTimePosition].endTime = "$h:$min"
            }
            timeAdapter!!.notifyDataSetChanged()
        }

    }


    fun addTimeItem() {
        if (timeData.size >= 3) {
            showToast(resources.getString(R.string.tips_no_more_time))
            return
        }
        val item = InIf.SystemsBean("时间", System.currentTimeMillis().toString(), "time", "1,2,3,4,5,6,7", "0")
        item.startTime = "19:00"
        item.endTime = "24:00"

        timeData.add(item)
        timeAdapter!!.notifyDataSetChanged()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == deviceRequestCode && resultCode == deviceResultCode) {
            if (data != null) {
                val extra = data!!.getParcelableArrayListExtra<RoomChannel.ChannelsBean>(ChannelSelector.INTENT_CHANNEL)
                if (extra != null) {
                    selectedChannel.clear()
                    selectedChannel.addAll(extra)
                    updataUI()
                }
            }
        }

        if (requestCode == srdRequestCode && resultCode == srdResultCode) {
            if (data != null) {
                val extra = data!!.getParcelableArrayListExtra<RoomSrd.DeviceBean>(SrdSelector.INTENT_SRD)
                if (extra != null) {
                    selectedSrd.clear()
                    selectedSrd.addAll(extra)
                    updataUI()
                }
            }
        }
    }

    //the adapter of Channel sceneRecyclerView
    private inner class TimeListAdapter(layoutResId: Int, data: List<InIf.SystemsBean>?) : BaseQuickAdapter<InIf.SystemsBean, BaseViewHolder>(layoutResId, data) {

        override fun convert(helper: BaseViewHolder, item: InIf.SystemsBean) {
            helper.setText(R.id.startTime, item.startTime)
                    .setText(R.id.endTime, item.endTime)
                    .addOnClickListener(R.id.startTime)
                    .addOnClickListener(R.id.endTime)

        }
    }

    override fun onGetValue(value: Int) {
        delayTimeValue = value
        updataUI()
    }

    private var selectedChannel = ArrayList<RoomChannel.ChannelsBean>()//已经选择的设备
    private var selectedSrd = ArrayList<RoomSrd.DeviceBean>()//已经选择de 测控器
    private var delayTimeValue = 0//延时时间
    private var timeData = ArrayList<InIf.SystemsBean>()//时间段
    private var youren = 1
    private var wuren = 1

    fun commit() {
        var nameValue = name.text.toString()
        var type = "2"


        if (nameValue.isEmpty()) {
            name.error = getString(R.string.tips_name_empty)
            return
        }
        if (selectedChannel.size == 0) {
            showToast(getString(R.string.tips_no_devices))
            return
        }
        if (selectedSrd.size == 0) {
            showToast(getString(R.string.tips_no_srd))
            return
        }
        for ((index, it) in timeData.withIndex()) {
            if (it.endTime <= it.startTime) {
                showToast(String.format(getString(R.string.tips_time_error), index + 1))
                return
            }
        }

        val dx = ArrayList<InDo>()
        selectedChannel.forEach {
            var d = InDo()
            d.area_id = it.area_id
            d.id = it.id
            d.channel_id = it.channel_id
            d.type = "channel"
            d.title = it.title
            dx.add(d)
        }

        val doX = JSON.toJSONString(dx)//

        /**条件
         * conditions : {"youren":1,"wuren":1}
         * systems : [{"title":"时间","id":"1533867460443","type":"time","startTime":"00:00","endTime":"23:59","repeat":"1,2,3,4,5,6,7","delay":0},{"title":"时间","id":1533867774596,"type":"time","startTime":"18:03","endTime":"18:59","delay":0}]
         * device : {"id":226,"title":"智能测控器","area":"客厅-T","parentId":2,"device_type":4800,"checked":true,"type":"srd","dev_id":226}
         */
        val wh = InIf()

        val systems = ArrayList<InIf.SystemsBean>()

        if (timeData.size == 0) {
            val item = InIf.SystemsBean("时间", System.currentTimeMillis().toString(), "time", "1,2,3,4,5,6,7", "0")
            item.startTime = "00:00"
            item.endTime = "24:00"
            item.delay = delayTimeValue.toString()
            systems.add(item)
        } else {
            timeData.forEach {
                it.delay = delayTimeValue.toString()
            }
            systems.addAll(timeData)
        }

        val conditionsBean = InIf.ConditionsBean()
        conditionsBean.wuren = wuren
        conditionsBean.youren = youren


        val dev_id = selectedSrd[0].id.toString()

        val d = InIf.DeviceBean()
        val area = if (selectedSrd[0].area == null) {
            ""
        } else {
            selectedSrd[0].area
        }
        d.area = area
        d.dev_id = selectedSrd[0].id
        d.device_type = selectedSrd[0].type
        d.title = area + selectedSrd[0].title


        wh.systems = systems
        wh.conditions = conditionsBean
        wh.device = d

        if (fragmentType == FRAGMENT_ADD) {
            presenter!!.addInteraction(nameValue, type, doX, JSON.toJSONString(wh), dev_id)
        } else {
            presenter!!.modifyInteraction(intentData!!.id.toString(), nameValue, type, doX, JSON.toJSONString(wh), dev_id)
        }
    }

    fun updataUI() {
        var deviceSB = StringBuilder()
        var srdSB = StringBuilder()
        selectedChannel.forEach {
            deviceSB.append(it.title)
                    .append(",")
        }
        selectedSrd.forEach {
            val area = if (it.area == null) {
                ""
            } else {
                it.area
            }
            srdSB.append(area)
                    .append(it.title)
                    .append(",")
        }

        if (deviceSB.length > 1)
            txDevices.text = deviceSB.toString().substring(0, deviceSB.length - 1)

        if (srdSB.length > 1)
            txSrd.text = srdSB.toString().substring(0, srdSB.length - 1)

        delayTime.text = (delayTimeValue.toString() + resources.getString(R.string.unit_min))

        btWuren.setCheckedImmediately(wuren == 1)
        btYouren.setCheckedImmediately(youren == 1)


        if (wuren == 1) {
            delayRoot.visibility = View.VISIBLE
        } else {
            delayRoot.visibility = View.GONE
        }

    }

    override fun showAddLoading() {
        if (loadingDialog == null) {
            loadingDialog = QMUITipDialog.Builder(context)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(getString(R.string.tips_loading))
                    .create()
        }
        loadingDialog!!.show()
    }

    override fun hideAddLoading() {
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
        }
    }


    override fun onCommit(code: Int, msg: String) {
        QMUIDialog.MessageDialogBuilder(context)
                .setMessage(msg)
                .setTitle(getString(R.string.tips))
                .setCancelable(false)
                .addAction(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    activity.finish()
                }
                .create()
                .show()
    }

    override fun onDestroyView() {
        presenter!!.destory()
        super.onDestroyView()
    }

}
