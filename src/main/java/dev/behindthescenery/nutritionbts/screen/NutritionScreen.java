package dev.behindthescenery.nutritionbts.screen;

import dev.behindthescenery.nutritionbts.access.HungerManagerAccess;
import dev.behindthescenery.nutritionbts.data.NutritionTypeLoader;
import dev.behindthescenery.nutritionbts.init.ConfigInit;
import dev.behindthescenery.nutritionbts.init.RenderInit;
import dev.behindthescenery.nutritionbts.nutrition.NutritionType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

@Environment(EnvType.CLIENT)
public class NutritionScreen extends Screen {
    private int x;
    private int y;
    @Nullable
    private HungerManagerAccess hungerManagerAccess = null;

    public NutritionScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.x = this.width / 2 - (176 / 2);
        this.y = this.height / 2 - (141 / 2);
        this.hungerManagerAccess = this.client != null && this.client.player != null ? (HungerManagerAccess) this.client.player.getHungerManager() : null;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawTexture(RenderInit.NUTRITION_ICONS, this.x, this.y, 0, 0, 176, 142);
        context.drawText(this.textRenderer, this.title, this.x + 176 / 2 - this.textRenderer.getWidth(this.title) / 2, this.y + 7, 0x3F3F3F, false);
        if (hungerManagerAccess == null) return;
        int extraY = 0;
        int extraBarY = 0;

        for (NutritionType type : NutritionTypeLoader.INSTANCE.getLoaded()) {
            int nutritionLevel = hungerManagerAccess.getNutritionLevel(type);
            final int extraYF = extraY;
            type.icon().ifPresent(icon -> context.drawItem(icon.value().getDefaultStack(), this.x + 7, this.y + 25 + extraYF));
            context.drawText(textRenderer, type.tooltip(), this.x + 28, this.y + 26 + extraY, 0x3f3f3f, false);
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 206 + extraBarY, 141, 5);

            if (hungerManagerAccess.getNutritionLevel(type) > 0) {
                context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 211 + extraBarY,
                    140 * nutritionLevel / ConfigInit.CONFIG.maxNutrition, 5);
            }
            context.drawText(this.textRenderer, Text.translatable("screen." + MOD_ID + ".nutritionValue", nutritionLevel, ConfigInit.CONFIG.maxNutrition), this.x + 127, this.y + 26 + extraY, 0x3F3F3F, false);

            List<Text> tooltips = new ArrayList<>();
            if (isPointWithinBounds(27, 36 + extraY, 31, 5, mouseX, mouseY)) {
                type.negativeEffects().forEach(effect -> tooltips.add(effect.getEffectType().value().getName()));
                type.negativeAttributeModifiers().forEach((attribute, modifier) -> tooltips.add(Text.translatable(Registries.ATTRIBUTE.get(attribute).getTranslationKey())));
            }
            else if (isPointWithinBounds(137, 36 + extraY, 31, 5, mouseX, mouseY)) {
                type.positiveEffects().forEach(effect -> tooltips.add(effect.getEffectType().value().getName()));
                type.positiveAttributeModifiers().forEach((attribute, modifier) -> tooltips.add(Text.translatable(Registries.ATTRIBUTE.get(attribute).getTranslationKey())));
            }
            if (!tooltips.isEmpty()) {
                context.drawTooltip(textRenderer, tooltips, mouseX, mouseY);
            }
            extraY += 23;
            extraBarY += 10;
        }
        if (isPointWithinBounds(5, 5, 11, 10, mouseX, mouseY)) {
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 5, this.y + 5, 187, 0, 11, 10);
        } else {
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 5, this.y + 5, 176, 0, 11, 10);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isPointWithinBounds(5, 5, 11, 10, mouseX, mouseY)) {
            if (this.client != null && this.client.player != null) {
                this.client.setScreen(new InventoryScreen(this.client.player));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client != null && this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        int i = this.x;
        int j = this.y;
        return (pointX -= i) >= (double) (x - 1) && pointX < (double) (x + width + 1) && (pointY -= j) >= (double) (y - 1) && pointY < (double) (y + height + 1);
    }

}
