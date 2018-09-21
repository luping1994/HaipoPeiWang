package net.suntrans.haipopeiwang.room.detail


import net.suntrans.haipopeiwang.presenter.IBasePresenter
import net.suntrans.haipopeiwang.room.Room
import net.suntrans.haipopeiwang.view.IBaseView

/**
 * Created by Looney on 2018/9/17.
 * Des:
 */
interface RoomDetailContract {


    interface Presenter : IBasePresenter {
        fun getRoomChannel(id:String)
    }


    interface View : IBaseView<Presenter> {
        fun onGetRoomChannel(rooms: List<Room>)
    }


}
