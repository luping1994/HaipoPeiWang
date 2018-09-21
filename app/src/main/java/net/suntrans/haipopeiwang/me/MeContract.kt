package net.suntrans.haipopeiwang.me


import net.suntrans.haipopeiwang.presenter.IBasePresenter
import net.suntrans.haipopeiwang.room.Room
import net.suntrans.haipopeiwang.view.IBaseView

/**
 * Created by Looney on 2018/9/17.
 * Des:
 */
interface MeContract {


    interface Presenter : IBasePresenter {
        fun getUserInfo()
    }


    interface View : IBaseView<Presenter> {

    }


}
