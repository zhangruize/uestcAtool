package com.example.cdzhangruize1.hotpursuit.model;

import java.util.ArrayList;

/**
 * 请注意：xpathMaps的元素的顺序会影响最终的解析结果，
 * 先插入主键为最佳原则
 */
public class BaseScraperModel {
    public static final int MODEL_TYPE_SCRAPER = 1;
    public static final int MODEL_TYPE_JSON = 2;

    private String name;
    private String icon;
    private int type;
    private int fromIndex;
    private ArrayList<String> links = new ArrayList<>();
    private ArrayList<MapRule> xpathMaps = new ArrayList<>();

    private boolean subscribe = true;

    public BaseScraperModel(String name, String icon, int type) {
        this(name, icon, type, 0);
    }

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
}
