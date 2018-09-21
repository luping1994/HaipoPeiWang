package net.suntrans.suntranscomponents.c4800;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
public class RoomChannel {

    /**
     * union_id : 1
     * id : 181
     * name : 客厅-T
     * channels : [{"id":866,"area_id":181,"channel_id":1838,"status":0,"updated_at":"2018-09-04 09:07:11","title":"客厅顶灯大厦"},{"id":867,"area_id":181,"channel_id":1841,"status":0,"updated_at":"2018-07-25 09:54:51","title":"卧室空调"},{"id":868,"area_id":181,"channel_id":1843,"status":0,"updated_at":"2018-07-25 09:55:10","title":"厨房插座"},{"id":869,"area_id":181,"channel_id":1842,"status":0,"updated_at":"2018-08-10 13:45:25","title":"卧室顶灯"},{"id":870,"area_id":181,"channel_id":1844,"status":0,"updated_at":"2018-07-25 09:55:06","title":"卫生间灯"},{"id":871,"area_id":181,"channel_id":1856,"status":0,"updated_at":"2018-07-27 11:55:21","title":"2通道"},{"id":883,"area_id":181,"channel_id":1855,"status":0,"updated_at":"2018-08-21 15:04:19","title":"1通道"},{"id":894,"area_id":181,"channel_id":1845,"status":0,"updated_at":"2018-09-04 09:06:50","title":"1通道"},{"id":904,"area_id":181,"channel_id":1858,"status":0,"updated_at":"2018-08-21 15:16:27","title":"4通道"}]
     */

    public int union_id;
    public int id;
    public String name;
    public List<ChannelsBean> channels;

    public boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static class ChannelsBean implements Parcelable {
        /**
         * id : 866
         * area_id : 181
         * channel_id : 1838
         * status : 0
         * updated_at : 2018-09-04 09:07:11
         * title : 客厅顶灯大厦
         */
        public int id;
        public int area_id;
        public int channel_id;
        public int status;
        public String updated_at;
        public String title;

        public int permission = 1;//通道权限 后台系统未实现 这里暂时都赋值为1 表示都有权限
        public boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

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
            dest.writeInt(this.permission);
            dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        }

        public ChannelsBean() {
        }

        protected ChannelsBean(Parcel in) {
            this.id = in.readInt();
            this.area_id = in.readInt();
            this.channel_id = in.readInt();
            this.status = in.readInt();
            this.updated_at = in.readString();
            this.title = in.readString();
            this.permission = in.readInt();
            this.isChecked = in.readByte() != 0;
        }

        public static final Creator<ChannelsBean> CREATOR = new Creator<ChannelsBean>() {
            @Override
            public ChannelsBean createFromParcel(Parcel source) {
                return new ChannelsBean(source);
            }

            @Override
            public ChannelsBean[] newArray(int size) {
                return new ChannelsBean[size];
            }
        };
    }
}
