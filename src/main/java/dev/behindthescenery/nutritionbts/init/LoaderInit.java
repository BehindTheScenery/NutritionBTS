package dev.behindthescenery.nutritionbts.init;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import dev.behindthescenery.nutritionbts.data.NutritionLoader;

public class LoaderInit {

    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new NutritionLoader());
    }
}
