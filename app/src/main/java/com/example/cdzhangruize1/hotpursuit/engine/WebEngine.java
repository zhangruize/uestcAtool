package com.example.cdzhangruize1.hotpursuit.engine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

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

import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.*;

public class WebEngine {
    private ObservableEmitter<LoadTask> mEmitter;

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
                try {
                    Document d = Jsoup.connect(url).get();
                    loadTask.processDocument(d, url);
                    loadTask.generateRecyclerView();
                } catch (IOException e) {
                    loadTask.callback.onFailed();
                }
                return Observable.just(loadTask);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<LoadTask>() {
            @Override
            public void accept(LoadTask loadTask) throws Exception {
                loadTask.callback.onSucceed(loadTask.view);
            }
        });
    }

    /**
     * 创建一个任务，获取数据后生成列表
     *
     * @param model      网络模型
     * @param listEngine 列表生成引擎
     * @param context    用来生成列表
     * @param callback   回调
     */
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

        public void processDocument(Document d, String link) {
            data.clear();
            for (MapRule mapRule : model.getMapRules()) {
                String selector = mapRule.selector;
                String name = mapRule.name;
                boolean needBreak = false;
                int count = 1;
                while (!needBreak) {
                    String sel = selector.replace("$", count + "");
                    Element e = d.selectFirst(sel);
                    String value = null;
                    switch (mapRule.type) {
                        case INNER_HTML:
                            if (e != null && e.childNodeSize() > 0 && e.childNode(0) instanceof TextNode) {
                                value = ((TextNode) e.childNode(0)).text().trim();
                            }
                            break;
                        case SRC:
                            if (e != null) {
                                String src = e.attributes().get("src").trim();
                                if (!src.startsWith("http")) {
                                    value = link.substring(0, link.lastIndexOf('/')) +
                                            (src.startsWith("/") ? "" : "/") + src;
                                }
                            }
                            break;
                    }
                    if (value != null) {
                        if (data.size() < count) {
                            data.add(new HashMap<String, String>());
                        }
                        data.get(count - 1).put(name, value);
                    } else if (count >= data.size()) {
                        needBreak = true;
                    }
                    count++;
                }
            }
        }

        public void generateRecyclerView() {
            view = listEngine.load(data, context);
        }
    }

    public interface LoadCallback2 {
        void onSucceed(RecyclerView view);

        void onFailed();
    }
}
