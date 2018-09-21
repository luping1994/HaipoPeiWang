package net.suntrans.haipopeiwang.me

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
class MePresenter(V: RoomContract.View) : RoomContract.Presenter {

    private var mView: RoomContract.View = V
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

    override fun getRoomData() {
        mDisposable.add(Store.getRooms(object : Store.StoreCallBack<RespondBody<DataWarp<Room>>> {
            override fun onNext(t: RespondBody<DataWarp<Room>>) {
                if (t.isOk) {
                    mView.onGetRoomData(t.data.lists)
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
