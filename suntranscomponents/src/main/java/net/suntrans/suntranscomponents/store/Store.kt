package net.suntrans.suntranscomponents.store

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.suntrans.suntranscomponents.bean.DataWarp
import net.suntrans.suntranscomponents.bean.RespondBody
import net.suntrans.suntranscomponents.bean.RoomSrd
import net.suntrans.suntranscomponents.c4800.Interaction
import net.suntrans.suntranscomponents.c4800.RoomChannel
import net.suntrans.suntranscomponents.store.RetrofitHelper

/**
 * Created by Looney on 2018/8/15.
 * Des:数据请求仓库
 */
object Store {

    interface StoreCallBack<T> {
        fun onNext(t: T)

        fun onError(e: Throwable)
    }

    private fun <S> translate(t: Flowable<S>): Flowable<S> {

        return t.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun getInteraction(type: String, callBack: StoreCallBack<RespondBody<DataWarp<Interaction>>>): Disposable {
        return translate(RetrofitHelper.getApi2().getInteraction(type))
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
    }


    fun getAllChannels(callBack: StoreCallBack<RespondBody<DataWarp<RoomChannel>>>): Disposable {
        return translate(RetrofitHelper.getApi2().allChannels)
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
    }


    fun getAllSrd(callBack: StoreCallBack<RespondBody<DataWarp<RoomSrd>>>): Disposable {
        return translate(RetrofitHelper.getApi2().getSensusDevices("2"))
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
    }

    fun addLinkage(map: Map<String, String>, callBack: StoreCallBack<RespondBody<*>>): Disposable {
        return translate(RetrofitHelper.getApi2().addLinkage(map))
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
    }

    fun editLinkage(map: Map<String, String>, callBack: StoreCallBack<RespondBody<*>>): Disposable {
        return translate(RetrofitHelper.getApi2().editLinkage(map))
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
    }


    fun delLinkage(map: Map<String, String>, callBack: StoreCallBack<RespondBody<*>>): Disposable {
        return translate(RetrofitHelper.getApi2().delLinkage(map))
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
    }


    fun changeStatus(map: Map<String, String>, callBack: StoreCallBack<RespondBody<*>>): Disposable {
        return translate(RetrofitHelper.getApi2().changeStatus(map))
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
    }


}
