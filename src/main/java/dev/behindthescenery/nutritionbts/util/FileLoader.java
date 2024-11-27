package dev.behindthescenery.nutritionbts.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.behindthescenery.nutritionbts.NutritionMain;
import net.minecraft.util.Identifier;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.locating.IModFile;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public abstract class FileLoader {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Identifier.class, new Identifier.Serializer()).create();
    private final String directoryName;

    public FileLoader(String directoryName) {
        this.directoryName = directoryName;
    }

    public void resolvePaths() {
        preInit();

        for (IModInfo info : ModList.get().getMods()) {
            if (info.getOwningFile().getFile().getType() != IModFile.Type.MOD) continue;

            Path path = info.getOwningFile().getFile().getFilePath().resolve("data").resolve(info.getModId()).resolve(directoryName);

            if (Files.exists(path)) {
                try (Stream<Path> paths = Files.walk(path)) {
                    for (Path path_ : paths.toList()) {
                        if (!path_.toString().endsWith(".json") || !Files.isRegularFile(path_)) continue;

                        try (FileReader reader = new FileReader(path_.toFile())) {
                            resolveFile(GSON.fromJson(reader, JsonElement.class));
                        } catch (IOException e) {
                            NutritionMain.LOGGER.error("Failed to read file \"{}\"", path_, e);
                        }
                    }
                } catch (IOException e) {
                    NutritionMain.LOGGER.error("Couldn't load \"{}\" in mod \"{}\"", directoryName, info.getModId(), e);
                }
            }
        }
    }

    protected abstract void resolveFile(@NotNull JsonElement element);

    protected abstract void preInit();
}
