package dev.behindthescenery.nutritionbts.access;

public interface HungerManagerAccess {

    void addNutritionLevel(int type, int level);

    void decrementNutritionLevel(int type, int level);

    void setNutritionLevel(int type, int level);

    int getNutritionLevel(int type);
}
