package net.nutritionz.init;

import net.dehydration.api.DrinkEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.network.NutritionServerPacket;

public class EventInit {

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            NutritionServerPacket.writeS2CItemNutritionPacket(handler.getPlayer());
        });
        if (FabricLoader.getInstance().isModLoaded("dehydration")) {
            DrinkEvent.EVENT.register((ItemStack stack, PlayerEntity player) -> {
                if (!player.getWorld().isClient()) {
                    Item item = stack.getItem();
                    if (NutritionMain.NUTRITION_ITEM_MAP.containsKey(item)) {
                        for (int i = 0; i < NutritionMain.NUTRITION_ITEM_MAP.get(item).size(); i++) {
                            if (NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i) > 0) {
                                ((HungerManagerAccess) player.getHungerManager()).addNutritionLevel(i, NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i));
                            }
                        }
                    }
                }
            });
        }
        // datapacks
        if (FabricLoader.getInstance().isModLoaded("adventurez")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "adventurez_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betterend")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "betterend_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betternether")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "betternether_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("cornexpansion")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "cornexpansion_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("croptopia")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "croptopia_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "dehydration_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("vinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "dehydration_x_vinery_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farm_and_charm")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "farm_and_charm_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "farmersdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("fishofthieves")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "fishofthieves_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("livingthings")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "livingthings_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("moredelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "moredelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("natures_spirit")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "natures_spirit_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("oceansdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "oceansdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("the_bumblezone")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "the_bumblezone_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("ubesdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "ubesdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("vinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "vinery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "wilderwild_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
    }

}
