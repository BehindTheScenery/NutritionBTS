package dev.behindthescenery.nutritionbts.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RenderInit {

    public static final Identifier NUTRITION_ICONS = Identifier.of("nutritionbts", "textures/gui/icons.png");

    public static void init() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
        });
    }

}
