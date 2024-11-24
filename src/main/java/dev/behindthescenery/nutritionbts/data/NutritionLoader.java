package dev.behindthescenery.nutritionbts.data;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.behindthescenery.nutritionbts.NutritionMain;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("LoggingSimilarMessage")
public class NutritionLoader implements SimpleSynchronousResourceReloadListener {

    public static final Logger LOGGER = LogManager.getLogger("NutritionBTS");

    private final List<String> nutritionList = List.of("carbohydrates", "protein", "fat", "vitamins", "minerals");
    private final List<Boolean> effectReplaceList = new ArrayList<>(List.of(false, false, false, false, false));
    // Map to store replacing bools
    private final HashMap<Item, Boolean> replaceList = new HashMap<>();

    private static void processEffects(JsonObject effectsJsonObject, HashMap<Integer, List<Object>> nutritionEffectsMap, int i) {
        List<Object> list = new ArrayList<>();

        for (String effectId : effectsJsonObject.keySet()) {
            Identifier effectIdentifier = Identifier.of(effectId);

            if (!Registries.STATUS_EFFECT.containsId(effectIdentifier) && !Registries.ATTRIBUTE.containsId(effectIdentifier)) {
                LOGGER.info("{} is not a valid status effect identifier nor attribute identifier", effectIdentifier);
                continue;
            }

            JsonObject effectJsonObject = effectsJsonObject.get(effectId).getAsJsonObject();
            if (Registries.STATUS_EFFECT.containsId(effectIdentifier)) {
                list.add(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(effectIdentifier).orElseThrow(), effectJsonObject.get("duration").getAsInt(),
                    effectJsonObject.has("amplifier") ? effectJsonObject.get("amplifier").getAsInt() : 0, false, false, true));
            } else {
                Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                attributeModifiers.put(Registries.ATTRIBUTE.getEntry(effectIdentifier).orElseThrow(), new EntityAttributeModifier(effectIdentifier,
                    effectJsonObject.get("value").getAsFloat(), Operation.valueOf(effectJsonObject.get("operation").getAsString().toUpperCase())));
                list.add(attributeModifiers);
            }
        }

        nutritionEffectsMap.put(i, list);
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of("nutritionbts", "loader");
    }

    @Override
    public void reload(ResourceManager manager) {

        manager.findResources("nutrition", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try {
                InputStream stream = resourceRef.getInputStream();
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                for (String itemId : data.keySet()) {
                    if (Registries.ITEM.get(Identifier.of(itemId)).toString().equals("air")) {
                        LOGGER.info("{} is not a valid item identifier", itemId);
                        continue;
                    }

                    JsonObject jsonObject = data.get(itemId).getAsJsonObject();
                    Item item = Registries.ITEM.get(Identifier.of(itemId));
                    if (replaceList.containsKey(item)) {
                        continue;
                    }
                    if (jsonObject.has("replace") && jsonObject.get("replace").getAsBoolean()) {
                        replaceList.put(item, true);
                    }
                    List<Integer> nutritionList = new ArrayList<>();
                    if (jsonObject.has("carbohydrates")) {
                        nutritionList.add(jsonObject.get("carbohydrates").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    if (jsonObject.has("protein")) {
                        nutritionList.add(jsonObject.get("protein").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    if (jsonObject.has("fat")) {
                        nutritionList.add(jsonObject.get("fat").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    if (jsonObject.has("vitamins")) {
                        nutritionList.add(jsonObject.get("vitamins").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    if (jsonObject.has("minerals")) {
                        nutritionList.add(jsonObject.get("minerals").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    NutritionMain.NUTRITION_ITEM_MAP.put(item, nutritionList);
                }

            } catch (Exception e) {
                LOGGER.error("Error occurred while loading resource {}. {}", id.toString(), e.toString());
            }
        });

        manager.findResources("nutrition_manager", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try {
                InputStream stream = resourceRef.getInputStream();
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                for (int i = 0; i < nutritionList.size(); i++) {
                    if (data.has(nutritionList.get(i))) {
                        JsonObject jsonObject = data.getAsJsonObject(nutritionList.get(i));

                        if (this.effectReplaceList.get(i)) {
                            continue;
                        }
                        if (JsonHelper.getBoolean(jsonObject, "replace", false)) {
                            this.effectReplaceList.set(i, true);
                        }

                        if (jsonObject.has("positive")) {
                            JsonObject positiveJsonObject = jsonObject.getAsJsonObject("positive");
                            processEffects(positiveJsonObject, NutritionMain.NUTRITION_POSITIVE_EFFECTS, i);
                        }
                        if (jsonObject.has("negative")) {
                            JsonObject negativeJsonObject = jsonObject.getAsJsonObject("negative");
                            processEffects(negativeJsonObject, NutritionMain.NUTRITION_NEGATIVE_EFFECTS, i);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred while loading resource {}. {}", id.toString(), e.toString());
            }
        });
    }

}
