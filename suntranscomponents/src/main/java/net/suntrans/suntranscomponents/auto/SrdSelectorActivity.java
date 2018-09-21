package net.suntrans.suntranscomponents.auto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import net.suntrans.suntranscomponents.CBaseActivity;
import net.suntrans.suntranscomponents.R;
import net.suntrans.suntranscomponents.c4800.selector.ChannelSelector;
import net.suntrans.suntranscomponents.c4800.selector.ChannelSelectorContract;
import net.suntrans.suntranscomponents.c4800.selector.SrdSelector;
import net.suntrans.suntranscomponents.c4800.selector.SrdSelectorContract;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
public class SrdSelectorActivity extends CBaseActivity {

    private SrdSelector channelSelector;

    public static void start(Context context) {
        Intent starter = new Intent(context, SrdSelectorActivity.class);
        context.startActivity(starter);
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_channel_selector;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        channelSelector = SrdSelector.Companion.newInstance();
        SrdSelectorContract.Presenter impl = new SrdSelectorPresenterImpl(channelSelector);
        impl.onStart();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, channelSelector).commit();

    }


    private final FragmentManager.OnBackStackChangedListener mBackStackChangedListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {

                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);

    }
}
