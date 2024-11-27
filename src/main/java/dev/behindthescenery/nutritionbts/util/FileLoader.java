package dev.behindthescenery.nutritionbts.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.behindthescenery.nutritionbts.NutritionMain;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public abstract class FileLoader {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Identifier.class, new Identifier.Serializer()).create();
    private final String directoryName;

    public FileLoader(String directoryName) {
        this.directoryName = directoryName;
    }

    public void resolvePaths(ResourceManager manager) {
        preInit();

        ResourceFinder finder = ResourceFinder.json(directoryName);

        for (Map.Entry<Identifier, List<Resource>> entry : finder.findAllResources(manager).entrySet()) {
            Identifier id = entry.getKey();
            List<Resource> resources = entry.getValue();

            for (Resource resource : resources) {
                try {
                    resolveFile(GSON.fromJson(resource.getReader(), JsonElement.class));
                } catch (IOException e) {
                    NutritionMain.LOGGER.error("Failed to read file \"{}\"", resource, e);
                }
            }

        }
    }

    protected abstract void resolveFile(@NotNull JsonElement element);

    protected abstract void preInit();
}
