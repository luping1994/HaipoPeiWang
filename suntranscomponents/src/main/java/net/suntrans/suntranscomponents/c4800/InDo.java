package net.suntrans.suntranscomponents.c4800;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Looney on 2018/9/18.
 * Des:感应的结果
 */
public class InDo implements Parcelable {


    /**
     * id : 866
     * area_id : 181
     * channel_id : 1838
     * status : 0
     * updated_at : 2018-08-02 14:33:47
     * title : 客厅顶灯大厦
     * parentId : 181
     * checked : true
     * type : channel
     */
    public int id;
    public int area_id;
    public int channel_id;
    public int status;
    public String updated_at;
    public String title;
    public int parentId;//自定义的父id 没有什么实际意义
    public boolean checked;//是否选中 加感应设置时候 省事没有去掉 没什么意义的字段
    public String type;//类型 可选 channel sensus

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.area_id);
        dest.writeInt(this.channel_id);
        dest.writeInt(this.status);
        dest.writeString(this.updated_at);
        dest.writeString(this.title);
        dest.writeInt(this.parentId);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
    }

    public InDo() {
    }

    protected InDo(Parcel in) {
        this.id = in.readInt();
        this.area_id = in.readInt();
        this.channel_id = in.readInt();
        this.status = in.readInt();
        this.updated_at = in.readString();
        this.title = in.readString();
        this.parentId = in.readInt();
        this.checked = in.readByte() != 0;
        this.type = in.readString();
    }

    public static final Creator<InDo> CREATOR = new Creator<InDo>() {
        @Override
        public InDo createFromParcel(Parcel source) {
            return new InDo(source);
        }

        @Override
        public InDo[] newArray(int size) {
            return new InDo[size];
        }
    };
}
