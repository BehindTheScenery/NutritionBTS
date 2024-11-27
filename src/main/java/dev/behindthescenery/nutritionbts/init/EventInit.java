package dev.behindthescenery.nutritionbts.init;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

public class EventInit {

    public static void init() {
        // sorry'anchik, in future maybe
//        if (FabricLoader.getInstance().isModLoaded("dehydration")) {
//            DrinkEvent.EVENT.register((ItemStack stack, PlayerEntity player) -> {
//                if (!player.getWorld().isClient()) {
//                    Item item = stack.getItem();
//                    if (NutritionMain.NUTRITION_ITEM_MAP.containsKey(item)) {
//                        for (int i = 0; i < NutritionMain.NUTRITION_ITEM_MAP.get(item).size(); i++) {
//                            if (NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i) > 0) {
//                                ((HungerManagerAccess) player.getHungerManager()).addNutritionLevel(i, NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i));
//                            }
//                        }
//                    }
//                }
//            });
//        }
        // datapacks
        if (FabricLoader.getInstance().isModLoaded("adventurez")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "adventurez_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betterend")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "betterend_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betternether")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "betternether_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("cornexpansion")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "cornexpansion_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("croptopia")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "croptopia_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "dehydration_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("vinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "dehydration_x_vinery_nutrition_compat"),
                FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farm_and_charm")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "farm_and_charm_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "farmersdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("fishofthieves")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "fishofthieves_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("livingthings")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "livingthings_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("moredelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "moredelight_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("natures_spirit")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "natures_spirit_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("oceansdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "oceansdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("the_bumblezone")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "the_bumblezone_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("ubesdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "ubesdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("vinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "vinery_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "wilderwild_nutrition_compat"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                ResourcePackActivationType.DEFAULT_ENABLED);
        }
    }

}
