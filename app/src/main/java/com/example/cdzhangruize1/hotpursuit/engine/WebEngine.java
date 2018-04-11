package com.example.cdzhangruize1.hotpursuit.engine;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

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

public class WebEngine {
    private ObservableEmitter<LoadTask> mEmitter;
    private OkHttpClient mClient = new OkHttpClient();
    private static WebEngine sInstance;

    public static WebEngine getInstance() {
        if (sInstance == null) {
            sInstance = new WebEngine();
        }
        return sInstance;
    }

    @SuppressLint("CheckResult")
    private WebEngine() {
        Observable.create(new ObservableOnSubscribe<LoadTask>() {
            @Override
            public void subscribe(ObservableEmitter<LoadTask> emitter) throws Exception {
                mEmitter = emitter;
            }
        }).observeOn(Schedulers.computation()).flatMap(new Function<LoadTask, ObservableSource<LoadTask>>() {
            @Override
            public ObservableSource<LoadTask> apply(LoadTask loadTask) throws Exception {
                String url = loadTask.getModel().getLinks().get(0);
                switch (loadTask.getModel().getType()) {
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
                            loadTask.getCallback().onFailed();
                        }
                        break;
                }
                loadTask.generateRecyclerView();
                return Observable.just(loadTask);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<LoadTask>() {
            @Override
            public void accept(LoadTask loadTask) throws Exception {
                if (loadTask.getView() != null) {
                    loadTask.getCallback().onSucceed(loadTask.getView());
                } else {
                    loadTask.getCallback().onFailed();
                }
            }
        });
    }

    public void load(LoadTask task) {
        mEmitter.onNext(task);
    }

    public interface LoadCallback2 {
        void onSucceed(RecyclerView view);

        void onFailed();
    }
}
