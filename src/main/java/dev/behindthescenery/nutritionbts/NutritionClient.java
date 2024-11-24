package dev.behindthescenery.nutritionbts;

import dev.behindthescenery.nutritionbts.init.RenderInit;
import dev.behindthescenery.nutritionbts.network.NutritionClientPacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@EventBusSubscriber(modid = "nutritionbts", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class NutritionClient {
    @SubscribeEvent
    public static void onStartup(FMLClientSetupEvent event) {
        RenderInit.init();
        NutritionClientPacketHandler.init();
    }
}
