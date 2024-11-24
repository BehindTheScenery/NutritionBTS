package dev.behindthescenery.nutritionbts.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import dev.behindthescenery.nutritionbts.init.ConfigInit;
import dev.behindthescenery.nutritionbts.init.RenderInit;
import dev.behindthescenery.nutritionbts.network.NutritionClientPacket;
import dev.behindthescenery.nutritionbts.screen.NutritionScreen;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClickedMixin(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> info) {
        if (this.client != null && this.focusedSlot == null && this.isPointWithinBounds(ConfigInit.CONFIG.posX, ConfigInit.CONFIG.posY, 9, 9, (double) mouseX, (double) mouseY)) {
            NutritionClientPacket.writeC2SNutritionPacket();
            this.client.setScreen(new NutritionScreen(Text.translatable("screen.nutritionbts")));
            info.setReturnValue(true);
        }
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    protected void drawBackgroundMixin(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
        if (this.isPointWithinBounds(ConfigInit.CONFIG.posX, ConfigInit.CONFIG.posY, 9, 9, (double) mouseX, (double) mouseY)) {
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + ConfigInit.CONFIG.posX, this.y + ConfigInit.CONFIG.posY, 185, 10, 9, 9);
            context.drawTooltip(this.textRenderer, Text.translatable("screen.nutritionbts"), mouseX, mouseY);
        } else {
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + ConfigInit.CONFIG.posX, this.y + ConfigInit.CONFIG.posY, 176, 10, 9, 9);
        }
    }

}
