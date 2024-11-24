package dev.behindthescenery.nutritionbts.init;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import dev.behindthescenery.nutritionbts.config.NutritionbtsConfig;

public class ConfigInit {

    public static NutritionbtsConfig CONFIG = new NutritionbtsConfig();

    public static void init() {
        AutoConfig.register(NutritionbtsConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(NutritionbtsConfig.class).getConfig();
    }
}
