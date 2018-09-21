package net.suntrans.haipopeiwang.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import de.hdodenhof.circleimageview.CircleImageView
import net.suntrans.haipopeiwang.BaseFragment
import net.suntrans.haipopeiwang.R
import net.suntrans.haipopeiwang.banner.GlideImageLoader
import net.suntrans.haipopeiwang.bean.DeviceChannelBean
import net.suntrans.haipopeiwang.bean.SceneBean
import net.suntrans.haipopeiwang.utils.UiUtils
import net.suntrans.suntranscomponents.auto.InteractionActivity
import java.util.*

/**
 * Created by Looney on 2018/9/6.
 * Des:
 */
class HomeFragment : BaseFragment(), HomeContract.View {


    private var banner: Banner? = null

    private var presenter: HomeContract.HomePresenter? = null

    private var sceneAdater: SceneAdater? = null
    private var channelAdater: ChannelAdater? = null
    private var gridAdapter: SimpleAdapter? = null

    private val scenes = ArrayList<SceneBean>()
    private val channels = ArrayList<DeviceChannelBean>()
    private val gridData = ArrayList<Map<String, Any>>()

    private var sceneRecyclerView: RecyclerView? = null
    private var channelRecyclerView: RecyclerView? = null
    private var gridView: GridView? = null


    private var refreshLayout: SwipeRefreshLayout? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(R.layout.fragment_home, null, false)
    }

    override fun onViewCreated(view: android.view.View?, savedInstanceState: Bundle?) {
        init(view!!)
    }

    private fun init(view: android.view.View) {
        fixStatusBar()
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "三川智家"

        refreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        refreshLayout!!.setColorSchemeColors(context.resources.getColor(R.color.colorPrimary), context.resources.getColor(R.color.colorAccent))
        refreshLayout!!.setOnRefreshListener { handler.postDelayed({ refreshLayout!!.isRefreshing = false }, 2000) }
        gridView = view.findViewById<GridView>(R.id.gridView)
        //新建适配器
        val from = arrayOf("image", "name")
        val to = intArrayOf(R.id.image, R.id.name)
        gridAdapter = SimpleAdapter(context, gridData, R.layout.item_fun, from, to)
        gridView!!.adapter = gridAdapter
        gridView!!.setOnItemClickListener { _, _, position, id ->
            val name = gridData[position]["name"] as String
            if (name=="感应设置"){
                InteractionActivity.start(context)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridView!!.isNestedScrollingEnabled = false
        }
        //        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

//        val barLayout = view.findViewById<AppBarLayout>(R.id.appBarLayout)

//        barLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset -> }
        sceneRecyclerView = view.findViewById(R.id.sceneRecyclerView)
        channelRecyclerView = view.findViewById(R.id.channelRecyclerView)

        //防止ScrollView滑动不流畅
        sceneRecyclerView!!.isNestedScrollingEnabled = false
        channelRecyclerView!!.isNestedScrollingEnabled = false

        banner = view.findViewById(R.id.banner)

        sceneAdater = SceneAdater(R.layout.item_scene, scenes)
        channelAdater = ChannelAdater(R.layout.item_channel, channels)

        sceneRecyclerView!!.adapter = sceneAdater
        channelRecyclerView!!.adapter = channelAdater

        sceneAdater!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val msg = String.format(getString(R.string.tips_excute_scene), scenes[position].name)
            QMUIDialog.MessageDialogBuilder(context)
                    .setMessage(msg)
                    .setTitle(getString(R.string.tips))
                    .addAction(R.string.cancel) { dialog, index -> dialog.dismiss() }
                    .addAction(R.string.ok) { dialog, index -> dialog.dismiss() }
                    .create().show()
        }

        channelAdater!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> }

        presenter!!.getBanner()
        presenter!!.getSceneData()
        presenter!!.getChannelData()
        presenter!!.getGridData()
    }

    private fun initBanner(data: Map<String, List<String>>) {
        val width = ScreenUtils.getScreenWidth()
        val height = ConvertUtils.dp2px(172f)
        //设置banner样式
        banner!!.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        banner!!.setImageLoader(GlideImageLoader(width, height))
        //设置图片集合
        banner!!.setImages(data["images"])
        //设置banner动画效果
        banner!!.setBannerAnimation(Transformer.BackgroundToForeground)
        //设置标题集合（当banner样式有显示title时）
        banner!!.setBannerTitles(data["titles"])
        //设置自动轮播，默认为true
        banner!!.isAutoPlay(true)
        //设置轮播时间
        banner!!.setDelayTime(6000)
        //设置指示器位置（当banner模式中有指示器时）
        banner!!.setIndicatorGravity(BannerConfig.RIGHT)
        //banner设置方法全部调用完毕时最后调用
        banner!!.start()
    }


    override fun setPresenter(presenter: HomeContract.HomePresenter) {
        this.presenter = presenter

    }

    //the adapter of scene sceneRecyclerView
    private inner class SceneAdater(layoutResId: Int, data: List<SceneBean>?) : BaseQuickAdapter<SceneBean, BaseViewHolder>(layoutResId, data) {
        private val imgSize = ConvertUtils.dp2px(36f)

        override fun convert(helper: BaseViewHolder, item: SceneBean) {
            helper.setText(R.id.name, item.name)
            val view = helper.getView<CircleImageView>(R.id.image)
            val tx = helper.getView<TextView>(R.id.name)
            if (item.used == 1) {
                view.borderWidth = ConvertUtils.dp2px(2f)
                view.borderColor = context.resources.getColor(R.color.colorUsed)
                tx.setTextColor(context.resources.getColor(R.color.colorUsed))
            } else {
                view.borderWidth = ConvertUtils.dp2px(0f)
                view.borderColor = Color.GRAY
                tx.setTextColor(Color.GRAY)
            }
            Glide.with(context)
                    .load(item.img)
                    .override(imgSize, imgSize)
                    .placeholder(R.drawable.scene)
                    .into(view)
        }
    }

    //the adapter of Channel sceneRecyclerView
    private inner class ChannelAdater(layoutResId: Int, data: List<DeviceChannelBean>?) : BaseQuickAdapter<DeviceChannelBean, BaseViewHolder>(layoutResId, data) {
        private val imgSize = ConvertUtils.dp2px(64f)

        override fun convert(helper: BaseViewHolder, item: DeviceChannelBean) {
            helper.setText(R.id.name, item.title)
            val view = helper.getView<ImageView>(R.id.image)
            UiUtils.setImageViewColor(view, if (item.status == 1) R.color.channel_on else R.color.channel_off)
        }
    }

    override fun onResume() {
        super.onResume()
        banner!!.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        banner!!.stopAutoPlay()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (presenter != null) {
            presenter!!.destory()
        }
    }


    override fun onGetSceneData(datas: List<SceneBean>) {
        this.scenes.clear()
        this.scenes.addAll(datas)
        sceneAdater!!.notifyDataSetChanged()
    }

    override fun onGetChannelData(data: List<DeviceChannelBean>) {
        this.channels.clear()
        this.channels.addAll(data)
        channelAdater!!.notifyDataSetChanged()
    }

    override fun onGetBannerData(data: Map<String, List<String>>) {
        initBanner(data)
    }

    override fun onGetGridData(data: List<Map<String, Any>>) {
        gridData.clear()
        gridData.addAll(data)
        gridAdapter!!.notifyDataSetChanged()

    }

    override fun onDataError(code: Int, msg: String) {
        showToast(msg)
    }

    companion object {

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
