package dev.behindthescenery.nutritionbts.data;

import com.mojang.serialization.Codec;
import dev.behindthescenery.nutritionbts.nutrition.NutritionType;
import dev.behindthescenery.nutritionbts.util.CodecFileLoader;
import org.jetbrains.annotations.NotNull;

public class NutritionTypeLoader extends CodecFileLoader<NutritionType> {
    public static final NutritionTypeLoader INSTANCE = new NutritionTypeLoader("nutrition_types");

    public NutritionTypeLoader(String directoryName) {
        super(directoryName);
    }

    @Override
    @NotNull
    public Codec<NutritionType> getCodec() {
        return NutritionType.CODEC;
    }
}
