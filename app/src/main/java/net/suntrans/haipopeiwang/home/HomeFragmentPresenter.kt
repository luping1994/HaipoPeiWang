package net.suntrans.haipopeiwang.home

import io.reactivex.disposables.CompositeDisposable
import net.suntrans.haipopeiwang.R
import net.suntrans.haipopeiwang.bean.DataWarp
import net.suntrans.haipopeiwang.bean.DeviceChannelBean
import net.suntrans.haipopeiwang.bean.RespondBody
import net.suntrans.haipopeiwang.bean.SceneBean
import net.suntrans.haipopeiwang.store.Store
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by Looney on 2018/9/7.
 * Des:
 */
class HomeFragmentPresenter(V: HomeContract.View) : HomeContract.HomePresenter {
    override fun getGridData() {
        val data = ArrayList<Map<String,Any>>()
        var map = HashMap<String,Any>()
        map["image"] = R.drawable.kongtiao
        map["name"] = "空调"
        data.add(map)

        map = HashMap<String,Any>()
        map["image"] = R.drawable.dianshi
        map["name"] = "电视"
        data.add(map)

        map = HashMap<String,Any>()
        map["image"] = R.drawable.tongdian
        map["name"] = "通电回路"
        data.add(map)

        map = HashMap<String,Any>()
        map["image"] = R.drawable.ele_charge
        map["name"] = "缴费"
        data.add(map)

        map = HashMap<String,Any>()
        map["image"] = R.drawable.ganying
        map["name"] = "感应设置"
        data.add(map)

        view.onGetGridData(data)
    }

    private var view: HomeContract.View = V
    private var mDisposable = CompositeDisposable()

    init {
        V.setPresenter(this)
    }

    override fun destory() {
        mDisposable.clear()
    }

    override fun onStart() {
        //do nothing
    }

    /**
     * Banner数据
     */
    override fun getBanner() {
        val images = ArrayList<String>()
        val titles = ArrayList<String>()
        images.add("https://wxapp.suntrans.net/h5/appImage/header1.jpg")
        images.add("https://wxapp.suntrans.net/h5/appImage/header2.jpg")
        images.add("https://wxapp.suntrans.net/h5/appImage/header3.jpg")
        titles.add("Banner1")
        titles.add("Banner2")
        titles.add("Banner3")
        val map = HashMap<String, List<String>>()
        map["images"] = images
        map["titles"] = titles

        view.onGetBannerData(map)

    }


    /**
     * 获取场景
     */
    override fun getSceneData() {
        mDisposable.add(Store.getHomeScene(object : Store.StoreCallBack<RespondBody<DataWarp<SceneBean>>> {
            override fun onNext(respondBody: RespondBody<DataWarp<SceneBean>>) {
                if (respondBody.code == 200) {
                    view.onGetSceneData(respondBody.data.lists)
                } else {
                    view.onDataError(respondBody.code, respondBody.msg)
                }
            }

            override fun onError(e: Throwable) {
                view.onDataError(500, e.localizedMessage)

            }
        }))


    }

    /**
     * 获取通道
     */
    override fun getChannelData() {
        mDisposable.add(Store.getHomeChannel(object : Store.StoreCallBack<RespondBody<DataWarp<DeviceChannelBean>>> {
            override fun onNext(respondBody: RespondBody<DataWarp<DeviceChannelBean>>) {
                if (respondBody.code == 200) {
                    view.onGetChannelData(respondBody.data.lists)
                } else {
                    view.onDataError(respondBody.code, respondBody.msg)
                }
            }

            override fun onError(e: Throwable) {
                view.onDataError(500, e.localizedMessage)

            }
        }))
    }
}
