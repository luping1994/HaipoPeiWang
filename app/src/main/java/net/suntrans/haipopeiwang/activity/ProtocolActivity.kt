package net.suntrans.haipopeiwang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_protocol.*

import net.suntrans.haipopeiwang.BaseActivity
import net.suntrans.haipopeiwang.BuildConfig
import net.suntrans.haipopeiwang.R

/**
 * Created by Looney on 2018/8/21.
 * Des:
 */
class ProtocolActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_protocol
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackArrowEnable(true)
        setToolBarTitle(getString(R.string.title_protocol))
        setUpWebview(webview)
        webview.loadUrl("https://hpjd.suntrans-cloud.com/protocol/index.html")

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
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowFileAccess = true// 设置允许访问文件数据
        webview.setInitialScale(0)
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.isVerticalScrollBarEnabled = false
        //        webview.setWebChromeClient(new WebChromeClient());

        val localWebSettings = webview.settings
        localWebSettings.javaScriptEnabled = true
        localWebSettings.javaScriptCanOpenWindowsAutomatically = true
        localWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL

        webview.isHorizontalScrollBarEnabled = false//水平不显示
//        webview.addJavascriptInterface(AndroidtoJs(), "control")

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
            }
        }
    }


    companion object {

        fun start(context: Context) {
            val starter = Intent(context, ProtocolActivity::class.java)
            context.startActivity(starter)
        }
    }
}
