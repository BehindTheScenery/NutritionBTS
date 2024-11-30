package dev.behindthescenery.nutritionbts.mixin;

import dev.behindthescenery.nutritionbts.access.HungerManagerAccess;
import dev.behindthescenery.nutritionbts.data.ItemLevelsLoader;
import dev.behindthescenery.nutritionbts.network.NutritionServerPacketHandler;
import dev.behindthescenery.nutritionbts.nutrition.NutritionType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void eatFoodMixin(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> info) {
        if (world.isClient) return;
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (!ItemLevelsLoader.INSTANCE.containsKey(stack.getItem())) return;

        for (Map.Entry<Identifier, Integer> levels : ItemLevelsLoader.INSTANCE.get(stack.getItem()).entrySet()) {
            if (levels.getValue() > 0) {
                try {
                    ((HungerManagerAccess) getHungerManager()).addNutritionLevel(NutritionType.byId(levels.getKey()), levels.getValue());
                    NutritionServerPacketHandler.writeS2CNutritionPacket((ServerPlayerEntity) self, (HungerManagerAccess) getHungerManager());
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

    @Shadow
    public abstract HungerManager getHungerManager();
}
