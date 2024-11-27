package dev.behindthescenery.nutritionbts.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.behindthescenery.nutritionbts.NutritionMain;
import dev.behindthescenery.nutritionbts.nutrition.NutritionType;
import dev.behindthescenery.nutritionbts.util.FileLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;

public class ItemLevelsLoader extends FileLoader {
    public static final ItemLevelsLoader INSTANCE = new ItemLevelsLoader("nutrition");

    public static final Codec<Map<Item, Map<Identifier, Integer>>> CODEC =
        Codec.unboundedMap(ItemStack.ITEM_CODEC.xmap(RegistryEntry::value, Registries.ITEM::getEntry),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT));
    private final Map<Item, Map<Identifier, Integer>> itemLevels = new HashMap<>();

    public ItemLevelsLoader(String directoryName) {
        super(directoryName);
    }

    @Override
    protected void resolveFile(@NotNull JsonElement element) {
        DataResult<Map<Item, Map<Identifier, Integer>>> result = CODEC.parse(JsonOps.INSTANCE, element).ifError(NutritionMain.LOGGER::error);
        if (result.isError()) return;

        itemLevels.putAll(result.getOrThrow());
    }

    @Unmodifiable
    public Map<Item, Map<Identifier, Integer>> getItemLevels() {
        return Map.copyOf(itemLevels);
    }

    @Override
    protected void preInit() {
        itemLevels.clear();
    }
}
