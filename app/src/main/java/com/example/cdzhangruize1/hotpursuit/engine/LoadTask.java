package com.example.cdzhangruize1.hotpursuit.engine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel;
import com.jayway.jsonpath.JsonPath;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_INNERHTML;
import static com.example.cdzhangruize1.hotpursuit.model.BaseScraperModel.MapRule.ELEMENT_TYPE_SRC;

public class LoadTask {
    private BaseScraperModel model;
    private WebEngine.LoadCallback2 callback;
    private ListEngine listEngine;
    private Context context;

    private ArrayList<HashMap<String, String>> data = new ArrayList<>();
    private RecyclerView view;

    public LoadTask(BaseScraperModel model, WebEngine.LoadCallback2 callback, ListEngine listEngine, Context context) {
        this.model = model;
        this.callback = callback;
        this.listEngine = listEngine;
        this.context = context;
    }

    public void generateRecyclerView() {
        if (view == null) {
            view = listEngine.generateView(data, context);
        } else if (view.getAdapter() instanceof BasicListAdapter) {
            BasicListAdapter adapter = (BasicListAdapter) view.getAdapter();
            adapter.setData(data);
        } else {
            //todo error
        }
    }

    public void processData(Object source, String contextLink) {//应当传入Document或者是jsonString
        for (BaseScraperModel.MapRule mapRule : model.getMapRules()) {
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

    private String extractValue(Object source, String sel, BaseScraperModel.MapRule mapRule, String contextLink) {
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

    public BaseScraperModel getModel() {
        return model;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }

    public WebEngine.LoadCallback2 getCallback() {
        return callback;
    }

    public RecyclerView getView() {
        return view;
    }

    public void setModel(BaseScraperModel model) {
        this.model = model;
    }
}
