package com.example.cdzhangruize1.hotpursuit.engine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.jayway.jsonpath.JsonPath;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.*;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_INNERHTML;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_SRC;

public class WebEngine {
    private ObservableEmitter<LoadTask> mEmitter;
    private OkHttpClient mClient = new OkHttpClient();

    @SuppressLint("CheckResult")
    public WebEngine() {
        Observable.create(new ObservableOnSubscribe<LoadTask>() {
            @Override
            public void subscribe(ObservableEmitter<LoadTask> emitter) throws Exception {
                mEmitter = emitter;
            }
        }).observeOn(Schedulers.computation()).flatMap(new Function<LoadTask, ObservableSource<LoadTask>>() {
            @Override
            public ObservableSource<LoadTask> apply(LoadTask loadTask) throws Exception {
                String url = loadTask.model.getLinks().get(0);
                switch (loadTask.model.getType()) {
                    case BaseScraperModel.MODEL_TYPE_JSON:
                        Request request = new Request.Builder().url(url).build();
                        Response response = mClient.newCall(request).execute();
                        if (response.body() != null) {
                            String json = response.body().string();
                            loadTask.processData(json, url);
                        }
                        break;
                    case BaseScraperModel.MODEL_TYPE_SCRAPER:
                        try {
                            Document d = Jsoup.connect(url).get();
                            loadTask.processData(d, url);
                        } catch (IOException e) {
                            loadTask.callback.onFailed();
                        }
                        break;
                }
                loadTask.generateRecyclerView();
                return Observable.just(loadTask);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<LoadTask>() {
            @Override
            public void accept(LoadTask loadTask) throws Exception {
                if (loadTask.view != null) {
                    loadTask.callback.onSucceed(loadTask.view);
                } else {
                    loadTask.callback.onFailed();
                }
            }
        });
    }

    public void load(BaseScraperModel model, ListEngine listEngine, Context context, LoadCallback2 callback) {
        mEmitter.onNext(new LoadTask(model, callback, listEngine, context));
    }

    class LoadTask {
        BaseScraperModel model;
        LoadCallback2 callback;
        ListEngine listEngine;
        Context context;

        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        RecyclerView view;

        LoadTask(BaseScraperModel model, LoadCallback2 callback, ListEngine listEngine, Context context) {
            this.model = model;
            this.callback = callback;
            this.listEngine = listEngine;
            this.context = context;
        }

        public void generateRecyclerView() {
            view = listEngine.load(data, context);
        }

        public void processData(Object source, String contextLink) {//应当传入Document或者是jsonString
            for (MapRule mapRule : model.getMapRules()) {
                String selector = mapRule.selector;
                String name = mapRule.name;
                boolean needBreak = false;
                int index = model.getFromIndex();
                int count = 0;
                while (!needBreak) {
                    String sel = selector.replace("$", index + "");
                    String value = extractValue(source, sel, mapRule, contextLink);
                    if (value != null) {
                        while (count >= data.size()) {
                            data.add(new HashMap<String, String>());
                        }
                        data.get(count).put(name, value);
                    } else if (count > data.size()) {
                        needBreak = true;
                    }
                    count++;
                    index++;
                }
            }
        }

        private String extractValue(Object source, String sel, MapRule mapRule, String contextLink) {
            //此方法只需要将source中针对特定的sel提取出最终想要装填时使用的数据即可。
            if (source instanceof Document) {
                Document d = (Document) source;
                Element e = d.selectFirst(sel);
                String value = null;
                switch (mapRule.type) {
                    case ELEMENT_TYPE_INNERHTML:
                        if (e != null && e.childNodeSize() > 0 && e.childNode(0) instanceof TextNode) {
                            value = ((TextNode) e.childNode(0)).text().trim();
                        }
                        break;
                    case ELEMENT_TYPE_SRC:
                        if (e != null) {
                            String src = e.attributes().get("src").trim();
                            if (!src.startsWith("http")) {
                                value = contextLink.substring(0, contextLink.lastIndexOf('/')) +
                                        (src.startsWith("/") ? "" : "/") + src;
                            }
                        }
                        break;
                }
                return value;
            } else if (source instanceof String) {//当做json处理
                try {
                    return JsonPath.read((String) source, sel);
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }

        public void clear() {
            data.clear();
        }
    }

    public interface LoadCallback2 {
        void onSucceed(RecyclerView view);

        void onFailed();
    }
}
