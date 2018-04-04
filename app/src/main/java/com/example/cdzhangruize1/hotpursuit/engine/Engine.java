package com.example.cdzhangruize1.hotpursuit.engine;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Engine {
    LoadCallback mLoadCallback;
    BaseScraperModel mModel;

    public Engine() {
    }

    public void setModel(BaseScraperModel mModel) {
        this.mModel = mModel;
    }

    public void load() {
        mWorkThread.start();
    }

    public void setmLoadCallback(LoadCallback mLoadCallback) {
        this.mLoadCallback = mLoadCallback;
    }

    public interface LoadCallback {
        void onSucceed(ArrayList<HashMap<String, String>> data);

        void onFailed();
    }

    private Thread mWorkThread = new Thread(new Runnable() {
        @Override
        public void run() {
            String url = mModel.links.get(0);
            try {
                Document d = Jsoup.connect(url).get();
                processDocument(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processDocument(Document d) {
            ArrayList<HashMap<String, String>> dataResult = new ArrayList<>();
            for (String path : mModel.xpathMaps.keySet()) {
                String mapTo = mModel.xpathMaps.get(path);
                boolean needBreak = false;
                int count = 1;
                while (!needBreak) {
                    String sel = path.replace("$", count + "");
                    Element e = d.selectFirst(sel);
                    if (e != null && e.childNodeSize() > 0 && e.childNode(0) instanceof TextNode) {
                        String value = ((TextNode) e.childNode(0)).text().trim();
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
    });
}
