package dev.behindthescenery.nutritionbts.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

@Config(name = MOD_ID)
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class NutritionbtsConfig implements ConfigData {
    public int maxNutrition = 100;
    public int negativeNutrition = 10;
    public int positiveNutrition = 90;
    public int posX = 162;
    public int posY = 5;
}
