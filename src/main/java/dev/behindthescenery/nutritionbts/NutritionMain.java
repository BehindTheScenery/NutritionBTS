package dev.behindthescenery.nutritionbts;

import java.util.HashMap;
import java.util.List;

import net.minecraft.item.Item;
import dev.behindthescenery.nutritionbts.init.ConfigInit;
import dev.behindthescenery.nutritionbts.init.EventInit;
import dev.behindthescenery.nutritionbts.init.LoaderInit;
import dev.behindthescenery.nutritionbts.network.NutritionServerPacketHandler;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

@Mod(MOD_ID)
public class NutritionMain {
    public static final String MOD_ID = "nutritionbts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final HashMap<Item, List<Integer>> NUTRITION_ITEM_MAP = new HashMap<>();
    public static final HashMap<Integer, List<Object>> NUTRITION_POSITIVE_EFFECTS = new HashMap<>();
    public static final HashMap<Integer, List<Object>> NUTRITION_NEGATIVE_EFFECTS = new HashMap<>();

    public NutritionMain() {
        ConfigInit.init();
        LoaderInit.init();
        NutritionServerPacketHandler.init();
        EventInit.init();
    }
}
