package net.suntrans.haipopeiwang.activity;

import android.os.Bundle;
import android.widget.TextView;

import net.suntrans.haipopeiwang.BaseActivity;
import net.suntrans.haipopeiwang.R;
import net.suntrans.haipopeiwang.fragment.SLockFragment;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Looney on 2018/3/8.
 * Des:
 */

public class SLockActivity extends BaseActivity {
    private TextView titleTx;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBackArrowEnable(true);
        String title = getIntent().getStringExtra("title");
        setToolBarTitle(title);


        String ip = getIntent().getStringExtra("ip");
        int port = getIntent().getIntExtra("port", 8899);

        SLockFragment fragment = SLockFragment.newInstance(ip, port, title);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_sensus;
    }
}
