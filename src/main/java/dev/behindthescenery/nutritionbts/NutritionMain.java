package dev.behindthescenery.nutritionbts;

import java.util.HashMap;
import java.util.List;

import net.minecraft.item.Item;
import dev.behindthescenery.nutritionbts.init.ConfigInit;
import dev.behindthescenery.nutritionbts.init.EventInit;
import dev.behindthescenery.nutritionbts.init.LoaderInit;
import dev.behindthescenery.nutritionbts.network.NutritionServerPacket;
import net.neoforged.fml.common.Mod;

// TODO: 24.11.2024 REPLACE WITH MOD_ID
@Mod("nutritionbts")
public class NutritionMain {
    public static final HashMap<Item, List<Integer>> NUTRITION_ITEM_MAP = new HashMap<Item, List<Integer>>();
    public static final HashMap<Integer, List<Object>> NUTRITION_POSITIVE_EFFECTS = new HashMap<Integer, List<Object>>();
    public static final HashMap<Integer, List<Object>> NUTRITION_NEGATIVE_EFFECTS = new HashMap<Integer, List<Object>>();

    public NutritionMain() {
        ConfigInit.init();
        LoaderInit.init();
        NutritionServerPacket.init();
        EventInit.init();
    }
}
