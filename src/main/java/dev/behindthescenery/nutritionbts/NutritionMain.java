package dev.behindthescenery.nutritionbts;

import java.util.HashMap;
import java.util.List;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import dev.behindthescenery.nutritionbts.init.ConfigInit;
import dev.behindthescenery.nutritionbts.init.EventInit;
import dev.behindthescenery.nutritionbts.init.LoaderInit;
import dev.behindthescenery.nutritionbts.network.NutritionServerPacket;

public class NutritionMain implements ModInitializer {
    public static final HashMap<Item, List<Integer>> NUTRITION_ITEM_MAP = new HashMap<Item, List<Integer>>();
    public static final HashMap<Integer, List<Object>> NUTRITION_POSITIVE_EFFECTS = new HashMap<Integer, List<Object>>();
    public static final HashMap<Integer, List<Object>> NUTRITION_NEGATIVE_EFFECTS = new HashMap<Integer, List<Object>>();

    @Override
    public void onInitialize() {
        ConfigInit.init();
        LoaderInit.init();
        NutritionServerPacket.init();
        EventInit.init();
    }
}
