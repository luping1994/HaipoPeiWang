package net.suntrans.haipopeiwang.store

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.suntrans.haipopeiwang.bean.*
import net.suntrans.haipopeiwang.room.Room
import net.suntrans.suntranscomponents.c4800.Interaction
import net.suntrans.suntranscomponents.c4800.RoomChannel

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

    /**
     * 获取验证码
     * @param phone
     * @param callBack
     * @return
     */
    fun getPhoneCode(phone: String, callBack: StoreCallBack<RespondBody<*>>): Disposable {
        return translate<RespondBody<*>>(RetrofitHelper.getApi2().getPhoneCode(phone))
                .subscribe({ respondBody -> callBack.onNext(respondBody) }, { throwable -> callBack.onError(throwable) })

    }

    /**
     * 账号密码登录
     */
    fun loginByPassword(phone: String, password: String, callBack: StoreCallBack<RespondBody<LoginData>>): Disposable {
        return translate<RespondBody<LoginData>>(RetrofitHelper.getApi2().loginByPassword(phone, password))
                .subscribe({ respondBody -> callBack.onNext(respondBody) }, { throwable -> callBack.onError(throwable) })

    }


    /**
     * 验证验证码
     * @param phone
     * @param callBack
     * @return
     */
    fun loginByCode(phone: String, code: String, callBack: StoreCallBack<RespondBody<LoginData>>): Disposable {
        return translate<RespondBody<LoginData>>(RetrofitHelper.getApi2().loginByCode(phone, code))
                .subscribe(
                        { respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) }
                )

    }


    /**
     * 绑定微信
     */
    fun bindWxRaw(wxid: String, callBack: StoreCallBack<RespondBody<*>>): Disposable {
        return translate<RespondBody<*>>(RetrofitHelper.getApi2().bindWx(wxid))
                .subscribe(
                        { respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) }
                )

    }


    /**
     * 获取openid并绑定微信
     */
    fun bindWx(appid: String, code: String, grant_type: String, secret: String, callBack: StoreCallBack<RespondBody<*>>): Disposable {
        return translate(RetrofitHelper.getOpenIdApi().getOpenId(appid, code, grant_type, secret)
                .flatMap { v -> RetrofitHelper.getApi2().bindWx(v.openid) })
                .subscribe(
                        { respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) }
                )
    }

    /**
     * 微信登录
     */
    fun loginByWX(wxid: String, callBack: StoreCallBack<RespondBody<LoginData>>): Disposable {
        return translate<RespondBody<LoginData>>(RetrofitHelper.getApi2().loginByWX(wxid))
                .subscribe(
                        { respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) }
                )
    }

    fun getOpenId(appid: String, code: String, grant_type: String, secret: String, callBack: StoreCallBack<WXLoginData>): Disposable {
        return translate<WXLoginData>(RetrofitHelper.getOpenIdApi().getOpenId(appid, code, grant_type, secret))
                .subscribe(
                        { respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) }
                )
    }

    /**
     * 获取OpenId RespondBody<LoginData>
     */
    fun loginByWX(appid: String, code: String, grant_type: String, secret: String, callBack: StoreCallBack<RespondBody<LoginData>>): Disposable {
        return translate(RetrofitHelper.getOpenIdApi().getOpenId(appid, code, grant_type, secret)
                .flatMap { v ->
                    RetrofitHelper.getApi2().loginByWX(v.openid)
                })
                .subscribe(
                        { respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) }
                )

    }

    /**
     * 绑定设备到家庭账号
     * @param devices the devices info,as Json string
     */
    fun bindDevices(devices: String, callBack: Store.StoreCallBack<RespondBody<*>>): Disposable {
        return translate<RespondBody<*>>(RetrofitHelper.getApi2().bindDevice(devices))
                .subscribe(
                        { respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) }
                )

    }

    fun unBindWx(callBack: Store.StoreCallBack<RespondBody<*>>): Disposable {
        return translate<RespondBody<*>>(RetrofitHelper.getApi2().unBindWx())
                .subscribe(
                        { respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) }
                )
    }

    fun getHomeScene(callBack: StoreCallBack<RespondBody<DataWarp<SceneBean>>>): Disposable {
        return translate(RetrofitHelper.getApi2().homeScene)
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
    }


    fun getHomeChannel(callBack: StoreCallBack<RespondBody<DataWarp<DeviceChannelBean>>>): Disposable {
        return translate(RetrofitHelper.getApi2().homeChannels)
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
    }


    fun getRooms(callBack: StoreCallBack<RespondBody<DataWarp<Room>>>): Disposable {
        return translate(RetrofitHelper.getApi2().rooms)
                .subscribe({ respondBody -> callBack.onNext(respondBody) },
                        { throwable -> callBack.onError(throwable) })
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


}
