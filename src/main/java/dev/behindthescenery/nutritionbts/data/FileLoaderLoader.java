package dev.behindthescenery.nutritionbts.data;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.profiler.Profiler;

public class FileLoaderLoader extends SinglePreparationResourceReloader<Void> {
    @Override
    protected Void prepare(ResourceManager manager, Profiler profiler) {
        return null;
    }

    @Override
    protected void apply(Void prepared, ResourceManager manager, Profiler profiler) {
        resolveAllPaths();
    }

    private void resolveAllPaths() {
        NutritionTypeLoader.INSTANCE.resolvePaths();
        ItemLevelsLoader.INSTANCE.resolvePaths();
    }
}
