package dev.behindthescenery.nutritionbts.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record NutritionPayload(int carbohydrateLevel, int proteinLevel, int fatLevel, int vitaminLevel,
                               int mineralLevel) implements CustomPayload {

    public static final CustomPayload.Id<NutritionPayload> PACKET_ID = new CustomPayload.Id<>(Identifier.of("nutritionbts", "nutrition_packet"));

    public static final PacketCodec<RegistryByteBuf, NutritionPayload> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeInt(value.carbohydrateLevel());
        buf.writeInt(value.proteinLevel());
        buf.writeInt(value.fatLevel());
        buf.writeInt(value.vitaminLevel());
        buf.writeInt(value.mineralLevel());
    }, buf -> new NutritionPayload(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}


