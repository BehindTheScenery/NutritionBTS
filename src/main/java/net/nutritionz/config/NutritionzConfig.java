package net.nutritionz.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "nutritionz")
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class NutritionzConfig implements ConfigData {

    public int maxNutrition = 100;
    public int negativeNutrition = 10;
    public int positiveNutrition = 90;
    public int posX = 162;
    public int posY = 5;
    @Comment("Used for the nutrition gui")
    public String carbohydrateItemId = "minecraft:sugar";
    public String proteinItemId = "minecraft:chicken";
    public String fatItemId = "minecraft:porkchop";
    public String vitaminItemId = "minecraft:apple";
    public String mineralItemId = "minecraft:iron_ingot";
}
