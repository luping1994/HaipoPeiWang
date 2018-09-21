package net.suntrans.suntranscomponents.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2018/9/19.
 * Des:
 */
public class RoomSrd {


    /**
     * id : 181
     * name : 客厅-T
     * device : {"id":233,"name":"测控器","title":"测控器","type":4800,"sub":[{"name":"温度","value":"wendu"},{"name":"湿度","value":"shidu"},{"name":"人员","value":"renyuan"}]}
     */

    public int id;
    public String name;
    public DeviceBean device;
    public List<DeviceBean> list;
    public boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public static class DeviceBean implements Parcelable {

        /**
         * id : 233
         * name : 测控器
         * title : 测控器
         * type : 4800
         * sub : [{"name":"温度","value":"wendu"},{"name":"湿度","value":"shidu"},{"name":"人员","value":"renyuan"}]
         */
        public int permission = 1;//通道权限 后台系统未实现 这里暂时都赋值为1 表示都有权限

        public int id;
        public String name;
        public String title;
        public int type;

        public String area;

        public List<SubBean> sub;

        public boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public static class SubBean implements Parcelable {
            /**
             * name : 温度
             * value : wendu
             */
            public String name;
            public String value;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
                dest.writeString(this.value);
            }

            public SubBean() {
            }

            protected SubBean(Parcel in) {
                this.name = in.readString();
                this.value = in.readString();
            }

            public static final Creator<SubBean> CREATOR = new Creator<SubBean>() {
                @Override
                public SubBean createFromParcel(Parcel source) {
                    return new SubBean(source);
                }

                @Override
                public SubBean[] newArray(int size) {
                    return new SubBean[size];
                }
            };
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.permission);
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.title);
            dest.writeInt(this.type);
            dest.writeString(this.area);
            dest.writeTypedList(this.sub);
            dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        }

        public DeviceBean() {
        }

        protected DeviceBean(Parcel in) {
            this.permission = in.readInt();
            this.id = in.readInt();
            this.name = in.readString();
            this.title = in.readString();
            this.type = in.readInt();
            this.area = in.readString();
            this.sub = in.createTypedArrayList(SubBean.CREATOR);
            this.isChecked = in.readByte() != 0;
        }

        public static final Parcelable.Creator<DeviceBean> CREATOR = new Parcelable.Creator<DeviceBean>() {
            @Override
            public DeviceBean createFromParcel(Parcel source) {
                return new DeviceBean(source);
            }

            @Override
            public DeviceBean[] newArray(int size) {
                return new DeviceBean[size];
            }
        };
    }
}
