package net.suntrans.suntranscomponents

import android.content.Context
import android.os.Build
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.BarUtils
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import net.suntrans.suntranscomponents.utils.ToastUtils

/**
 * Created by Looney on 2018/8/15.
 * Des:
 */
open class CBaseFragment : Fragment() {
    protected val handler = Handler()

    protected  var loadingDialog: QMUITipDialog? = null

    protected  var alertDialog: QMUITipDialog? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    fun showLoading(msg: String) {

    }

    fun hideLoading() {

    }

    fun showToast(msg: String?) {
        ToastUtils.showToast(msg,context!!.applicationContext)
    }

    fun showErr(msg: String?) {
        ToastUtils.showToast(msg,context!!.applicationContext)
    }

    protected fun fixStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val statusBarFix = view!!.findViewById<View>(R.id.statusBarFix)
            val toolbar = view!!.findViewById<Toolbar>(R.id.toolbar)
            val statusBarHeight = BarUtils.getStatusBarHeight()
            if (statusBarFix != null)
                statusBarFix!!.layoutParams.height = statusBarHeight
            else if (toolbar != null) {
                val layoutParams: ViewGroup.MarginLayoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + statusBarHeight
                        , layoutParams.rightMargin, layoutParams.bottomMargin)
                toolbar.layoutParams = layoutParams
            }
        }
    }

    protected fun isGtKITKAT(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }



}