package ru.redenergy.kit.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents kit config
 * Contains list of kits, which users and receive
 */
public class KitConfig {

    /**
     * List of loaded kits
     */
    private List<Kit> kits = new ArrayList<>();

    public KitConfig(List<Kit> kits) {
        this.kits = kits;
    }

    /**
     * Returns kit with the given name, if there are more than one kit with such name, it will return the first found
     * if there is not any kit with such name it will return null
     * @param name - kit name
     */
    public Kit findKit(String name) {
        return kits.stream().filter(it -> it.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Kit> getKits() {
        return kits;
    }
}