package net.suntrans.haipopeiwang.room


import net.suntrans.haipopeiwang.presenter.IBasePresenter
import net.suntrans.haipopeiwang.view.IBaseView

/**
 * Created by Looney on 2018/9/17.
 * Des:
 */
interface RoomContract {


    interface Presenter : IBasePresenter {
        fun getRoomData()
    }


    interface View : IBaseView<Presenter> {
        fun onGetRoomData(rooms: List<Room>)
    }


}
