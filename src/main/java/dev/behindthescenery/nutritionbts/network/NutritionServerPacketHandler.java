package dev.behindthescenery.nutritionbts.network;

import dev.behindthescenery.nutritionbts.access.HungerManagerAccess;
import dev.behindthescenery.nutritionbts.network.packet.NutritionPayload;
import dev.behindthescenery.nutritionbts.network.packet.NutritionSyncPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class NutritionServerPacketHandler {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(NutritionPayload.PACKET_ID, NutritionPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(NutritionSyncPayload.PACKET_ID, NutritionSyncPayload.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(NutritionSyncPayload.PACKET_ID, (payload, context) -> {
            //noinspection resource
            context.server().execute(() -> {
                writeS2CNutritionPacket(context.player(), ((HungerManagerAccess) context.player().getHungerManager()));
            });
        });
    }

    public static void writeS2CNutritionPacket(ServerPlayerEntity serverPlayerEntity, @NotNull HungerManagerAccess hungerManagerAccess) {
        ServerPlayNetworking.send(serverPlayerEntity, new NutritionPayload(hungerManagerAccess.getNutritionLevels()));
    }
}
