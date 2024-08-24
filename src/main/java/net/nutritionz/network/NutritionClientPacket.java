package net.nutritionz.network;

import java.util.*;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.network.packet.NutritionEffectPacket;
import net.nutritionz.network.packet.NutritionItemPacket;
import net.nutritionz.network.packet.NutritionPacket;
import net.nutritionz.network.packet.NutritionSyncPacket;

@Environment(EnvType.CLIENT)
public class NutritionClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(NutritionPacket.PACKET_ID, (payload, context) -> {
            int carbohydrateLevel = payload.carbohydrateLevel();
            int proteinLevel = payload.proteinLevel();
            int fatLevel = payload.fatLevel();
            int vitaminLevel = payload.vitaminLevel();
            int mineralLevel = payload.mineralLevel();

            context.client().execute(() -> {
                ((HungerManagerAccess) context.player().getHungerManager()).setNutritionLevel(0, carbohydrateLevel);
                ((HungerManagerAccess) context.player().getHungerManager()).setNutritionLevel(1, proteinLevel);
                ((HungerManagerAccess) context.player().getHungerManager()).setNutritionLevel(2, fatLevel);
                ((HungerManagerAccess) context.player().getHungerManager()).setNutritionLevel(3, vitaminLevel);
                ((HungerManagerAccess) context.player().getHungerManager()).setNutritionLevel(4, mineralLevel);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NutritionItemPacket.PACKET_ID, (payload, context) -> {
            List<Integer> itemIds = payload.itemIds();
            List<Integer> nutritionValues = payload.nutritionValues();

            context.client().execute(() -> {
                NutritionMain.NUTRITION_ITEM_MAP.clear();
                for (int i = 0; i < itemIds.size(); i++) {
                    List<Integer> nutritionList = new ArrayList<Integer>();
                    nutritionList.add(nutritionValues.get(i * 5));
                    nutritionList.add(nutritionValues.get(i * 5 + 1));
                    nutritionList.add(nutritionValues.get(i * 5 + 2));
                    nutritionList.add(nutritionValues.get(i * 5 + 3));
                    nutritionList.add(nutritionValues.get(i * 5 + 4));
                    NutritionMain.NUTRITION_ITEM_MAP.put(Registries.ITEM.get(itemIds.get(i)), nutritionList);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NutritionEffectPacket.PACKET_ID, (payload, context) -> {

            List<Integer> positiveEffectCount = payload.positiveEffectCount();
            List<Identifier> positiveEffectIds = payload.positiveEffectIds();
            List<Integer> positiveEffectDurations = payload.positiveEffectDurations();
            List<Integer> positiveEffectAmplifiers = payload.positiveEffectAmplifiers();
            List<Identifier> positiveAttributeIds = payload.positiveAttributeIds();
            List<Float> positiveAttributeValues = payload.positiveAttributeValues();
            List<String> positiveAttributeOperations = payload.positiveAttributeOperations();

            List<Integer> negativeEffectCount = payload.negativeEffectCount();
            List<Identifier> negativeEffectIds = payload.negativeEffectIds();
            List<Integer> negativeEffectDurations = payload.negativeEffectDurations();
            List<Integer> negativeEffectAmplifiers = payload.negativeEffectAmplifiers();
            List<Identifier> negativeAttributeIds = payload.negativeAttributeIds();
            List<Float> negativeAttributeValues = payload.negativeAttributeValues();
            List<String> negativeAttributeOperations = payload.negativeAttributeOperations();

            HashMap<Integer, List<Object>> positiveEffectMap = new HashMap<>();

            int effectCount = 0;
            int attributeCount = 0;
            for (int i = 0; i < positiveEffectCount.size() / 2; i++) {
                List<Object> list = new ArrayList<>();

                for (int u = 0; u < positiveEffectCount.get(i * 2); u++) {
                    if (Registries.STATUS_EFFECT.get(positiveEffectIds.get(effectCount)) != null) {
                        list.add(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(positiveEffectIds.get(effectCount)).get(), positiveEffectDurations.get(effectCount), positiveEffectAmplifiers.get(effectCount), false, false, true));
                    }
                    effectCount++;
                }

                for (int u = 0; u < positiveEffectCount.get(i * 2 + 1); u++) {
                    if (Registries.ATTRIBUTE.get(positiveAttributeIds.get(attributeCount)) != null) {
                        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                        attributeModifiers.put(Registries.ATTRIBUTE.getEntry(positiveAttributeIds.get(attributeCount)).get(),
                                new EntityAttributeModifier(Registries.ATTRIBUTE.getId(Registries.ATTRIBUTE.get(positiveAttributeIds.get(attributeCount))), positiveAttributeValues.get(attributeCount), Operation.valueOf(positiveAttributeOperations.get(attributeCount).toUpperCase())));
                        list.add(attributeModifiers);
                    }
                    attributeCount++;
                }

                positiveEffectMap.put(i, list);
            }

            HashMap<Integer, List<Object>> negativeEffectMap = new HashMap<>();

            effectCount = 0;
            attributeCount = 0;
            for (int i = 0; i < negativeEffectCount.size() / 2; i++) {
                List<Object> list = new ArrayList<>();

                for (int u = 0; u < negativeEffectCount.get(i * 2); u++) {
                    if (Registries.STATUS_EFFECT.get(negativeEffectIds.get(effectCount)) != null) {
                        list.add(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(negativeEffectIds.get(effectCount)).get(), negativeEffectDurations.get(effectCount), negativeEffectAmplifiers.get(effectCount), false, false, true));
                    }
                    effectCount++;
                }

                for (int u = 0; u < negativeEffectCount.get(i * 2 + 1); u++) {
                    if (Registries.ATTRIBUTE.get(negativeAttributeIds.get(attributeCount)) != null) {
                        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                        attributeModifiers.put(Registries.ATTRIBUTE.getEntry(negativeAttributeIds.get(attributeCount)).get(),
                                new EntityAttributeModifier(Registries.ATTRIBUTE.getId(Registries.ATTRIBUTE.get(negativeAttributeIds.get(attributeCount))), negativeAttributeValues.get(attributeCount), EntityAttributeModifier.Operation.valueOf(negativeAttributeOperations.get(attributeCount).toUpperCase())));
                        list.add(attributeModifiers);
                    }
                    attributeCount++;
                }

                negativeEffectMap.put(i, list);
            }

            context.client().execute(() -> {
                NutritionMain.NUTRITION_POSITIVE_EFFECTS.clear();
                NutritionMain.NUTRITION_NEGATIVE_EFFECTS.clear();
                positiveEffectMap.forEach(NutritionMain.NUTRITION_POSITIVE_EFFECTS::put);
                negativeEffectMap.forEach(NutritionMain.NUTRITION_NEGATIVE_EFFECTS::put);
            });
        });
    }

    public static void writeC2SNutritionPacket() {
        ClientPlayNetworking.send(new NutritionSyncPacket());
    }
}
