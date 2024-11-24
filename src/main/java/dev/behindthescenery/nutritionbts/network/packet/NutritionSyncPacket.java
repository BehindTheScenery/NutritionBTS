package dev.behindthescenery.nutritionbts.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record NutritionSyncPacket() implements CustomPayload {

    public static final CustomPayload.Id<NutritionSyncPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("nutritionbts", "nutrition_sync_packet"));

    public static final PacketCodec<RegistryByteBuf, NutritionSyncPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
    }, buf -> new NutritionSyncPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}


