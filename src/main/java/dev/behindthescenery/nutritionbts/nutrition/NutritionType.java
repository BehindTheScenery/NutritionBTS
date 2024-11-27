package dev.behindthescenery.nutritionbts.nutrition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.nutritionbts.data.NutritionTypeLoader;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record NutritionType(Identifier id, Optional<RegistryEntry<Item>> icon,
                            MutableText tooltip, List<StatusEffectInstance> positiveEffects,
                            List<StatusEffectInstance> negativeEffects,
                            Map<Identifier, EntityAttributeModifier> positiveAttributeModifiers,
                            Map<Identifier, EntityAttributeModifier> negativeAttributeModifiers) {
    public static final Codec<NutritionType> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Identifier.CODEC.fieldOf("id").forGetter(NutritionType::id),
        ItemStack.ITEM_CODEC.xmap(Optional::ofNullable, opt -> opt.orElse(null)).fieldOf("icon").forGetter(NutritionType::icon),
        Codec.STRING.fieldOf("tooltip").xmap(Text::translatable, text -> text.getContent() instanceof TranslatableTextContent content ? content.getKey() : text.getString()).orElse(Text.empty()).forGetter(NutritionType::tooltip),
        StatusEffectInstance.CODEC.listOf().fieldOf("positiveEffects").orElse(List.of()).forGetter(NutritionType::positiveEffects),
        StatusEffectInstance.CODEC.listOf().fieldOf("negativeEffects").orElse(List.of()).forGetter(NutritionType::negativeEffects),
        Codec.unboundedMap(Identifier.CODEC, EntityAttributeModifier.CODEC).fieldOf("positiveAttributes").orElse(Map.of()).forGetter(NutritionType::positiveAttributeModifiers),
        Codec.unboundedMap(Identifier.CODEC, EntityAttributeModifier.CODEC).fieldOf("negativeAttributes").orElse(Map.of()).forGetter(NutritionType::negativeAttributeModifiers)
    ).apply(inst, NutritionType::new));

    public static final PacketCodec<RegistryByteBuf, NutritionType> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public static @NotNull NutritionType byId(Identifier id) {
        for (NutritionType type : NutritionTypeLoader.INSTANCE.getLoaded())
            if (Objects.equals(type.id, id)) return type;
        throw new IllegalArgumentException();
    }
}
