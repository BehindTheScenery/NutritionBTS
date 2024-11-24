package dev.behindthescenery.nutritionbts.mixin;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import dev.behindthescenery.nutritionbts.NutritionMain;
import dev.behindthescenery.nutritionbts.access.HungerManagerAccess;
import dev.behindthescenery.nutritionbts.init.ConfigInit;

@Mixin(HungerManager.class)
public class HungerManagerMixin implements HungerManagerAccess {

    private int carbohydrateLevel = ConfigInit.CONFIG.maxNutrition / 2;
    private int proteinLevel = ConfigInit.CONFIG.maxNutrition / 2;
    private int fatLevel = ConfigInit.CONFIG.maxNutrition / 2;
    private int vitaminLevel = ConfigInit.CONFIG.maxNutrition / 2;
    private int mineralLevel = ConfigInit.CONFIG.maxNutrition / 2;
    private Map<Integer, Boolean> effectMap = new HashMap<Integer, Boolean>() {
        {
            put(0, false);
            put(1, false);
            put(2, false);
            put(3, false);
            put(4, false);
        }
    };
    // Unused
    private boolean shouldUpdateNutritions = false;

    @Shadow
    private int foodTickTimer;

    @Inject(method = "update", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(II)I", ordinal = 0))
    private void updateNutritionMixin(PlayerEntity player, CallbackInfo info) {
        decrementNutritionLevel(0, 1);
        decrementNutritionLevel(1, 1);
        decrementNutritionLevel(2, 1);
        decrementNutritionLevel(3, 1);
        decrementNutritionLevel(4, 1);
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void updateMixin(PlayerEntity player, CallbackInfo info) {
        if (this.foodTickTimer % 5 == 0 && this.shouldUpdateNutritions) {
            this.shouldUpdateNutritions = false;
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "update", at = @At("TAIL"))
    private void updateNutritionEffectsMixin(PlayerEntity player, CallbackInfo info) {
        if (!player.isCreative() && player.getWorld().getTime() % 20 == 0) {
            boolean changedAttributes = false;
            List<Integer> list = List.of(this.carbohydrateLevel, this.proteinLevel, this.fatLevel, this.vitaminLevel, this.mineralLevel);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) <= ConfigInit.CONFIG.negativeNutrition) {
                    List<Object> negativeEffectList = NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(i);
                    if (negativeEffectList != null && !negativeEffectList.isEmpty()) {
                        for (int u = 0; u < negativeEffectList.size(); u++) {
                            if (negativeEffectList.get(u) instanceof StatusEffectInstance statusEffectInstance) {
                                if (!player.hasStatusEffect(statusEffectInstance.getEffectType())
                                        || player.getStatusEffect(statusEffectInstance.getEffectType()).getDuration() < statusEffectInstance.getDuration() - 50) {
                                    player.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
                                }
                            } else if (!this.effectMap.get(i) && negativeEffectList.get(u) instanceof Multimap multimap) {
                                player.getAttributes().addTemporaryModifiers(multimap);
                                changedAttributes = true;
                            }
                        }
                        this.effectMap.put(i, true);
                    }
                } else if (list.get(i) >= ConfigInit.CONFIG.positiveNutrition) {
                    List<Object> positiveEffectList = NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i);
                    if (positiveEffectList != null && !positiveEffectList.isEmpty()) {
                        for (int u = 0; u < positiveEffectList.size(); u++) {
                            if (positiveEffectList.get(u) instanceof StatusEffectInstance statusEffectInstance) {
                                if (!player.hasStatusEffect(statusEffectInstance.getEffectType())
                                        || player.getStatusEffect(statusEffectInstance.getEffectType()).getDuration() < statusEffectInstance.getDuration() - 50) {
                                    player.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
                                }
                            } else if (!this.effectMap.get(i) && positiveEffectList.get(u) instanceof Multimap multimap) {
                                player.getAttributes().addTemporaryModifiers(multimap);
                                changedAttributes = true;
                            }
                        }
                        this.effectMap.put(i, true);
                    }
                } else {
                    if (this.effectMap.get(i)) {
                        this.effectMap.put(i, false);
                        List<Object> positiveEffectList = NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i);
                        for (int u = 0; u < positiveEffectList.size(); u++) {
                            if (positiveEffectList.get(u) instanceof Multimap multimap) {
                                player.getAttributes().removeModifiers(multimap);
                                changedAttributes = true;

                            }
                        }
                        List<Object> negativeEffectList = NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(i);
                        for (int u = 0; u < negativeEffectList.size(); u++) {
                            if (negativeEffectList.get(u) instanceof Multimap multimap) {
                                player.getAttributes().removeModifiers(multimap);
                                changedAttributes = true;
                            }
                        }
                    }
                }
            }
            if (changedAttributes) {
                Collection<EntityAttributeInstance> collection = player.getAttributes().getAttributesToSend();
                if (!collection.isEmpty()) {
                    ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityAttributesS2CPacket(player.getId(), collection));
                }
            }
        }
    }

    @Inject(method = "addExhaustion", at = @At("TAIL"))
    private void addExhaustionMixin(float exhaustion, CallbackInfo info) {
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readNbtMixin(NbtCompound nbt, CallbackInfo info) {
        this.carbohydrateLevel = nbt.getInt("CarbohydrateLevel");
        this.proteinLevel = nbt.getInt("ProteinLevel");
        this.fatLevel = nbt.getInt("FatLevel");
        this.vitaminLevel = nbt.getInt("VitaminLevel");
        this.mineralLevel = nbt.getInt("MineralLevel");
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeNbtMixin(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("CarbohydrateLevel", this.carbohydrateLevel);
        nbt.putInt("ProteinLevel", this.proteinLevel);
        nbt.putFloat("FatLevel", this.fatLevel);
        nbt.putFloat("VitaminLevel", this.vitaminLevel);
        nbt.putFloat("MineralLevel", this.mineralLevel);
    }

    @Override
    public void addNutritionLevel(int type, int level) {
        if (type == 0) {
            this.carbohydrateLevel = (this.carbohydrateLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.carbohydrateLevel + level);
        } else if (type == 1) {
            this.proteinLevel = (this.proteinLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.proteinLevel + level);
        } else if (type == 2) {
            this.fatLevel = (this.fatLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.fatLevel + level);
        } else if (type == 3) {
            this.vitaminLevel = (this.vitaminLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.vitaminLevel + level);
        } else if (type == 4) {
            this.mineralLevel = (this.mineralLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.mineralLevel + level);
        }
        this.shouldUpdateNutritions = true;
    }

    @Override
    public void decrementNutritionLevel(int type, int level) {
        if (type == 0) {
            this.carbohydrateLevel = (this.carbohydrateLevel - level < 0) ? 0 : (this.carbohydrateLevel - level);
        } else if (type == 1) {
            this.proteinLevel = (this.proteinLevel - level < 0) ? 0 : (this.proteinLevel - level);
        } else if (type == 2) {
            this.fatLevel = (this.fatLevel - level < 0) ? 0 : (this.fatLevel - level);
        } else if (type == 3) {
            this.vitaminLevel = (this.vitaminLevel - level < 0) ? 0 : (this.vitaminLevel - level);
        } else if (type == 4) {
            this.mineralLevel = (this.mineralLevel - level < 0) ? 0 : (this.mineralLevel - level);
        }
        this.shouldUpdateNutritions = true;
    }

    @Override
    public void setNutritionLevel(int type, int level) {
        if (type == 0) {
            this.carbohydrateLevel = level;
        } else if (type == 1) {
            this.proteinLevel = level;
        } else if (type == 2) {
            this.fatLevel = level;
        } else if (type == 3) {
            this.vitaminLevel = level;
        } else if (type == 4) {
            this.mineralLevel = level;
        }
        this.shouldUpdateNutritions = true;
    }

    @Override
    public int getNutritionLevel(int type) {
        switch (type) {
        case 0:
            return this.carbohydrateLevel;
        case 1:
            return this.proteinLevel;
        case 2:
            return this.fatLevel;
        case 3:
            return this.vitaminLevel;
        case 4:
            return this.mineralLevel;
        default:
            return 0;
        }
    }

}
