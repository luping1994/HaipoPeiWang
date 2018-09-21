package net.suntrans.suntranscomponents.auto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import net.suntrans.suntranscomponents.CBaseActivity;
import net.suntrans.suntranscomponents.R;
import net.suntrans.suntranscomponents.c4800.Interaction;
import net.suntrans.suntranscomponents.c4800.add.InteractionAddContract;
import net.suntrans.suntranscomponents.c4800.add.InteractionAddFragment;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
public class InteractionAddActivity extends CBaseActivity {

    private InteractionAddFragment interactionFragment;
    private InteractionAddContract.Presenter impl;

    public static void start(Context context) {
        Intent starter = new Intent(context, InteractionAddActivity.class);
        starter.putExtra("type", InteractionAddFragment.FRAGMENT_ADD);
        context.startActivity(starter);
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_interaction_add;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setBackArrowEnable(true);
//        setToolBarTitle(getString(R.string.title_interaction_add));
        String type = getIntent().getStringExtra("type");
        Interaction data = getIntent().getParcelableExtra("data");
        if (data==null){
            data=new Interaction();
        }

        interactionFragment = InteractionAddFragment.Companion.newInstance(type, data);
        impl = new InteractionAddPresenterImpl(interactionFragment);
        impl.onStart();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, interactionFragment).commit();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
