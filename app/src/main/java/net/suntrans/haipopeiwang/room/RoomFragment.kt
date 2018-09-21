package net.suntrans.haipopeiwang.room

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_room.*
import net.suntrans.haipopeiwang.LazyLoadFragment
import net.suntrans.haipopeiwang.R
import net.suntrans.haipopeiwang.RouterActivity
import net.suntrans.haipopeiwang.room.detail.RoomDetailFragment
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Looney on 2018/9/6.
 * Des:房间通用列表页面
 */
class RoomFragment : LazyLoadFragment(), RoomContract.View {


    private var presenter: RoomContract.Presenter? = null
    private var adapter: RoomAdapter? = null
    private val datas = ArrayList<Room>()
    private var itemHeight: Int = 0
    private var itemWidth: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_room, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        fixStatusBar()
        val toolbar = view!!.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.title_room)
        toolbar.inflateMenu(R.menu.menu_add)
        toolbar.setOnMenuItemClickListener { item: MenuItem? ->
            if (item!!.itemId == R.id.add) {

            }
            true
        }
        itemWidth = (ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(16f))
        itemHeight = itemWidth * 9 / 16

        adapter = RoomAdapter(R.layout.item_room, datas)

        adapter!!.setOnItemClickListener { adapter, view, position ->

            val args = Bundle()
            args.putString("id", datas[position].id.toString())
            args.putString("img_url", datas[position].img_url)
            args.putString("name", datas[position].name)
            RouterActivity.start(activity, RouterActivity.RoomFun.ROOM_DETAIL, args)

        }

        recyclerView.adapter = adapter


        ColorfulSwipeRefreshLayout.setOnRefreshListener {
            getData()
            handler.postDelayed({ ColorfulSwipeRefreshLayout.isRefreshing = false }, 2000)
        }

    }

    override fun lazyLoadDataImpl() {
        getData()
    }

    private fun getData() {
        if (!ColorfulSwipeRefreshLayout.isRefreshing)
            ColorfulSwipeRefreshLayout.isRefreshing = true
        presenter!!.getRoomData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onGetRoomData(rooms: List<Room>) {
        datas.clear()
        datas.addAll(rooms)
        adapter!!.notifyDataSetChanged()
        handler.removeCallbacksAndMessages(null)
        ColorfulSwipeRefreshLayout.isRefreshing = false
    }

    override fun setPresenter(presenter: RoomContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDataError(code: Int, msg: String) {
        showErr(msg)
    }


    //the adapter of scene sceneRecyclerView
    private inner class RoomAdapter(layoutResId: Int, data: List<Room>?) : BaseQuickAdapter<Room, BaseViewHolder>(layoutResId, data) {

        override fun convert(helper: BaseViewHolder, item: Room) {
            helper.setText(R.id.name, item.name)
            val view = helper.getView<ImageView>(R.id.image)
            Glide.with(context)
                    .load(item.img_url)
//                    .override(imgSize, imgSize)
                    .placeholder(R.drawable.room)
                    .centerCrop()
                    .into(view)
        }
    }

    private inner class RoomViewHolder(view: View?) : BaseViewHolder(view) {
        init {
            itemView.layoutParams.height = itemHeight
            itemView.layoutParams.width = itemWidth
        }
    }

    companion object {
        fun newInstance(): RoomFragment {
            val args = Bundle()
            val fragment = RoomFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
