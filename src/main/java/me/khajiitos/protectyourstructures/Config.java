package me.khajiitos.protectyourstructures;

import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final File file = new File("config/protectyourstructures.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final List<ResourceKey<Structure>> structures = new ArrayList<>();

    public static void load(RegistryAccess registryAccess) {
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file)) {
                JsonObject object = GSON.fromJson(fileReader, JsonObject.class);

                JsonArray structuresArray = object.get("structures").getAsJsonArray();

                structuresArray.forEach(jsonElement -> {
                    try {
                        String resourceKeyStr = jsonElement.getAsString();
                        ResourceLocation resourceLocation = new ResourceLocation(resourceKeyStr);
                        Structure structure = registryAccess.registryOrThrow(Registry.STRUCTURE_REGISTRY).get(resourceLocation);

                        if (structure != null) {
                            structures.add(ResourceKey.create(Registry.STRUCTURE_REGISTRY, resourceLocation));
                        } else {
                            ProtectYourStructures.LOGGER.warn("Skipping unknown structure: " + resourceKeyStr);
                        }
                    } catch (IllegalArgumentException | ClassCastException ignored) {}
                });

            } catch (JsonIOException | JsonSyntaxException e) {
                ProtectYourStructures.LOGGER.error("JSON error", e);
            } catch (IOException e) {
                ProtectYourStructures.LOGGER.error("Error while loading config", e);
            }
        } else {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                ProtectYourStructures.LOGGER.error("Failed to create directories for protectyourstructures.json");
                return;
            }
            save();
        }
    }

    public static void save() {
        try (FileWriter fileReader = new FileWriter(file)) {

            JsonObject object = new JsonObject();
            JsonArray structuresArray = new JsonArray();
            structures.forEach(structureKey -> structuresArray.add(structureKey.location().toString()));
            object.add("structures", structuresArray);

            fileReader.write(GSON.toJson(object));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
