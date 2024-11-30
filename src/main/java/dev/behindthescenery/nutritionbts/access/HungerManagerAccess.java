package dev.behindthescenery.nutritionbts.access;

import dev.behindthescenery.nutritionbts.nutrition.NutritionType;

import java.util.Map;

public interface HungerManagerAccess {

    void addNutritionLevel(NutritionType type, int level);

    void decrementNutritionLevel(NutritionType type, int level);

    void setNutritionLevel(NutritionType type, int level);

    int getNutritionLevel(NutritionType type);

    Map<NutritionType, Integer> getNutritionLevels();

    void setNutritionLevels(Map<NutritionType, Integer> levels);
}
