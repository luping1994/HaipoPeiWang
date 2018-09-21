package net.suntrans.suntranscomponents.c4800.selector

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import kotlinx.android.synthetic.main.fragment_expand.*

import net.suntrans.suntranscomponents.CBaseFragment
import net.suntrans.suntranscomponents.R
import net.suntrans.suntranscomponents.bean.RoomSrd
import net.suntrans.suntranscomponents.c4800.add.InteractionAddFragment

/**
 * Created by Looney on 2018/9/18
 * Des:测控器选择器(兼 第六感选择器)
 */
class SrdSelector : CBaseFragment(), SrdSelectorContract.View {


    companion object {
        const val INTENT_SRD = "srd"
        val datas = ArrayList<RoomSrd>()

        fun newInstance(): SrdSelector {
            val args = Bundle()
            val fragment = SrdSelector()
            fragment.arguments = args
            return fragment
        }
    }


    override fun setPresenter(presenter: SrdSelectorContract.Presenter?) {
        this.presenter = presenter
    }

    override fun onDataError(code: Int, msg: String?) {
        showErr(msg)
    }

    private var adapter: SrdAdapter? = null
    private var presenter: SrdSelectorContract.Presenter? = null
    private val stopRefreshRunnable = Runnable {
        refreshLayout.isRefreshing = false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_expand, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

//        expandListView.setGroupIndicator(null)
        adapter = SrdAdapter(datas, context)
        expandListView.setAdapter(adapter)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary), resources.getColor(R.color.colorAccent))

        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setTitle(R.string.title_srd_list)

        val extra = activity.intent.getParcelableArrayListExtra<RoomSrd.DeviceBean>(INTENT_SRD)
        if (extra != null) {
            selected.clear()
            selected.addAll(extra)
        }
        getData(false)
        refreshLayout.setOnRefreshListener {
            getData(true)
        }
        initListener()
    }

    fun getData(force: Boolean) {
        if (!force) {
            if (datas.size != 0) {
                for ((index,item2) in datas.withIndex()){
                    for (item3 in item2.list){
                        for (item in selected){
                            if (item3.id==item.id){
                                item3.setChecked(true)
                            }
                        }
                    }
                    expandListView.expandGroup(index, true)

                }
                return
            }
        }

        if (!refreshLayout.isRefreshing)
            refreshLayout.isRefreshing = true
        handler.postDelayed(stopRefreshRunnable, 5000)
        presenter!!.getAllSrd()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_commit,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId==R.id.commit){
            commit()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGetAllSrd(data: List<RoomSrd>) {
        data.forEach {
            it.list = ArrayList<RoomSrd.DeviceBean>()
            if (it.device != null)
                it.list.add(it.device)
            var it1 = it

            for (deviceBean in it1.list) {
                deviceBean.area = it1.name
                selected.forEach {
                    if (deviceBean.id == it.id) {
                        deviceBean.setChecked(true)
                    }
                }

            }

        }

        datas.clear()
        datas.addAll(data)
        adapter!!.notifyDataSetChanged()
        for (index in datas.indices) {
            expandListView.expandGroup(index, true)
        }
        if (refreshLayout != null)
            refreshLayout.isRefreshing = false
        handler.removeCallbacks(stopRefreshRunnable)
    }

    var selected = ArrayList<RoomSrd.DeviceBean>()
    fun initListener() {
        fab.setOnClickListener {
            commit()
//            QMUIDialog.MessageDialogBuilder(context)
//                    .setMessage(String.format(getString(R.string.tips_selector_channel), selected.size.toString()))
//                    .setTitle(getString(R.string.tips))
//                    .addAction(R.string.cancel) { dialog, _ -> dialog.dismiss() }
//                    .addAction(R.string.ok) { dialog, _ ->
//                        dialog.dismiss()
//                        val intent = Intent()
//                        intent.putParcelableArrayListExtra(SrdSelector.INTENT_SRD, selected)
//                        activity.setResult(InteractionAddFragment.srdResultCode, intent)
//                        activity.finish()
//                    }
//                    .create()
//                    .show()


        }
    }

    private fun commit(){
        selected.clear()
        datas.forEach {
            it.list.forEach {
                if (it.isChecked) {
                    selected.add(it)
                }
            }
        }
        if (selected.size == 0) {
            showToast(getString(R.string.tips_no_devices))
            return
        }
        if (selected.size > 1) {
            showToast(getString(R.string.tips_more_srd))
            return
        }
        val intent = Intent()
        intent.putParcelableArrayListExtra(SrdSelector.INTENT_SRD, selected)
        activity.setResult(InteractionAddFragment.srdResultCode, intent)
        activity.finish()
    }

    override fun onDestroyView() {
        presenter!!.destory()
        super.onDestroyView()
    }
}
