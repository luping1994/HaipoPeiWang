package net.suntrans.haipopeiwang.activity;

import android.os.Bundle;


import net.suntrans.haipopeiwang.BaseActivity;
import net.suntrans.haipopeiwang.Config;
import net.suntrans.haipopeiwang.R;
import net.suntrans.haipopeiwang.fragment.SixParamFragment;
import net.suntrans.haipopeiwang.fragment.TenParamFragment;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Looney on 2018/3/5.
 * Des:
 */

public class ParamActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_param;
    }

    private String type;
    private static final String TAG = "ParamActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String title = getIntent().getStringExtra("title");
        setBackArrowEnable(true);
        setToolBarTitle(title);


        String ip = getIntent().getStringExtra("ip");
        int port = getIntent().getIntExtra("port", 8899);
        type = getIntent().getStringExtra("type");
        if (Config.ST_SLC_6.equals(type)) {

            SixParamFragment fragment = SixParamFragment.newInstance(ip, port);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();


        } else if (Config.ST_SLC_10.equals(type)) {

            TenParamFragment fragment = TenParamFragment.newInstance(ip, port);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

        } else if (Config.ST_SECC.equals(type)) {


        }
    }


}
