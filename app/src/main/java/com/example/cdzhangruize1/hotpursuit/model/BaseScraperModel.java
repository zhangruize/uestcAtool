package com.example.cdzhangruize1.hotpursuit.model;

import java.util.ArrayList;

/**
 * 请注意：xpathMaps的元素的顺序会影响最终的解析结果，
 * 先插入主键为最佳原则
 */
public class BaseScraperModel {
    private ArrayList<String> links = new ArrayList<>();
    private ArrayList<MapRule> xpathMaps = new ArrayList<>();

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

    public static class MapRule {
        public String selector;
        public String name;
        public Type type;

        public enum Type {
            INNER_HTML, SRC
        }

        public MapRule(String selector, String name, Type type) {
            this.selector = selector;
            this.name = name;
            this.type = type;
        }
    }
}
