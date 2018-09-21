package net.suntrans.haipopeiwang.room.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_room.*
import kotlinx.android.synthetic.main.fragment_room_detail.*
import net.suntrans.haipopeiwang.LazyLoadFragment
import net.suntrans.haipopeiwang.R
import net.suntrans.haipopeiwang.room.Room
import java.util.*

/**
 * Created by Looney on 2018/9/6.
 * Des:房间通用列表页面
 */
class RoomDetailFragment : LazyLoadFragment(), RoomDetailContract.View {


    private var presenter: RoomDetailContract.Presenter? = null
    private var adapter: RoomAdapter? = null
    private val datas = ArrayList<Room>()
    private var itemHeight: Int = 0
    private var itemWidth: Int = 0
    private var id = "0"
    private var imgurl = "0"
    private var name = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments.getString("id")
        imgurl = arguments.getString("img_url")
        name = arguments.getString("name")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_room_detail, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val toolbar = view!!.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.title_room)
        toolbar.setOnMenuItemClickListener { item: MenuItem? ->
            if (item!!.itemId == R.id.add) {

            }
            true
        }
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        itemWidth = (ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(16f))
        itemHeight = itemWidth * 9 / 16

        adapter = RoomAdapter(R.layout.item_room, datas)

        adapter!!.setOnItemClickListener { adapter, view, position ->

        }
        collapsingToolbarLayout.title = name
        val imageView = view.findViewById<ImageView>(R.id.banner)
        imageView.viewTreeObserver.addOnGlobalLayoutListener {
            val height = imageView.measuredHeight
            val widht = imageView.measuredWidth
            if (TextUtils.isEmpty(imgurl)) {
                imgurl = defaultImgUrl
            }
            Glide.with(context)
                    .load(imgurl)
                    .override(widht, height)
                    .into(imageView)
            imageView.viewTreeObserver.removeOnGlobalLayoutListener { this }
        }


        recyclerViewDetail.adapter = adapter

//
//        ColorfulSwipeRefreshLayout.setOnRefreshListener {
//            getData()
//            handler.postDelayed({ ColorfulSwipeRefreshLayout.isRefreshing = false }, 2000)
//        }

    }


    override fun lazyLoadDataImpl() {
        getData()
    }

    private fun getData() {
//        if (!ColorfulSwipeRefreshLayout.isRefreshing)
//            ColorfulSwipeRefreshLayout.isRefreshing = true
//        presenter!!.getRoomChannel(id)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }


    override fun onGetRoomChannel(rooms: List<Room>) {
        datas.clear()
        datas.addAll(rooms)
        adapter!!.notifyDataSetChanged()
        handler.removeCallbacksAndMessages(null)
        ColorfulSwipeRefreshLayout.isRefreshing = false
    }


    override fun setPresenter(presenter: RoomDetailContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDataError(code: Int, msg: String) {
        showErr(msg)
    }


    //the adapter of scene sceneRecyclerView
    private inner class RoomAdapter(layoutResId: Int, data: List<Room>?) : BaseQuickAdapter<Room, BaseViewHolder>(layoutResId, data) {

        override fun convert(helper: BaseViewHolder, item: Room) {
            helper.setText(R.id.name, item.name)
            println(item.name)
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
        val defaultImgUrl = "https://hpjd.suntrans-cloud.com//uploads/images/aOiua5L62tqo6GwiuGcSa88dpxIuJEKqfs8ZHzEP.jpeg"
        fun newInstance(): RoomDetailFragment {
            val args = Bundle()
            val fragment = RoomDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
