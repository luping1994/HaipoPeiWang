package net.suntrans.haipopeiwang.store;

import net.suntrans.haipopeiwang.bean.DataWarp;
import net.suntrans.haipopeiwang.bean.DeviceChannelBean;
import net.suntrans.haipopeiwang.bean.LoginData;
import net.suntrans.haipopeiwang.bean.ProConfig;
import net.suntrans.haipopeiwang.bean.RespondBody;
import net.suntrans.haipopeiwang.bean.SceneBean;
import net.suntrans.haipopeiwang.bean.WXLoginData;
import net.suntrans.haipopeiwang.room.Room;
import net.suntrans.suntranscomponents.c4800.Interaction;
import net.suntrans.suntranscomponents.c4800.RoomChannel;


import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Looney on 2018/8/15.
 * Des:
 */
public interface Api {
    @POST("Conf/index")
    Flowable<ProConfig> getProConfig();
    ///api/phoneCode

    @POST("api/phoneCode")
    @FormUrlEncoded
    Flowable<RespondBody> getPhoneCode(@Field("phone") String phone);

    @POST("api/loginByPhone/index")
    @FormUrlEncoded
    Flowable<RespondBody<LoginData>> loginByCode(@Field("phone") String phone, @Field("code") String code);

    @POST("api/v1/login/token")
    @FormUrlEncoded
    Flowable<RespondBody<LoginData>> loginByPassword(@Field("username") String username, @Field("password") String password);

    @POST("api/v1/loginByWeixin")
    @FormUrlEncoded
    Flowable<RespondBody<LoginData>> loginByWX(@Field("wx_id") String wx_id);


    @POST("api/jingdong/peiwang/peiwang/userBindDevice")
    @FormUrlEncoded
    Flowable<RespondBody> bindDevice(@Field("devices") String devices);


    @POST("api/jingdong/peiwang/weixin/bindWeixin")
    @FormUrlEncoded
    Flowable<RespondBody> bindWx(@Field("wx_id") String wx_id);


    @POST("api/jingdong/peiwang/weixin/unbindWeixin")
    Flowable<RespondBody> unBindWx();

    @GET("sns/oauth2/access_token")
    Flowable<WXLoginData> getOpenId(@Query("appid") String appid,
                                    @Query("code") String code,
                                    @Query("grant_type") String grant_type,
                                    @Query("secret") String secret);


    /**
     * 获取首页场景
     *
     * @return
     */
    @POST("api/jingdong/weixin/scene/sceneList")
    Flowable<RespondBody<DataWarp<SceneBean>>> getHomeScene();


    /**
     * 获取首页通道
     *
     * @return
     */
    @POST("api/jingdong/weixin/device/channels")
    Flowable<RespondBody<DataWarp<DeviceChannelBean>>> getHomeChannels();


    /**
     * 获取房间列表
     *
     * @return
     */
    @POST("api/jingdong/weixin/area/houseList")
    Flowable<RespondBody<DataWarp<Room>>> getRooms();


    //*****************感应设置 start*********************//
    /**
     * 获取感应列表
     * @param type 1为第六感联动 2为测控器感应策略
     * @return
     */
    @POST("api/jingdong/weixin/automation/linkageList")
    @FormUrlEncoded
    Flowable<RespondBody<DataWarp<Interaction>>> getInteraction(@Field("type") String type);

    /**
     * 所有通道  按照房间进行分类
     * @return
     */
    @POST("api/jingdong/weixin/scene/getChannels")
    Flowable<RespondBody<DataWarp<RoomChannel>>> getAllChannels();
}
