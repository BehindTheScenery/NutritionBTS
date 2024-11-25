package dev.behindthescenery.nutritionbts.util;

import dev.behindthescenery.nutritionbts.NutritionMain;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

public class NutritionUtil {
    private static final List<String> NUTRITION_TOOLTIPS = List.of(
        "item." + MOD_ID + ".carbohydrates",
        "item." + MOD_ID + ".protein",
        "item." + MOD_ID + ".fat",
        "item." + MOD_ID + ".vitamins",
        "item." + MOD_ID + ".minerals"
    );

    public static void addNutritionToolTip(ItemStack stack, List<Text> list) {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) && NutritionMain.NUTRITION_ITEM_MAP.containsKey(stack.getItem())) {
            List<Integer> nutritionList = NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem());

            boolean hasNutritions = false;
            for (int i = 0; i < nutritionList.size(); i++) {
                if (nutritionList.get(i) > 0) {
                    if (!hasNutritions) {
                        list.add(ScreenTexts.EMPTY);
                        list.add(Text.translatable("item." + MOD_ID + ".nutrients"));
                    }
                    list.add(Text.translatable(NUTRITION_TOOLTIPS.get(i), nutritionList.get(i)).formatted(Formatting.GREEN));
                    hasNutritions = true;
                }
            }
        }
    }

}
