package net.suntrans.suntranscomponents.auto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import net.suntrans.suntranscomponents.CBaseActivity;
import net.suntrans.suntranscomponents.R;
import net.suntrans.suntranscomponents.c4800.InteractionFragment;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
public class InteractionActivity extends CBaseActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, InteractionActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackArrowEnable(true);
        setToolBarTitle(getString(R.string.title_interaction));
        InteractionFragment interactionFragment = InteractionFragment.Companion.newInstance();
        InteractionPresenterImpl impl = new InteractionPresenterImpl(interactionFragment);
        impl.onStart();

        getSupportFragmentManager().beginTransaction().replace(R.id.content,interactionFragment).commit();

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_interaction;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        if(item.getItemId()==R.id.add){
            InteractionAddActivity.start(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
