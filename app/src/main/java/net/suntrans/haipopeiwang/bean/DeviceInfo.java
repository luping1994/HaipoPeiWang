package net.suntrans.haipopeiwang.bean;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Looney on 2018/3/2.
 * Des:
 */

public class DeviceInfo implements Comparable<DeviceInfo> {


    /**
     * Name : EMW3165#10E09F
     * IP : 192.168.0.251
     * Port : 8080
     * MAC : D0:BA:E4:10:E0:9F
     * Firmware Rev : ATV1.1.2@EMW3165
     * MICO OS Rev : 31620002.042
     * Model : EMW3165
     * Protocol : com.mxchip.at
     * Manufacturer : MXCHIP Inc.
     * Seed : 1156
     */

    /**
     * [
     {
     "name" : "ST-SLC-10_QQ_00002056",
     "vtype" : "4100",
     "micoOSRev" : "31621002.050",
     "ssid" : "ST202",
     "model" : "st-slc-10",
     "addr" : "2056",
     "firmwareRev" : "ST-SLC-10_QQ_2.0.2.6",
     "mac" : "C8:93:46:84:2E:BF",
     "ipAddress" : "10.255.8.227",
     "hardwareRev" : "ST3166_1.0.0",
     "seed" : "541",
     "protocol" : "com.mxchip.spp",
     "port" : "8899",
     "manufacturer" : "MXCHIP Inc."
     }
     ]
     */

    public String Name;
    public String IP;
    public int Port;
    public String MAC;
    @SerializedName("Firmware Rev")
    public String FirmwareRev; // FIXME check this code
    @SerializedName("MICO OS Rev")
    public String MICOOSRev; // FIXME check this code
    public String Model;
    public String Protocol;
    public String Manufacturer;
    public String Seed;
    public String vtype;

    @Override
    public int compareTo(@NonNull DeviceInfo o) {

        return  this.Name.compareTo(o.Name);
    }
}
