package net.suntrans.haipopeiwang.home

import net.suntrans.haipopeiwang.bean.DeviceChannelBean
import net.suntrans.haipopeiwang.bean.SceneBean
import net.suntrans.haipopeiwang.presenter.IBasePresenter
import net.suntrans.haipopeiwang.view.IBaseView

/**
 * Created by Looney on 2018/9/17.
 * Des:
 */
interface HomeContract {

    interface View : IBaseView<HomePresenter> {

        fun onGetSceneData(data: List<SceneBean>)

        fun onGetChannelData(data: List<DeviceChannelBean>)

        fun onGetBannerData(data: Map<String, List<String>>)

        fun onGetGridData(data:List<Map<String,Any>>)
    }

    interface HomePresenter : IBasePresenter {
        fun getBanner()
        fun getSceneData()
        fun getChannelData()
        fun getGridData();
    }
}
