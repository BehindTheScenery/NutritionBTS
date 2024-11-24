package dev.behindthescenery.nutritionbts;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import dev.behindthescenery.nutritionbts.init.RenderInit;
import dev.behindthescenery.nutritionbts.network.NutritionClientPacket;

@Environment(EnvType.CLIENT)
public class NutritionClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RenderInit.init();
        NutritionClientPacket.init();
    }
}
