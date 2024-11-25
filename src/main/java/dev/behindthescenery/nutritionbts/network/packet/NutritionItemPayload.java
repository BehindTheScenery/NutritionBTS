package dev.behindthescenery.nutritionbts.network.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

public record NutritionItemPayload(List<Integer> itemIds, List<Integer> nutritionValues) implements CustomPayload {

    public static final CustomPayload.Id<NutritionItemPayload> PACKET_ID = new CustomPayload.Id<>(Identifier.of(MOD_ID, "nutrition_item_packet"));

    public static final PacketCodec<RegistryByteBuf, NutritionItemPayload> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeCollection(value.itemIds, PacketByteBuf::writeInt);
        buf.writeCollection(value.nutritionValues, PacketByteBuf::writeInt);
    }, buf -> new NutritionItemPayload(buf.readList(PacketByteBuf::readInt), buf.readList(PacketByteBuf::readInt)));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}


