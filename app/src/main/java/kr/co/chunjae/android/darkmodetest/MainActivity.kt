package kr.co.chunjae.android.darkmodetest

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingWebView()

        webview.loadUrl("https://naver.com")
    }

    /**
     * WebView Setting
     */
    fun settingWebView() {
        webview.apply {
            //            addJavascriptInterface(new clsJsMethod this, _handler, mWebView), "HybridApp");
            setHorizontalScrollBarEnabled(false)
            setVerticalScrollBarEnabled(false)
        }
        webview.getSettings().apply {
            setJavaScriptEnabled(true)
            setCacheMode(WebSettings.LOAD_NO_CACHE)
            setDefaultTextEncodingName("utf-8")
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            setSavePassword(false)
            setDomStorageEnabled(true)
            setMediaPlaybackRequiresUserGesture(false)
        }


        val webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                    view: WebView,
                    url: String
            ): Boolean {
                if (url?.contains("rtsp")) {
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

                    Log.d("", "url : $url")
                    startActivity(intent)

                } else {

                    view.loadUrl(url)
                }
                return true
            }
        }
        setDayNightMode(resources.configuration)

        webview.webViewClient = webViewClient
    }


    /**
     * Configuration에 따른 Light / Dark Mode Setting
     * DarkMode 변경 시 OnCreate -> settingWebView -> setDayNightMode로 WebView의 DarkMode 적용됨
     * @param newConfig
     */
    fun setDayNightMode(newConfig : Configuration) {
        val currentNightMode = newConfig.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)

        when(currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                Toast.makeText(applicationContext, "NightMode No", Toast.LENGTH_SHORT).show()
                webview.settings.forceDark = WebSettings.FORCE_DARK_OFF
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                Toast.makeText(applicationContext, "NightMode Yes", Toast.LENGTH_SHORT).show()
                webview.settings.forceDark = WebSettings.FORCE_DARK_ON
            }
        }
    }
}
