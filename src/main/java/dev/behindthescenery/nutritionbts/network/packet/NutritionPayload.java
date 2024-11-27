package dev.behindthescenery.nutritionbts.network.packet;

import dev.behindthescenery.nutritionbts.nutrition.NutritionType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

public record NutritionPayload(Map<NutritionType, Integer> levels) implements CustomPayload {
    public static final CustomPayload.Id<NutritionPayload> PACKET_ID = new CustomPayload.Id<>(Identifier.of(MOD_ID, "nutrition_packet"));

    public static final PacketCodec<RegistryByteBuf, NutritionPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.map(HashMap::new, NutritionType.PACKET_CODEC, PacketCodecs.INTEGER), NutritionPayload::levels,
        NutritionPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}


