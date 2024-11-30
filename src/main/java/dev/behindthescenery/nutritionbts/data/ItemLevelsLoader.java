package dev.behindthescenery.nutritionbts.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.behindthescenery.nutritionbts.NutritionMain;
import dev.behindthescenery.nutritionbts.util.FileLoader;
import dev.behindthescenery.nutritionbts.util.ItemLevels;
import net.minecraft.SharedConstants;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ItemLevelsLoader extends FileLoader {
    public static final ItemLevelsLoader INSTANCE = new ItemLevelsLoader("nutrition");

    private final List<ItemLevels> itemLevels = new ArrayList<>();

    public ItemLevelsLoader(String directoryName) {
        super(directoryName);
    }

    @Override
    protected void resolveFile(@NotNull JsonElement element) {
        DataResult<ItemLevels> result = ItemLevels.CODEC.parse(JsonOps.INSTANCE, element).ifError(itemLevelsError -> { if (SharedConstants.isDevelopment) NutritionMain.LOGGER.error(itemLevelsError); });
        if (result.isError()) return;
        ItemLevels levels = result.getOrThrow();

        itemLevels.add(levels);
        itemLevels.sort(Comparator.comparingInt(ItemLevels::replacePriority));
    }

    @Unmodifiable
    public List<ItemLevels> getItemLevels() {
        return List.copyOf(itemLevels);
    }

    @Override
    protected void preInit() {
        itemLevels.clear();
    }

    public boolean containsKey(Item item) {
        for (ItemLevels levels : itemLevels) if (levels.levels().containsKey(item)) return true;
        return false;
    }

    public Map<Identifier, Integer> get(Item item) {
        for (ItemLevels levels : itemLevels) if (levels.levels().containsKey(item)) return levels.levels().get(item);
        return Map.of();
    }
}
