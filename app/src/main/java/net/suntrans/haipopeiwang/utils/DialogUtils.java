package net.suntrans.haipopeiwang.utils;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import android.content.Context;

import net.suntrans.haipopeiwang.R;

/**
 * Created by Looney on 2018/8/16.
 * Des:
 */
public class DialogUtils {
    public static QMUITipDialog showLoadingDialog(Context context, @QMUITipDialog.Builder.IconType int type, String tipWord) {
        return new QMUITipDialog.Builder(context)
                .setIconType(type)
                .setTipWord(tipWord)
                .create();
    }

    public static QMUIDialog createAlertDialog(Context context, String msg, String title) {
        return new QMUIDialog.MessageDialogBuilder(context)
                .setMessage(msg)
                .setTitle(title)
                .create();

    }
}
