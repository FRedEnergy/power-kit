package ru.redenergy.kit.config;

import java.util.List;

/**
 * Represents one item kit
 */
public class Kit {

    /**
     * Kit identifier
     */
    private String name;

    /**
     * Interval between kit uses in seconds
     */
    private long interval;

    /**
     * List of items available in this kit
     */
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
