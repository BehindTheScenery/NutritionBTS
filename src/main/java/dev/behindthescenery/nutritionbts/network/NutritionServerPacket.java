package dev.behindthescenery.nutritionbts.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import dev.behindthescenery.nutritionbts.NutritionMain;
import dev.behindthescenery.nutritionbts.access.HungerManagerAccess;
import dev.behindthescenery.nutritionbts.network.packet.NutritionEffectPacket;
import dev.behindthescenery.nutritionbts.network.packet.NutritionItemPacket;
import dev.behindthescenery.nutritionbts.network.packet.NutritionPacket;
import dev.behindthescenery.nutritionbts.network.packet.NutritionSyncPacket;

public class NutritionServerPacket {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(NutritionPacket.PACKET_ID, NutritionPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(NutritionItemPacket.PACKET_ID, NutritionItemPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(NutritionEffectPacket.PACKET_ID, NutritionEffectPacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(NutritionSyncPacket.PACKET_ID, NutritionSyncPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(NutritionSyncPacket.PACKET_ID, (payload, context) -> {
            context.server().execute(() -> {
                writeS2CNutritionPacket(context.player(), ((HungerManagerAccess) context.player().getHungerManager()));
                writeS2CEffectNutritionPacket(context.player());
            });
        });
    }

    public static void writeS2CNutritionPacket(ServerPlayerEntity serverPlayerEntity, HungerManagerAccess hungerManagerAccess) {
        ServerPlayNetworking.send(serverPlayerEntity, new NutritionPacket(hungerManagerAccess.getNutritionLevel(0), hungerManagerAccess.getNutritionLevel(1), hungerManagerAccess.getNutritionLevel(2), hungerManagerAccess.getNutritionLevel(3), hungerManagerAccess.getNutritionLevel(4)));
    }

    public static void writeS2CItemNutritionPacket(ServerPlayerEntity serverPlayerEntity) {
        List<Integer> itemIds = new ArrayList<>();
        List<Integer> nutritionValues = new ArrayList<>();
        NutritionMain.NUTRITION_ITEM_MAP.forEach((item, list) -> {
            itemIds.add(Registries.ITEM.getRawId(item));
            nutritionValues.addAll(list);
        });

        ServerPlayNetworking.send(serverPlayerEntity, new NutritionItemPacket(itemIds, nutritionValues));
    }

    public static void writeS2CEffectNutritionPacket(ServerPlayerEntity serverPlayerEntity) {
        List<Integer> positiveEffectCount = new ArrayList<>();
        List<Identifier> positiveEffectIds = new ArrayList<>();
        List<Integer> positiveEffectDurations = new ArrayList<>();
        List<Integer> positiveEffectAmplifiers = new ArrayList<>();
        List<Identifier> positiveAttributeIds = new ArrayList<>();
        List<Float> positiveAttributeValues = new ArrayList<>();
        List<String> positiveAttributeOperations = new ArrayList<>();

        for (Map.Entry<Integer, List<Object>> entry : NutritionMain.NUTRITION_POSITIVE_EFFECTS.entrySet()) {
            int effectCount  = 0;
            int attributeCount = 0;
            for (Object object : entry.getValue()) {
                if (object instanceof StatusEffectInstance statusEffectInstance) {
                    positiveEffectIds.add(Registries.STATUS_EFFECT.getId(statusEffectInstance.getEffectType().value()));
                    positiveEffectDurations.add(statusEffectInstance.getDuration());
                    positiveEffectAmplifiers.add(statusEffectInstance.getAmplifier());
                    effectCount++;
                } else {
                    Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> multimap = (Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier>) object;
                    multimap.forEach((attribute, modifier) -> {
                        positiveAttributeIds.add(Registries.ATTRIBUTE.getId(attribute.value()));
                        positiveAttributeValues.add((float) modifier.value());
                        positiveAttributeOperations.add(modifier.operation().name());
                    });
                    attributeCount++;
                }
            }
            positiveEffectCount.add(effectCount);
            positiveEffectCount.add(attributeCount);
        }

        List<Integer> negativeEffectCount = new ArrayList<>();
        List<Identifier> negativeEffectIds = new ArrayList<>();
        List<Integer> negativeEffectDurations = new ArrayList<>();
        List<Integer> negativeEffectAmplifiers = new ArrayList<>();
        List<Identifier> negativeAttributeIds = new ArrayList<>();
        List<Float> negativeAttributeValues = new ArrayList<>();
        List<String> negativeAttributeOperations = new ArrayList<>();

        for (Map.Entry<Integer, List<Object>> entry : NutritionMain.NUTRITION_NEGATIVE_EFFECTS.entrySet()) {
            int effectCount  = 0;
            int attributeCount = 0;
            for (Object object : entry.getValue()) {
                if (object instanceof StatusEffectInstance statusEffectInstance) {
                    negativeEffectIds.add(Registries.STATUS_EFFECT.getId(statusEffectInstance.getEffectType().value()));
                    negativeEffectDurations.add(statusEffectInstance.getDuration());
                    negativeEffectAmplifiers.add(statusEffectInstance.getAmplifier());
                    effectCount++;
                } else {
                    Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> multimap = (Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier>) object;
                    multimap.forEach((attribute, modifier) -> {
                        negativeAttributeIds.add(Registries.ATTRIBUTE.getId(attribute.value()));
                        negativeAttributeValues.add((float) modifier.value());
                        negativeAttributeOperations.add(modifier.operation().name());
                    });
                    attributeCount++;
                }
            }
            negativeEffectCount.add(effectCount);
            negativeEffectCount.add(attributeCount);
        }

        ServerPlayNetworking.send(serverPlayerEntity, new NutritionEffectPacket(positiveEffectCount, positiveEffectIds, positiveEffectDurations, positiveEffectAmplifiers, positiveAttributeIds, positiveAttributeValues, positiveAttributeOperations, negativeEffectCount, negativeEffectIds, negativeEffectDurations, negativeEffectAmplifiers, negativeAttributeIds, negativeAttributeValues, negativeAttributeOperations));
    }

}
