package net.suntrans.suntranscomponents.c4800.selector

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import kotlinx.android.synthetic.main.fragment_expand.*
import net.suntrans.suntranscomponents.CBaseFragment
import net.suntrans.suntranscomponents.R
import net.suntrans.suntranscomponents.c4800.RoomChannel
import net.suntrans.suntranscomponents.c4800.add.InteractionAddFragment

/**
 * Created by Looney on 2018/9/18
 * Des:
 */
class ChannelSelector : CBaseFragment(), ChannelSelectorContract.View {

    companion object {
        const val INTENT_CHANNEL = "channels"
        val datas = ArrayList<RoomChannel>()

        fun newInstance(): ChannelSelector {
            val args = Bundle()
            val fragment = ChannelSelector()
            fragment.arguments = args
            return fragment
        }
    }


    override fun setPresenter(presenter: ChannelSelectorContract.Presenter?) {
        this.presenter = presenter
    }

    override fun onDataError(code: Int, msg: String?) {
        showErr(msg)
    }

    private var adapter: ChannelAdapter? = null
    private var presenter: ChannelSelectorContract.Presenter? = null
    private val stopRefreshRunnable = Runnable {
        refreshLayout.isRefreshing = false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_expand, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

//        expandListView.setGroupIndicator(null)
        adapter = ChannelAdapter(datas, context)
        expandListView.setAdapter(adapter)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary), resources.getColor(R.color.colorAccent))

        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setTitle(R.string.title_channel_list)
        val extra = activity.intent.getParcelableArrayListExtra<RoomChannel.ChannelsBean>(INTENT_CHANNEL)
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
                for (item2 in datas) {
                    var selectedChSize = 0
                    for (item3 in item2.channels) {
                        for (item in selected) {
                            if (item3.channel_id == item.channel_id) {
                                item3.setChecked(true)
                                selectedChSize++
                            }
                        }
                    }
                    if (selectedChSize == item2.channels.size && selectedChSize != 0) {
                        item2.setChecked(true)
                    }
                }
                adapter!!.notifyDataSetChanged()
                return
            }
        }
        handler.postDelayed(stopRefreshRunnable, 5000)
        if (!refreshLayout.isRefreshing)
            refreshLayout.isRefreshing = true
        presenter!!.getAllChannels()
    }

    override fun onGetAllChannels(data: List<RoomChannel>) {
        for (item2 in data) {
            var selectedChSize = 0
            for (item3 in item2.channels) {
                for (item in selected) {
                    if (item3.channel_id == item.channel_id) {
                        item3.setChecked(true)
                        selectedChSize++
                    }
                }
            }
            if (selectedChSize == item2.channels.size && selectedChSize != 0) {
                item2.setChecked(true)
            }
        }
        datas.clear()
        datas.addAll(data)
        adapter!!.notifyDataSetChanged()
        refreshLayout.isRefreshing = false
        handler.removeCallbacks(stopRefreshRunnable)
    }

    var selected = ArrayList<RoomChannel.ChannelsBean>()
    fun initListener() {
        fab.setOnClickListener {
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_commit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.commit) {
            commit()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun commit() {
        selected.clear()
        datas.forEach {
            it.channels.forEach {
                if (it.isChecked) {
                    selected.add(it)
                }
            }
        }
        if (selected.size == 0) {
            showToast(getString(R.string.tips_no_devices))
        } else {
            QMUIDialog.MessageDialogBuilder(context)
                    .setMessage(String.format(getString(R.string.tips_selector_channel), selected.size.toString()))
                    .setTitle(getString(R.string.tips))
                    .addAction(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                    .addAction(R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent()
                        intent.putParcelableArrayListExtra(INTENT_CHANNEL, selected)
                        activity.setResult(InteractionAddFragment.deviceResultCode, intent)
                        activity.finish()
                    }
                    .create()
                    .show()

        }
    }


    override fun onDestroyView() {
        presenter!!.destory()
        super.onDestroyView()
    }
}
