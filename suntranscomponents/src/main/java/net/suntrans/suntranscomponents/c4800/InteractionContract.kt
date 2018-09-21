package net.suntrans.suntranscomponents.c4800

import net.suntrans.suntranscomponents.presenter.IBasePresenter
import net.suntrans.suntranscomponents.view.IBaseView


/**
 * Created by Looney on 2018/9/17.
 * Des:测控器感应列表契约类
 */
interface InteractionContract {

    interface View : IBaseView<Presenter> {
        fun onGetInteractionList(data:List<Interaction>)
        fun showAddLoading()
        fun hideAddLoading()
        fun onDeleteSuccess(msg:String?)
        fun onChangeStatusSuccess(msg:String?)
    }

    interface Presenter : IBasePresenter {
        fun getList()
        fun onItemSwitchButtonClicked(item:Interaction,status:String)
        fun deleteItem(item:Interaction)
        fun onItemClick(item:Interaction)
    }
}
