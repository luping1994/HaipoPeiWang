package net.suntrans.suntranscomponents.c4800;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
public class Interaction implements Parcelable {

    /**
     * id : 22
     * type : 2
     * while : {"conditions":{"youren":1,"wuren":1},"systems":[{"title":"时间","id":"1533867460443","type":"time","startTime":"00:00","endTime":"23:59","repeat":"1,2,3,4,5,6,7","delay":0},{"title":"时间","id":1533867774596,"type":"time","startTime":"18:03","endTime":"18:59","delay":0}],"device":{"id":226,"title":"智能测控器","area":"客厅-T","parentId":2,"device_type":4800,"checked":true,"type":"srd","dev_id":226}}
     * do : [{"id":866,"area_id":181,"channel_id":1838,"status":0,"updated_at":"2018-08-02 14:33:47","title":"客厅顶灯大厦","parentId":181,"checked":true,"type":"channel"},{"id":867,"area_id":181,"channel_id":1841,"status":0,"updated_at":"2018-07-25 09:54:51","title":"卧室空调","parentId":181,"checked":true,"type":"channel"},{"id":868,"area_id":181,"channel_id":1843,"status":0,"updated_at":"2018-07-25 09:55:10","title":"厨房插座","parentId":181,"checked":true,"type":"channel"},{"id":869,"area_id":181,"channel_id":1842,"status":0,"updated_at":null,"title":"卧室顶灯","parentId":181,"checked":true,"type":"channel"},{"id":870,"area_id":181,"channel_id":1844,"status":0,"updated_at":"2018-07-25 09:55:06","title":"卫生间灯","parentId":181,"checked":true,"type":"channel"},{"id":865,"area_id":182,"channel_id":1,"status":0,"updated_at":"2018-08-02 14:31:43","title":"空调客厅空调","parentId":182,"checked":true,"type":"channel"}]
     * status : 0
     * created_at : 2018-08-04 14:25:50
     * updated_at : 2018-08-10 10:23:03
     * deleted_at : null
     * name : 测控器感应联动控制
     * union_id : 1
     * dev_id : 226
     */

    public int id;
    public int type;

    @SerializedName("while")
    public String whileX;

    @SerializedName("do")
    public String doX;

    public int status;
    public String created_at;
    public String updated_at;
    public String deleted_at;
    public String name;
    public int union_id;
    public int dev_id;

    public String subTitle;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.whileX);
        dest.writeString(this.doX);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
        dest.writeString(this.name);
        dest.writeInt(this.union_id);
        dest.writeInt(this.dev_id);
        dest.writeString(this.subTitle);
    }

    public Interaction() {
    }

    protected Interaction(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.whileX = in.readString();
        this.doX = in.readString();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
        this.name = in.readString();
        this.union_id = in.readInt();
        this.dev_id = in.readInt();
        this.subTitle = in.readString();
    }

    public static final Creator<Interaction> CREATOR = new Creator<Interaction>() {
        @Override
        public Interaction createFromParcel(Parcel source) {
            return new Interaction(source);
        }

        @Override
        public Interaction[] newArray(int size) {
            return new Interaction[size];
        }
    };
}
