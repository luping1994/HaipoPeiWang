package net.suntrans.haipopeiwang.energy

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import kotlinx.android.synthetic.main.fragment_energy.*
import net.suntrans.haipopeiwang.App
import net.suntrans.haipopeiwang.BaseFragment
import net.suntrans.haipopeiwang.LazyLoadFragment
import net.suntrans.haipopeiwang.R
import net.suntrans.haipopeiwang.R.id.webview
import net.suntrans.haipopeiwang.utils.LogUtil

/**
 * Created by Looney on 2018/9/6.
 * Des:
 */
class EnergyFragment : LazyLoadFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_energy, null, false)
    }

    private var isFinished = false
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fixStatusBar()
        val toolbar = view!!.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.title_energy)
        setUpWebview(webview)
        val token = App.getSharedPreferences()!!.getString("access_token", "")
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                    webview.evaluateJavascript("javascript:initEnergy('$token')") {
////
////                    }
//                }
                webview.loadUrl("javascript:initEnergy('$token')")
                isFinished = true
            }
        }
        ColorfulSwipeRefreshLayout.setOnRefreshListener {
            if (isFinished)
                webview.loadUrl("javascript:initEnergy('$token')")
            handler.postDelayed(Runnable {
                ColorfulSwipeRefreshLayout.isRefreshing = false
            }, 2000)
        }
        webview.addJavascriptInterface(AndroidtoJs(), "android")
    }

    fun loadData() {

        webview.loadUrl("file:///android_asset/energy.html")
        ColorfulSwipeRefreshLayout.isRefreshing = true
        handler.postDelayed(Runnable {
            ColorfulSwipeRefreshLayout.isRefreshing = false
        }, 5000)
    }

    companion object {
        val TAG = this.javaClass.simpleName
        fun newInstance(): EnergyFragment {
            val args = Bundle()

            val fragment = EnergyFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun setUpWebview(webview: WebView) {
        val settings = webview.settings

        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.setGeolocationEnabled(true)
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        webview.setInitialScale(0)
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.webViewClient = WebViewClient()
        webview.isVerticalScrollBarEnabled = false

        val localWebSettings = webview.settings
        localWebSettings.javaScriptEnabled = true
        localWebSettings.javaScriptCanOpenWindowsAutomatically = true
        localWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL

        webview.isHorizontalScrollBarEnabled = false//水平不显示
        webview.isVerticalScrollBarEnabled = true


    }

    // 继承自Object类
    inner class AndroidtoJs : Any() {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        fun callAndroid(code: String, msg: String) {
            ColorfulSwipeRefreshLayout.isRefreshing = false
            handler.removeCallbacksAndMessages(null)
            if (code == "200") {
//                showToast(msg)
            } else {
                showErr(msg)
            }
        }
    }

    override fun lazyLoadDataImpl() {
        loadData()
    }

}
