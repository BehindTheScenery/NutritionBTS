package dev.behindthescenery.nutritionbts.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.behindthescenery.nutritionbts.NutritionMain;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public abstract class CodecFileLoader<T> extends FileLoader {
    public CodecFileLoader(String directoryName) {
        super(directoryName);
    }

    private final List<T> loaded = new ArrayList<>();

    @Unmodifiable
    public List<T> getLoaded() {
        return List.copyOf(loaded);
    }

    @Override
    protected void preInit() {
        loaded.clear();
    }

    @NotNull
    public abstract Codec<T> getCodec();

    @Override
    protected void resolveFile(@NotNull JsonElement element) {
        DataResult<T> res = getCodec().parse(JsonOps.INSTANCE, element).ifError(NutritionMain.LOGGER::error);
        if (res.isError()) return;

        loaded.add(res.getOrThrow());
    }
}
