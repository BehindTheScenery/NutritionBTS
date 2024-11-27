package dev.behindthescenery.nutritionbts.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Map;

public record ItemLevels(int replacePriority, Map<Item, Map<Identifier, Integer>> levels) {
    public static final Codec<ItemLevels> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Codec.INT.optionalFieldOf("replacePriority", 0).forGetter(ItemLevels::replacePriority),
        Codec.unboundedMap(ItemStack.ITEM_CODEC.xmap(RegistryEntry::value, Registries.ITEM::getEntry), Codec.unboundedMap(Identifier.CODEC, Codec.INT)).optionalFieldOf("values", Map.of()).forGetter(ItemLevels::levels)
    ).apply(inst, ItemLevels::new));
}
