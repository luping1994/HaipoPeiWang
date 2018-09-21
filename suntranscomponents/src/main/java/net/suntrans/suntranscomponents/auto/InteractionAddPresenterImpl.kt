package net.suntrans.suntranscomponents.auto

import io.reactivex.disposables.CompositeDisposable
import net.suntrans.suntranscomponents.bean.RespondBody
import net.suntrans.suntranscomponents.c4800.add.InteractionAddContract
import net.suntrans.suntranscomponents.store.Store

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
class InteractionAddPresenterImpl(V: InteractionAddContract.View) : InteractionAddContract.Presenter {
    override fun modifyInteraction(id: String, name: String, type: String, d: String, wh: String, dev_id: String) {
        view.showAddLoading()
        val map = HashMap<String,String>()
        map["id"] = id
        map["name"] = name
        map["type"] = type
        map["do"] = d
        map["while"] = wh
        map["dev_id"] = dev_id
        mDisposable.add(Store.editLinkage(map,object :Store.StoreCallBack<RespondBody<*>>{
            override fun onNext(t: RespondBody<*>) {
                if(t.isOk){
                    view.onCommit(t.code,t.msg)
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


    override fun addInteraction(name:String,type:String,d:String,wh:String,dev_id:String) {
        view.showAddLoading()
        val map = HashMap<String,String>()
        map["name"] = name
        map["type"] = type
        map["do"] = d
        map["while"] = wh
        map["dev_id"] = dev_id
        mDisposable.add(Store.addLinkage(map,object :Store.StoreCallBack<RespondBody<*>>{
            override fun onNext(t: RespondBody<*>) {
                if(t.isOk){
                    view.onCommit(t.code,t.msg)
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


    private var view: InteractionAddContract.View = V
    private var mDisposable = CompositeDisposable()

    init {
        V.setPresenter(this)
    }


    override fun onStart() {

    }

    override fun destory() {
        mDisposable.clear()
    }
}
