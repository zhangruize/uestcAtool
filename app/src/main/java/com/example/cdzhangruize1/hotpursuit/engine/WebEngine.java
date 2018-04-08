package com.example.cdzhangruize1.hotpursuit.engine;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WebEngine {
    LoadCallback mLoadCallback;
    BaseScraperModel mScraperModel;

    public WebEngine() {
    }

    public void load(BaseScraperModel model) {//todo 我们或许应该在每一次Load都开启一次新的线程任务。可以支持并发load
        mScraperModel = model;
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
            String url = mScraperModel.links.get(0);
            try {
                Document d = Jsoup.connect(url).get();
                processDocument(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processDocument(Document d) {
            ArrayList<HashMap<String, String>> dataResult = new ArrayList<>();
            for (String path : mScraperModel.xpathMaps.keySet()) {
                String mapTo = mScraperModel.xpathMaps.get(path);
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
