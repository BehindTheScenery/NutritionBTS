package dev.behindthescenery.nutritionbts.screen;

import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.client.render.*;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

@Environment(EnvType.CLIENT)
public class NutritionScreen extends Screen {
    public static final int TEXTURE_WIDTH = 256;
    public static final int TEXTURE_HEIGHT = 256;
    public static final int START_HEIGHT = 20;
    public static final int BARS_GAP = 23;
    public static final int SCREEN_WIDTH = 176;
    public static final int BORDERS = 4;
    public static final int BOTTOM_EXTRA = 4;

    private boolean heightDirty = true;
    private int screenHeight, x, y;

    @Nullable
    private HungerManagerAccess hungerManagerAccess = null;

    public NutritionScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.hungerManagerAccess = this.client != null && this.client.player != null ? (HungerManagerAccess) this.client.player.getHungerManager() : null;

        if (hungerManagerAccess == null) return;

//        this.x = (this.width - SCREEN_WIDTH) / 2;
//        this.y = (this.height - SCREEN_HEIGHT) / 2;
        // 178 - 20 = 158
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (heightDirty) {
            screenHeight = START_HEIGHT + BARS_GAP * hungerManagerAccess.getNutritionLevels().keySet().stream().filter(NutritionTypeLoader.INSTANCE.getLoaded()::contains).toList().size();
            x = (width - SCREEN_WIDTH) / 2;
            y = (height - screenHeight) / 2;
            heightDirty = false;
        }

        context.drawTexture(RenderInit.NUTRITION_ICONS, x, this.y, 0, 0, SCREEN_WIDTH, BORDERS);
        for (int i = 0; i < screenHeight - BORDERS + BOTTOM_EXTRA; i++) context.drawTexture(RenderInit.NUTRITION_ICONS, this.x, this.y + 4 + i, 0, BORDERS, SCREEN_WIDTH, 1);
        context.drawTexture(RenderInit.NUTRITION_ICONS, x, this.y + screenHeight + BOTTOM_EXTRA, 0, 5, SCREEN_WIDTH, 4);

        context.drawText(this.textRenderer, this.title, this.x + 176 / 2 - this.textRenderer.getWidth(this.title) / 2, this.y + 7, 0x3F3F3F, false);
        if (hungerManagerAccess == null) return;
        int extraY = 0;

        for (NutritionType type : NutritionTypeLoader.INSTANCE.getLoaded()) {
            int nutritionLevel = hungerManagerAccess.getNutritionLevel(type);
            final int extraYF = extraY;
            type.icon().ifPresent(icon -> context.drawItem(icon.value().getDefaultStack(), this.x + 7, this.y + 25 + extraYF));
            context.drawText(textRenderer, type.tooltip(), this.x + 28, this.y + 26 + extraY, 0x3f3f3f, false);
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 206, 141, 5);

            if (hungerManagerAccess.getNutritionLevel(type) > 0) {
                coloredDraw(context, RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 211, 140 * nutritionLevel / ConfigInit.CONFIG.maxNutrition, 5, type.color());
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
            extraY += BARS_GAP;
        }
        if (isPointWithinBounds(5, 5, 11, 10, mouseX, mouseY)) {
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 5, this.y + 5, 187, 0, 11, 10);
        } else {
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 5, this.y + 5, 176, 0, 11, 10);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void coloredDraw(DrawContext context, Identifier nutritionIcons, int x, int y, int u, int v, int width, int height, int color) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderTexture(0, nutritionIcons);
        int red = ColorHelper.Argb.getRed(color);
        int green = ColorHelper.Argb.getGreen(color);
        int blue = ColorHelper.Argb.getBlue(color);
        int x2 = x + width;
        int y2 = y + height;
        float minU = (u + 0.0F) / (float) TEXTURE_WIDTH;
        float maxU = (u + (float) width) / (float) TEXTURE_WIDTH;
        float minV = (v + 0.0F) / (float) TEXTURE_HEIGHT;
        float maxV = (v + (float) height) / (float) TEXTURE_HEIGHT;
        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferbuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferbuilder.vertex(matrix, (float) x, (float) y2, 0).color(red, green, blue, 255)
            .texture(minU, maxV);
        bufferbuilder.vertex(matrix, (float) x2, (float) y2, 0).color(red, green, blue, 255)
            .texture(maxU, maxV);
        bufferbuilder.vertex(matrix, (float) x2, (float) y, 0).color(red, green, blue, 255)
            .texture(maxU, minV);
        bufferbuilder.vertex(matrix, (float) x, (float) y, 0).color(red, green, blue, 255)
            .texture(minU, minV);
        BufferRenderer.drawWithGlobalProgram(bufferbuilder.end());
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

    public void markHeightDirty() {
        heightDirty = true;
    }
}
