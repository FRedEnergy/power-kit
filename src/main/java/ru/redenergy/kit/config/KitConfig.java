package ru.redenergy.kit.config;

import java.util.ArrayList;
import java.util.List;

public class KitConfig {

    private List<Kit> kits = new ArrayList<>();

    public KitConfig(List<Kit> kits) {
        this.kits = kits;
    }

    public Kit findKit(String name){
        return kits.stream().filter(it -> it.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
