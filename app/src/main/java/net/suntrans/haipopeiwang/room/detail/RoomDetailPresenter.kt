package net.suntrans.haipopeiwang.room.detail

import io.reactivex.disposables.CompositeDisposable
import net.suntrans.haipopeiwang.bean.DataWarp
import net.suntrans.haipopeiwang.bean.RespondBody
import net.suntrans.haipopeiwang.room.Room
import net.suntrans.haipopeiwang.room.RoomContract
import net.suntrans.haipopeiwang.store.Store

/**
 * Created by Looney on 2018/9/17.
 * Des:
 */
class RoomDetailPresenter(V: RoomDetailContract.View) : RoomDetailContract.Presenter {

    private var mView: RoomDetailContract.View = V
    private var mDisposable = CompositeDisposable()

    init {
        V.setPresenter(this)
    }

    override fun destory() {
        mDisposable.clear()
    }

    override fun onStart() {
        //do nothing
    }

    override fun getRoomChannel(id:String) {
        mDisposable.add(Store.getRooms(object : Store.StoreCallBack<RespondBody<DataWarp<Room>>> {
            override fun onNext(t: RespondBody<DataWarp<Room>>) {
                if (t.isOk) {
                    mView.onGetRoomChannel(t.data.lists)
                } else {
                    mView.onDataError(t.code, t.msg)
                }
            }

            override fun onError(e: Throwable) {
                mView.onDataError(500, e.localizedMessage)

            }

        }))
    }

}
