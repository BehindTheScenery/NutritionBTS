package dev.behindthescenery.nutritionbts.util;

import dev.behindthescenery.nutritionbts.data.ItemLevelsLoader;
import dev.behindthescenery.nutritionbts.nutrition.NutritionType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

import static dev.behindthescenery.nutritionbts.NutritionMain.MOD_ID;

public class NutritionUtil {
    public static void addNutritionToolTip(ItemStack stack, List<Text> list) {
        if (ItemLevelsLoader.INSTANCE.getItemLevels().containsKey(stack.getItem())) {
            if (Screen.hasShiftDown()) {
                Map<Identifier, Integer> nutritionMap = ItemLevelsLoader.INSTANCE.getItemLevels().get(stack.getItem());
                list.add(ScreenTexts.EMPTY);
                list.add(Text.translatable("item." + MOD_ID + ".nutrients"));
                nutritionMap.forEach((k, v) -> {
                    if (v > 0) {
                        try {
                            list.add(NutritionType.byId(k).tooltip().copy().append(" (" + v + ")").formatted(Formatting.GREEN));
                        } catch (IllegalArgumentException ignore) {
                        }
                    }
                });
                return;
            }
            list.add(Text.translatable("text." + MOD_ID + ".hold_shift").formatted(Formatting.GRAY));
        }
    }
}
