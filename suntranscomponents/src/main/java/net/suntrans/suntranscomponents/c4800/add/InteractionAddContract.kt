package net.suntrans.suntranscomponents.c4800.add

import net.suntrans.suntranscomponents.presenter.IBasePresenter
import net.suntrans.suntranscomponents.view.IBaseView


/**
 * Created by Looney on 2018/9/17.
 * Des:测控器感应add契约类
 */
interface InteractionAddContract {

    interface View : IBaseView<Presenter> {
        fun onCommit(code:Int,msg:String)
        fun showAddLoading()
        fun hideAddLoading()
    }

    interface Presenter : IBasePresenter {
      fun addInteraction(name:String,type:String,d:String,wh:String,dev_id:String)
      fun modifyInteraction(id:String,name:String,type:String,d:String,wh:String,dev_id:String)
    }
}
