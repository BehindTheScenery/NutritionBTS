package dev.behindthescenery.nutritionbts.mixin;

import dev.behindthescenery.nutritionbts.access.HungerManagerAccess;
import dev.behindthescenery.nutritionbts.data.NutritionTypeLoader;
import dev.behindthescenery.nutritionbts.init.ConfigInit;
import dev.behindthescenery.nutritionbts.nutrition.NutritionType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

//@SuppressWarnings("ALL")
@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(HungerManager.class)
public class HungerManagerMixin implements HungerManagerAccess {
    // Needs to be serialized
    @Unique
    private final Map<NutritionType, Integer> nutritions = new HashMap<>();

    @Inject(method = "update", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(II)I", ordinal = 0))
    private void updateNutritionMixin(PlayerEntity player, CallbackInfo info) {
        NutritionTypeLoader.INSTANCE.getLoaded().forEach(type -> decrementNutritionLevel(type, 1));
    }

    @Unique
    private void applyEffects(PlayerEntity player, @NotNull Collection<StatusEffectInstance> effects) {
        for (StatusEffectInstance instance : effects) {
            if (!player.hasStatusEffect(instance.getEffectType()) || player.getStatusEffect(instance.getEffectType()).getDuration() < instance.getDuration() - 50) {
                player.addStatusEffect(new StatusEffectInstance(instance));
            }
        }
    }

    @Unique
    private List<EntityAttributeInstance> applyAttributeModifiers(PlayerEntity player, @NotNull Map<Identifier, EntityAttributeModifier> map) {
        List<EntityAttributeInstance> modifiedAttributes = new ArrayList<>();
        for (Map.Entry<Identifier, EntityAttributeModifier> entry : map.entrySet()) {
            Registries.ATTRIBUTE.getEntry(entry.getKey()).ifPresent(attribute -> {
                EntityAttributeInstance instance = player.getAttributeInstance(attribute);
                if (instance == null || instance.hasModifier(entry.getValue().id())) return;
                instance.addPersistentModifier(entry.getValue());
                modifiedAttributes.add(instance);
            });
        }
        return modifiedAttributes;
    }

    @Unique
    private List<EntityAttributeInstance> tryRemoveAttributeModifiers(PlayerEntity player, @NotNull Map<Identifier, EntityAttributeModifier> map) {
        List<EntityAttributeInstance> modifiedAttributes = new ArrayList<>();
        for (Map.Entry<Identifier, EntityAttributeModifier> entry : map.entrySet()) {
            Registries.ATTRIBUTE.getEntry(entry.getKey()).ifPresent(attribute -> {
                EntityAttributeInstance instance = player.getAttributeInstance(attribute);
                if (instance == null || !instance.hasModifier(entry.getValue().id())) return;
                instance.removeModifier(entry.getValue());
                modifiedAttributes.add(instance);
            });
        }
        return modifiedAttributes;
    }

    @Inject(method = "update", at = @At("TAIL"))
    private void updateNutritionEffectsMixin(@NotNull PlayerEntity player, CallbackInfo info) {
        if (player.isCreative() || player.getWorld().getTime() % 20 != 0) return;

        List<EntityAttributeInstance> modifiedAttributes = new ArrayList<>();

        for (NutritionType type : NutritionTypeLoader.INSTANCE.getLoaded()) {
            if (!nutritions.containsKey(type)) nutritions.put(type, ConfigInit.CONFIG.maxNutrition / 2);

            int nutritionLevel = nutritions.get(type);

            if (nutritionLevel <= ConfigInit.CONFIG.negativeNutrition) {
                applyEffects(player, type.negativeEffects());
                modifiedAttributes.addAll(applyAttributeModifiers(player, type.negativeAttributeModifiers()));
            }
            else modifiedAttributes.addAll(tryRemoveAttributeModifiers(player, type.negativeAttributeModifiers()));

            if (nutritionLevel >= ConfigInit.CONFIG.positiveNutrition) {
                applyEffects(player, type.positiveEffects());
                modifiedAttributes.addAll(applyAttributeModifiers(player, type.positiveAttributeModifiers()));
            }
            else modifiedAttributes.addAll(tryRemoveAttributeModifiers(player, type.positiveAttributeModifiers()));
        }

        if (!modifiedAttributes.isEmpty()) ((ServerPlayerEntity) player).networkHandler.send(new EntityAttributesS2CPacket(player.getId(), modifiedAttributes));
    }

    @Inject(method = "addExhaustion", at = @At("TAIL"))
    private void addExhaustionMixin(float exhaustion, CallbackInfo info) {
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readNbtMixin(NbtCompound nbt, CallbackInfo info) {
        nbt.getList("Nutritions", NbtElement.COMPOUND_TYPE).forEach(e -> {
            NbtCompound compound = (NbtCompound) e;
            try {
                nutritions.put(NutritionType.byId(Identifier.of(compound.getString("Key"))), compound.getInt("Value"));
            } catch (IllegalArgumentException ignored) {
            }
        });
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeNbtMixin(NbtCompound nbt, CallbackInfo info) {
        NbtList list = new NbtList();
        nutritions.forEach((k, v) -> {
            NbtCompound compound = new NbtCompound();
            compound.putString("Key", k.id().toString());
            compound.putInt("Value", v);
            list.add(compound);
        });
        nbt.put("Nutritions", list);
    }

    @Override
    public void addNutritionLevel(NutritionType type, int level) {
        nutritions.put(type, Math.min(nutritions.getOrDefault(type, 0) + level, ConfigInit.CONFIG.maxNutrition));
    }

    @Override
    public void decrementNutritionLevel(NutritionType type, int level) {
        nutritions.put(type, Math.max(nutritions.getOrDefault(type, 0) - level, 0));
    }

    @Override
    public void setNutritionLevel(NutritionType type, int level) {
        nutritions.put(type, level);
    }

    @Override
    public int getNutritionLevel(NutritionType type) {
        return nutritions.getOrDefault(type, 0);
    }

    @Override
    public Map<NutritionType, Integer> getNutritionLevels() {
        return Map.copyOf(nutritions);
    }

    @Override
    public void setNutritionLevels(Map<NutritionType, Integer> levels) {
        nutritions.putAll(levels);
    }
}
