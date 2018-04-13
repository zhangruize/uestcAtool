package com.example.cdzhangruize1.hotpursuit.model;

import java.util.ArrayList;

/**
 * 请注意：xpathMaps的元素的顺序会影响最终的解析结果，
 * 先插入主键为最佳原则
 */
public class BaseScraperModel {
    public static final transient int MODEL_TYPE_SCRAPER = 1;
    public static final transient int MODEL_TYPE_JSON = 2;

    private String name;
    private String icon;
    private int type;
    private int fromIndex;
    private ArrayList<String> links = new ArrayList<>();
    private ArrayList<MapRule> xpathMaps = new ArrayList<>();

    //客户端使用变量，服务器不需要保存
    private transient boolean subscribe = false;
    private transient boolean isNew = false;//如果客户端之前没有此模型的订阅设置，则表明此模型是新的。

    public BaseScraperModel(String name, String icon, int type, int fromIndex) {
        this.name = name;
        this.icon = icon;
        this.type = type;
        this.fromIndex = fromIndex;
    }

    public void addLink(String link) {
        links.add(link);
    }

    public void addMapRule(MapRule rule) {
        xpathMaps.add(rule);
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public ArrayList<MapRule> getMapRules() {
        return xpathMaps;
    }

    public int getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }

    public String getName(){
        return name;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public static class MapRule {
        public String selector;
        public String name;
        public int type;

        public static final int ELEMENT_TYPE_INNERHTML = 1;
        public static final int ELEMENT_TYPE_SRC = 2;

        public MapRule(String selector, String name) {//如果是JSON类型，不需要type
            this.selector = selector;
            this.name = name;
        }

        public MapRule(String selector, String name, int type) {
            this.selector = selector;
            this.name = name;
            this.type = type;
        }
    }
}
