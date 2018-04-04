package com.example.cdzhangruize1.hotpursuit.engine;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;

public class Engine {
    LoadCallback mLoadCallback;
    BaseScraperModel mModel;
    WebView mWebView;

    public class MyJavaScriptInterface {
        @JavascriptInterface
        public void showHTML(String html) {
            ArrayList<HashMap<String, String>> dataResult = new ArrayList<>();
            Document d = Jsoup.parse(html);
            for (String path : mModel.xpathMaps.keySet()) {
                String mapTo = mModel.xpathMaps.get(path);
                boolean needBreak = false;
                int count = 1;
                while (!needBreak) {
                    String sel = path.replace("$", count + "");
                    Element e = d.selectFirst(sel);
                    if (e.text() != null) {
                        String value = e.text().trim();
                        if (dataResult.size() < count) {
                            dataResult.add(new HashMap<String, String>());
                        }
                        dataResult.get(count - 1).put(mapTo, value);
                    } else {
                        needBreak = true;
                    }
                    count++;
                }
            }
            if (mLoadCallback != null) {
                mLoadCallback.onSucceed(dataResult);
            }
        }
    }

    public Engine(Context context) {
        mWebView = new WebView(context);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl("javascript:HTMLOUT.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });
    }

    public void setModel(BaseScraperModel mModel) {
        this.mModel = mModel;
    }

    public void load() {
        mWebView.loadUrl(mModel.links.get(0));
    }

    public void setmLoadCallback(LoadCallback mLoadCallback) {
        this.mLoadCallback = mLoadCallback;
    }

    public interface LoadCallback {
        void onSucceed(ArrayList<HashMap<String, String>> data);

        void onFailed();
    }
}
