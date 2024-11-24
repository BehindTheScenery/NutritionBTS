package dev.behindthescenery.nutritionbts.init;

import dev.behindthescenery.nutritionbts.config.NutritionbtsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class ConfigInit {

    public static NutritionbtsConfig CONFIG = new NutritionbtsConfig();

    public static void init() {
        AutoConfig.register(NutritionbtsConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(NutritionbtsConfig.class).getConfig();
    }
}
