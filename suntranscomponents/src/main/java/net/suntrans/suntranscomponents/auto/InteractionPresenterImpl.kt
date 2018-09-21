package net.suntrans.suntranscomponents.auto

import io.reactivex.disposables.CompositeDisposable
import net.suntrans.suntranscomponents.bean.DataWarp

import net.suntrans.suntranscomponents.bean.RespondBody
import net.suntrans.suntranscomponents.c4800.Interaction
import net.suntrans.suntranscomponents.c4800.InteractionContract
import net.suntrans.suntranscomponents.store.Store

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
class InteractionPresenterImpl(V: InteractionContract.View) : InteractionContract.Presenter {



    private var view: InteractionContract.View = V
    private var mDisposable = CompositeDisposable()

    init {
        V.setPresenter(this)
    }

    override fun getList() {
        mDisposable.add(Store.getInteraction("2", object : Store.StoreCallBack<RespondBody<DataWarp<Interaction>>> {
            override fun onNext(t: RespondBody<DataWarp<Interaction>>) {
                if (t.isOk) {
                    view.onGetInteractionList(t.data.lists)
                } else {
                    view.onDataError(t.code, t.msg)
                }
            }

            override fun onError(e: Throwable) {
                view.onDataError(500, e.localizedMessage)
            }

        }))
    }

    override fun onItemSwitchButtonClicked(item: Interaction,status:String) {
        view.showAddLoading()
        val map = HashMap<String,String>()
        map["id"] = item.id.toString()
        map["status"] = status

        mDisposable.add(Store.changeStatus(map,object :Store.StoreCallBack<RespondBody<*>>{
            override fun onNext(t: RespondBody<*>) {
                if(t.isOk){
                    view.onChangeStatusSuccess(t.msg)
                    getList()
                }else{
                    view.onDataError(500,t.msg)
                }
                view.hideAddLoading()
            }

            override fun onError(e: Throwable) {
                view.hideAddLoading()
                view.onDataError(500,e.message)
            }

        }))
    }

    override fun deleteItem(item: Interaction) {
        view.showAddLoading()
        val map = HashMap<String,String>()
        map["id"] = item.id.toString()

        mDisposable.add(Store.delLinkage(map,object :Store.StoreCallBack<RespondBody<*>>{
            override fun onNext(t: RespondBody<*>) {
                if(t.isOk){
                    view.onDeleteSuccess(t.msg)
                    getList()
                }else{
                    view.onDataError(500,t.msg)
                }
                view.hideAddLoading()
            }

            override fun onError(e: Throwable) {
                view.hideAddLoading()
                view.onDataError(500,e.message)
            }

        }))
    }

    override fun onItemClick(item: Interaction) {

    }

    override fun onStart() {

    }

    override fun destory() {
        mDisposable.clear()
    }
}
