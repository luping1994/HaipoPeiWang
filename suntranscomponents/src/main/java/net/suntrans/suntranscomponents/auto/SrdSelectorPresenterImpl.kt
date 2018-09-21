package net.suntrans.suntranscomponents.auto

import io.reactivex.disposables.CompositeDisposable
import net.suntrans.suntranscomponents.bean.DataWarp
import net.suntrans.suntranscomponents.bean.RespondBody
import net.suntrans.suntranscomponents.bean.RoomSrd
import net.suntrans.suntranscomponents.c4800.selector.SrdSelectorContract
import net.suntrans.suntranscomponents.store.Store

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
class SrdSelectorPresenterImpl(V: SrdSelectorContract.View) : SrdSelectorContract.Presenter {

    private var view: SrdSelectorContract.View = V
    private var mDisposable = CompositeDisposable()

    init {
        V.setPresenter(this)
    }


    override fun onStart() {

    }


    override fun getAllSrd()  {
        mDisposable.add(Store.getAllSrd( object : Store.StoreCallBack<RespondBody<DataWarp<RoomSrd>>> {
            override fun onNext(t: RespondBody<DataWarp<RoomSrd>>) {
                if (t.isOk) {
                    view.onGetAllSrd(t.data.lists)
                } else {
                    view.onDataError(t.code, t.msg)
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view.onDataError(500, e.localizedMessage)
            }

        }))
    }


    override fun destory() {
        mDisposable.clear()
    }
}
