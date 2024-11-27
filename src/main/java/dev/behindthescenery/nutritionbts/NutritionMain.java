package dev.behindthescenery.nutritionbts;

import dev.behindthescenery.nutritionbts.data.FileLoaderLoader;
import dev.behindthescenery.nutritionbts.init.ConfigInit;
import dev.behindthescenery.nutritionbts.init.EventInit;
import dev.behindthescenery.nutritionbts.network.NutritionServerPacketHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

@Mod(MOD_ID)
public class NutritionMain {
    public static final String MOD_ID = "nutritionbts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    public NutritionMain() {
        NeoForge.EVENT_BUS.addListener(this::addReloadListener);
        ConfigInit.init();
        NutritionServerPacketHandler.init();
        EventInit.init();
    }

    @SubscribeEvent
    private void addReloadListener(@NotNull AddReloadListenerEvent event) {
        event.addListener(new FileLoaderLoader());
    }
}
