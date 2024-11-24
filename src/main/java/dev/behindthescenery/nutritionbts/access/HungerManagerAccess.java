package dev.behindthescenery.nutritionbts.access;

public interface HungerManagerAccess {

    public void addNutritionLevel(int type, int level);

    public void decrementNutritionLevel(int type, int level);

    public void setNutritionLevel(int type, int level);

    public int getNutritionLevel(int type);
}
