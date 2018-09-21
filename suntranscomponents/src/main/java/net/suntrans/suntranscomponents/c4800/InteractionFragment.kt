package net.suntrans.suntranscomponents.c4800

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.fragment_interaction.*
import net.suntrans.looney.widgets.SwitchButton
import net.suntrans.suntranscomponents.CBaseFragment
import net.suntrans.suntranscomponents.R
import net.suntrans.suntranscomponents.auto.InteractionAddActivity
import net.suntrans.suntranscomponents.c4800.add.InteractionAddFragment
import net.suntrans.suntranscomponents.utils.DefaultDecoration


/**
 * Created by Looney on 2018/9/17.
 * Des:测控器感应列表
 */
open class InteractionFragment : CBaseFragment(), InteractionContract.View {


    companion object {

        fun newInstance(): InteractionFragment {
            val args = Bundle()
            val fragment = InteractionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var presenter: InteractionContract.Presenter? = null
    private var interactionDatas = ArrayList<Interaction>()
    private var adapter: InteractionAdater? = null

    var items :Array<String>?=null

    private var stopRunnable = Runnable {
        refreshLayout!!.isRefreshing = false
    }//请求超时时主动把下拉刷新隐藏

    override fun setPresenter(presenter: InteractionContract.Presenter?) {
        this.presenter = presenter
    }

    override fun onDataError(code: Int, msg: String?) {
        if (msg != null) {
            showErr(msg)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_interaction, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        items= arrayOf(getString(R.string.item_edit), getString(R.string.item_delete))
        adapter = InteractionAdater(R.layout.item_interaction, interactionDatas)
//        adapter!!.setOnItemChildClickListener { _, view, position ->
//            if (position != -1) {
//                val bt = view as SwitchButton
//                var status = if (bt.isChecked){
//                    "1"
//                } else{
//                    "0"
//                }
//                presenter!!.onItemSwitchButtonClicked(interactionDatas[position],status)
//            }
//        }
        adapter!!.bindToRecyclerView(recyclerView)
        adapter!!.setEmptyView(R.layout.recycler_view_empty)
        adapter!!.setOnItemClickListener { _, _, position ->
            if (position != -1) {

                QMUIDialog.MenuDialogBuilder(activity)
                        .addItems(items) { dialog, which ->
                            if (which==0){
                                val starter = Intent(context, InteractionAddActivity::class.java)
                                starter.putExtra("type",InteractionAddFragment.FRAGMENT_EDIT)
                                starter.putExtra("data",interactionDatas[position])
                                context.startActivity(starter)
                            }else{
                                presenter!!.deleteItem(interactionDatas[position])
                            }
                            dialog.dismiss()
                        }
                        .show()
            }

        }
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DefaultDecoration(context))
        refreshLayout.setOnRefreshListener {
            getData()
        }
        recyclerView.addItemDecoration(DefaultDecoration(context))


    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }


    private fun getData() {
        refreshLayout.isRefreshing = true
        handler.postDelayed(stopRunnable, 5000)
        presenter!!.getList()
    }

    override fun onGetInteractionList(d: List<Interaction>) {
        refreshLayout.isRefreshing = false
        d.forEach { v ->
            //            val doX = JSON.parseArray(v.doX,InDo::class.java)
            val whileX = JSON.parseObject(v.whileX, InIf::class.java)
            var subTitle = ""
            whileX.systems.forEach { v2 ->
                subTitle += v2.startTime + "~" + v2.endTime + ","
            }
            v.subTitle = subTitle
        }
        interactionDatas.clear()
        interactionDatas.addAll(d)
        adapter!!.notifyDataSetChanged()
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

    override fun onDeleteSuccess(msg: String?) {
        showToast(msg)
    }

    override fun onChangeStatusSuccess(msg: String?) {
        showToast(msg)

    }



    //the adapter of Channel sceneRecyclerView
    private inner class InteractionAdater(layoutResId: Int, data: List<Interaction>?) : BaseQuickAdapter<Interaction, BaseViewHolder>(layoutResId, data) {
        override fun convert(helper: BaseViewHolder, item: Interaction) {
            helper.setText(R.id.name, item.name)
                    .setText(R.id.time, String.format(context.getString(R.string.interaction_time), item.subTitle))

            val switchButton = helper.getView<SwitchButton>(R.id.switchButton)
            switchButton.setCheckedImmediately(item.status==1)
            switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
                val status = if (switchButton.isChecked){
                    "1"
                } else{
                    "0"
                }
                presenter!!.onItemSwitchButtonClicked(interactionDatas[helper.adapterPosition],status)
            }

        }
    }



    override fun onDestroy() {
        super.onDestroy()
        presenter!!.destory()
    }
}
