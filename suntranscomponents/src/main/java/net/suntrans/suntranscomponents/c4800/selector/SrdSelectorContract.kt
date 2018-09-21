package net.suntrans.suntranscomponents.c4800.selector

import net.suntrans.suntranscomponents.bean.RoomSrd
import net.suntrans.suntranscomponents.presenter.IBasePresenter
import net.suntrans.suntranscomponents.view.IBaseView


/**
 * Created by Looney on 2018/9/17.
 * Des:
 */
interface SrdSelectorContract {

    interface View : IBaseView<Presenter> {
        fun onGetAllSrd(data: List<RoomSrd>)
    }

    interface Presenter : IBasePresenter {
        fun getAllSrd()
    }
}
