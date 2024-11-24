package dev.behindthescenery.nutritionbts.network.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.*;

public record NutritionEffectPacket(List<Integer> positiveEffectCount, List<Identifier> positiveEffectIds, List<Integer> positiveEffectDurations, List<Integer> positiveEffectAmplifiers,
                                    List<Identifier> positiveAttributeIds,
                                    List<Float> positiveAttributeValues, List<String> positiveAttributeOperations, List<Integer> negativeEffectCount, List<Identifier> negativeEffectIds,
                                    List<Integer> negativeEffectDurations,
                                    List<Integer> negativeEffectAmplifiers, List<Identifier> negativeAttributeIds, List<Float> negativeAttributeValues,
                                    List<String> negativeAttributeOperations) implements CustomPayload {

    public static final CustomPayload.Id<NutritionEffectPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("nutritionbts", "nutrition_effect_packet"));

    public static final PacketCodec<RegistryByteBuf, NutritionEffectPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeCollection(value.positiveEffectCount(), PacketByteBuf::writeInt);
        buf.writeCollection(value.positiveEffectIds(), PacketByteBuf::writeIdentifier);
        buf.writeCollection(value.positiveEffectDurations(), PacketByteBuf::writeInt);
        buf.writeCollection(value.positiveEffectAmplifiers(), PacketByteBuf::writeInt);
        buf.writeCollection(value.positiveAttributeIds(), PacketByteBuf::writeIdentifier);
        buf.writeCollection(value.positiveAttributeValues(), PacketByteBuf::writeFloat);
        buf.writeCollection(value.positiveAttributeOperations(), PacketByteBuf::writeString);
        buf.writeCollection(value.negativeEffectCount(), PacketByteBuf::writeInt);
        buf.writeCollection(value.negativeEffectIds(), PacketByteBuf::writeIdentifier);
        buf.writeCollection(value.negativeEffectDurations(), PacketByteBuf::writeInt);
        buf.writeCollection(value.negativeEffectAmplifiers(), PacketByteBuf::writeInt);
        buf.writeCollection(value.negativeAttributeIds(), PacketByteBuf::writeIdentifier);
        buf.writeCollection(value.negativeAttributeValues(), PacketByteBuf::writeFloat);
        buf.writeCollection(value.negativeAttributeOperations(), PacketByteBuf::writeString);
    }, buf -> new NutritionEffectPacket(buf.readList(PacketByteBuf::readInt), buf.readList(PacketByteBuf::readIdentifier), buf.readList(PacketByteBuf::readInt), buf.readList(PacketByteBuf::readInt),
            buf.readList(PacketByteBuf::readIdentifier), buf.readList(PacketByteBuf::readFloat), buf.readList(PacketByteBuf::readString),
            buf.readList(PacketByteBuf::readInt), buf.readList(PacketByteBuf::readIdentifier), buf.readList(PacketByteBuf::readInt), buf.readList(PacketByteBuf::readInt),
            buf.readList(PacketByteBuf::readIdentifier), buf.readList(PacketByteBuf::readFloat), buf.readList(PacketByteBuf::readString)));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}


