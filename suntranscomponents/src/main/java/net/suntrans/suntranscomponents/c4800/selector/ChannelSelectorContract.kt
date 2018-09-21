package net.suntrans.suntranscomponents.c4800.selector

import net.suntrans.suntranscomponents.c4800.RoomChannel
import net.suntrans.suntranscomponents.presenter.IBasePresenter
import net.suntrans.suntranscomponents.view.IBaseView


/**
 * Created by Looney on 2018/9/17.
 * Des:
 */
interface ChannelSelectorContract {

    interface View : IBaseView<Presenter> {
        fun onGetAllChannels(data: List<RoomChannel>)
    }

    interface Presenter : IBasePresenter {
        fun getAllChannels()
    }
}
