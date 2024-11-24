package dev.behindthescenery.nutritionbts.init;

import dev.behindthescenery.nutritionbts.data.NutritionLoader;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class LoaderInit {

    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new NutritionLoader());
    }
}
