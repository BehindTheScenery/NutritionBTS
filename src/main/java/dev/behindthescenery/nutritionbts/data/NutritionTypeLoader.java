package dev.behindthescenery.nutritionbts.data;

import com.google.common.collect.Comparators;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.behindthescenery.nutritionbts.NutritionMain;
import dev.behindthescenery.nutritionbts.nutrition.NutritionType;
import dev.behindthescenery.nutritionbts.util.FileLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NutritionTypeLoader extends FileLoader {
    public static final NutritionTypeLoader INSTANCE = new NutritionTypeLoader("nutrition_types");

    public NutritionTypeLoader(String directoryName) {
        super(directoryName);
    }

    private final List<NutritionType> loaded = new ArrayList<>();

    @Unmodifiable
    public List<NutritionType> getLoaded() {
        return List.copyOf(loaded);
    }

    @Override
    protected void preInit() {
        loaded.clear();
    }

    @Override
    protected void resolveFile(@NotNull JsonElement element) {
        DataResult<NutritionType> res = NutritionType.CODEC.parse(JsonOps.INSTANCE, element).ifError(NutritionMain.LOGGER::error);
        if (res.isError()) return;

        loaded.add(res.getOrThrow());
    }

    @Override
    protected void postInit() {
        loaded.sort(Comparator.comparingInt(NutritionType::column));
    }
}
