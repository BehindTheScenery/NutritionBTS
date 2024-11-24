package dev.behindthescenery.nutritionbts.screen;

import com.google.common.collect.Multimap;
import dev.behindthescenery.nutritionbts.NutritionMain;
import dev.behindthescenery.nutritionbts.access.HungerManagerAccess;
import dev.behindthescenery.nutritionbts.init.ConfigInit;
import dev.behindthescenery.nutritionbts.init.RenderInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class NutritionScreen extends Screen {

    private final List<ItemStack> nutritionItems = List.of(new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.carbohydrateItemId))),
        new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.proteinItemId))), new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.fatItemId))),
        new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.vitaminItemId))), new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.mineralItemId))));
    private final List<Text> nutritionTexts = List.of(Text.translatable("screen.nutritionbts.carbohydrates"), Text.translatable("screen.nutritionbts.protein"), Text.translatable("screen.nutritionbts.fat"),
        Text.translatable("screen.nutritionbts.vitamins"), Text.translatable("screen.nutritionbts.minerals"));
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

    @SuppressWarnings("unchecked")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawTexture(RenderInit.NUTRITION_ICONS, this.x, this.y, 0, 0, 176, 142);
        context.drawText(this.textRenderer, this.title, this.x + 176 / 2 - this.textRenderer.getWidth(this.title) / 2, this.y + 7, 0x3F3F3F, false);
        int extraY = 0;
        int extraBarY = 0;
        for (int i = 0; i < this.nutritionItems.size(); i++) {
            context.drawItem(this.nutritionItems.get(i), this.x + 7, this.y + 25 + extraY);
            context.drawText(this.textRenderer, this.nutritionTexts.get(i), this.x + 28, this.y + 26 + extraY, 0x3F3F3F, false);
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 206 + extraBarY, 141, 5);
            if (this.hungerManagerAccess != null) {
                if (this.hungerManagerAccess.getNutritionLevel(i) > 0) {
                    context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 211 + extraBarY,
                        140 * this.hungerManagerAccess.getNutritionLevel(i) / ConfigInit.CONFIG.maxNutrition, 5);
                }
                context.drawText(this.textRenderer, Text.translatable("screen.nutritionbts.nutritionValue", this.hungerManagerAccess.getNutritionLevel(i), ConfigInit.CONFIG.maxNutrition), this.x + 127,
                    this.y + 26 + extraY, 0x3F3F3F, false);
                List<Text> tooltips = new ArrayList<>();
                if (isPointWithinBounds(27, 36 + extraY, 31, 5, mouseX, mouseY)) {
                    if (NutritionMain.NUTRITION_NEGATIVE_EFFECTS.containsKey(i)) {
                        NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(i).forEach(effect -> {
                            if (effect instanceof StatusEffectInstance statusEffectInstance) {
                                tooltips.add(statusEffectInstance.getEffectType().value().getName());
                            } else {
                                Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> map = (Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier>) effect;
                                map.forEach((attribute, modifier) -> {
                                    tooltips.add(Text.translatable(attribute.value().getTranslationKey()));
                                });
                            }
                        });
                    }

                } else if (isPointWithinBounds(137, 36 + extraY, 31, 5, mouseX, mouseY)) {
                    if (NutritionMain.NUTRITION_POSITIVE_EFFECTS.containsKey(i)) {
                        NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i).forEach(effect -> {
                            if (effect instanceof StatusEffectInstance statusEffectInstance) {
                                tooltips.add(statusEffectInstance.getEffectType().value().getName());
                            } else {
                                Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> map = (Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier>) effect;
                                map.forEach((attribute, modifier) -> {
                                    tooltips.add(Text.translatable(attribute.value().getTranslationKey()));
                                });
                            }
                        });
                    }
                }
                if (!tooltips.isEmpty()) {
                    context.drawTooltip(textRenderer, tooltips, mouseX, mouseY);
                }
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
            this.client.setScreen(new InventoryScreen(this.client.player));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
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
