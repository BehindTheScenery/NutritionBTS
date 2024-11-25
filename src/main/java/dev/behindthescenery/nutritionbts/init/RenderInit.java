package dev.behindthescenery.nutritionbts.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

@Environment(EnvType.CLIENT)
public class RenderInit {

    public static final Identifier NUTRITION_ICONS = Identifier.of(MOD_ID, "textures/gui/icons.png");

    public static void init() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
        });
    }

}
