package com.example.hetwapenapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment

class FoodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView: WebView = view.findViewById(R.id.webview)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Schakel JavaScript in

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // JavaScript om een specifiek deel van de pagina weer te geven
                view?.loadUrl("""
                    javascript:(function() {
                        var targetElement = document.querySelector('.et_pb_column.et_pb_column_4_4.et_pb_column_1.et_pb_css_mix_blend_mode_passthrough.et-last-child');
                        if (targetElement) {
                            document.body.innerHTML = '';
                            document.body.appendChild(targetElement);
                        }
                    })();
                """)
            }
        }

        webView.loadUrl("https://wapenvanroosendaal.nl/menukaart/")
    }
}
