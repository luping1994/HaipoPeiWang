package net.suntrans.suntranscomponents.store;


import net.suntrans.suntranscomponents.bean.DataWarp;
import net.suntrans.suntranscomponents.bean.RespondBody;
import net.suntrans.suntranscomponents.bean.RoomSrd;
import net.suntrans.suntranscomponents.c4800.Interaction;
import net.suntrans.suntranscomponents.c4800.RoomChannel;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Looney on 2018/8/15.
 * Des:
 */
public interface Api {


    //*****************感应设置 start*********************//
    /**
     * 获取感应列表
     * @param type 1为第六感联动 2为测控器感应策略
     * @return
     */
    @POST("automation/linkageList")
    @FormUrlEncoded
    Flowable<RespondBody<DataWarp<Interaction>>> getInteraction(@Field("type") String type);

    /**
     * 所有通道  按照房间进行分类
     * @return
     */
    @POST("scene/getChannels")
    Flowable<RespondBody<DataWarp<RoomChannel>>> getAllChannels();

    /**
     * 获取感应设备
     * @param type 1为第六感 2为测控器
     * @return
     */
    @POST("automation/sensusDevices")
    @FormUrlEncoded
    Flowable<RespondBody<DataWarp<RoomSrd>>> getSensusDevices(@Field("type") String type);

    /**
     * 添加感应设置
     * @param
     * @return
     */
    @POST("automation/addLinkage")
    @FormUrlEncoded
    Flowable<RespondBody> addLinkage(@FieldMap Map<String,String> map);

    /**
     * 修改感应设置
     * @param
     * @return
     */
    @POST("automation/editLinkage")
    @FormUrlEncoded
    Flowable<RespondBody> editLinkage(@FieldMap Map<String,String> map);

    @POST("automation/delLinkage")
    @FormUrlEncoded
    Flowable<RespondBody> delLinkage(@FieldMap Map<String,String> map);

    @POST("automation/changeStatus")
    @FormUrlEncoded
    Flowable<RespondBody> changeStatus(@FieldMap Map<String,String> map);
}
