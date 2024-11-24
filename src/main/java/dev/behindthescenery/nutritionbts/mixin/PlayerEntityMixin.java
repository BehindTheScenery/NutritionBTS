package dev.behindthescenery.nutritionbts.mixin;

import dev.behindthescenery.nutritionbts.NutritionMain;
import dev.behindthescenery.nutritionbts.access.HungerManagerAccess;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void eatFoodMixin(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> info) {
        if (NutritionMain.NUTRITION_ITEM_MAP.containsKey(stack.getItem())) {
            for (int i = 0; i < NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem()).size(); i++) {
                if (NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem()).get(i) > 0) {
                    ((HungerManagerAccess) getHungerManager()).addNutritionLevel(i, NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem()).get(i));
                }
            }
        }
    }

    @Shadow
    public abstract HungerManager getHungerManager();
}
