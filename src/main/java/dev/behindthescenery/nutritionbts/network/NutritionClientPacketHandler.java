package dev.behindthescenery.nutritionbts.network;

import dev.behindthescenery.nutritionbts.access.HungerManagerAccess;
import dev.behindthescenery.nutritionbts.network.packet.NutritionPayload;
import dev.behindthescenery.nutritionbts.network.packet.NutritionSyncPayload;
import dev.behindthescenery.nutritionbts.nutrition.NutritionType;
import dev.behindthescenery.nutritionbts.screen.NutritionScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

import java.util.Map;

@SuppressWarnings("resource")
@Environment(EnvType.CLIENT)
public class NutritionClientPacketHandler {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(NutritionPayload.PACKET_ID, (payload, context) -> {
            Map<NutritionType, Integer> levels = payload.levels();
            context.client().execute(() -> {
                ((HungerManagerAccess) context.player().getHungerManager()).setNutritionLevels(levels);
                if (MinecraftClient.getInstance().currentScreen instanceof NutritionScreen screen) screen.markHeightDirty();
            });
        });
    }

    public static void writeC2SNutritionPacket() {
        ClientPlayNetworking.send(new NutritionSyncPayload());
    }
}
