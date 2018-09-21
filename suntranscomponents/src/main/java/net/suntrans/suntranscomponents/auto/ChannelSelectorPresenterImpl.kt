package net.suntrans.suntranscomponents.auto

import io.reactivex.disposables.CompositeDisposable
import net.suntrans.suntranscomponents.bean.DataWarp
import net.suntrans.suntranscomponents.bean.RespondBody
import net.suntrans.suntranscomponents.c4800.*
import net.suntrans.suntranscomponents.c4800.selector.ChannelSelectorContract
import net.suntrans.suntranscomponents.store.Store

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
class ChannelSelectorPresenterImpl(V: ChannelSelectorContract.View) : ChannelSelectorContract.Presenter {

    private var view: ChannelSelectorContract.View = V
    private var mDisposable = CompositeDisposable()

    init {
        V.setPresenter(this)
    }


    override fun onStart() {

    }



    override fun getAllChannels() {
        mDisposable.add(Store.getAllChannels( object : Store.StoreCallBack<RespondBody<DataWarp<RoomChannel>>> {
            override fun onNext(t: RespondBody<DataWarp<RoomChannel>>) {
                if (t.isOk) {
                    view.onGetAllChannels(t.data.lists)
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
