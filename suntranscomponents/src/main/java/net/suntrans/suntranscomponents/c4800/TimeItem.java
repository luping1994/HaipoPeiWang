package net.suntrans.suntranscomponents.c4800;

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
public class TimeItem {
    public String id;
    public String title;
    public String repeat;
    public String delay;
    public String type;
    public String startTime;
    public String endTime;

    public TimeItem(String id, String title, String repeat, String delay, String type) {
        this.id = id;
        this.title = title;
        this.repeat = repeat;
        this.delay = delay;
        this.type = type;
    }
}
