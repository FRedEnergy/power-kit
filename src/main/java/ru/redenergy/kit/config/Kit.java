package ru.redenergy.kit.config;

import java.util.List;

public class Kit {

    private String name;

    /**
     * In seconds
     */
    private long interval;

    private List<KitItem> items;

    public Kit(String name, long interval, List<KitItem> items) {
        this.name = name;
        this.interval = interval;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public long getInterval() {
        return interval;
    }

    public List<KitItem> getItems() {
        return items;
    }
}
