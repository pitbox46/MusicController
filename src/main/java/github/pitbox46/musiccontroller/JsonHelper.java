package github.pitbox46.musiccontroller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundListSerializer;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class JsonHelper {
    private final static Logger LOGGER = LogManager.getLogger();
    private final Minecraft minecraft;
    private static final Gson GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
    private static final TypeToken<Map<String, SoundList>> TYPE = new TypeToken<Map<String, SoundList>>() {};

    public JsonHelper(Minecraft minecraft) {
        this.minecraft = minecraft;
        try {
            jsonCreator(jsonReader(new File(MusicController.getPath().toFile(), String.format("assets%1$sminecraft%1$ssounds.json.base", File.separator))));
        } catch(IOException e) {
            System.out.println("Error occurred while populating musiccontroller sounds.json");
            e.printStackTrace();
        }
    }

    public static JsonObject jsonReader(File file) {
        try(Reader reader = new FileReader(file)) {
            return JSONUtils.fromJson(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String,SoundList> soundsReader(ResourceLocation resourceLocation) throws IOException {
        for (IResource resource : minecraft.getResourceManager().getAllResources(resourceLocation)) {
            if(resource.getPackName().equals("Mod Resources")) {
                try (InputStream inputstream = resource.getInputStream();
                     Reader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8)) {
                    return JSONUtils.fromJSONUnlenient(GSON, reader, TYPE);
                }
            }
        }
        return null;
    }

    /**
     * Creates a new json file copied from {@code minecraft:sounds.json} if no {@code musiccontroller:sounds.json} exists
     * @param jsonObject json to be copied
     * @return true if a new file is created and copied <br> false otherwise
     * @throws IOException
     */
    private static boolean jsonCreator(JsonObject jsonObject) throws IOException {
        File soundsJson = new File(MusicController.getPath().toFile(), String.format("assets%1$sminecraft%1$ssounds.json", File.separator));
        if(soundsJson.createNewFile()) {
            try(FileWriter writer = new FileWriter(soundsJson)) {
                writer.write(GSON.toJson(jsonObject));
                return true;
            }
        }
        return false;
    }

    public static void addSound(String playlist, ResourceLocation sound) {
        File soundsFile = new File(MusicController.getPath().toFile(), String.format("assets%1$sminecraft%1$ssounds.json", File.separator));
        JsonObject parentObject = jsonReader(soundsFile);

        JsonObject playlistObject = parentObject.getAsJsonObject(playlist);
        JsonElement soundsArray = playlistObject.get("sounds");
        if(soundsArray == null) {
            playlistObject.add("sounds", new JsonArray());
            soundsArray = playlistObject.get("sounds");
        }

        JsonObject addedSound = new JsonObject();
        addedSound.addProperty("name", sound.getPath());
        addedSound.addProperty("stream", true);
        soundsArray.getAsJsonArray().add(addedSound);
        try(FileWriter writer = new FileWriter(soundsFile)) {
            writer.write(GSON.toJson(parentObject));
        } catch (IOException e) {
            System.out.println("Error occurred while adding sound");
            e.printStackTrace();
        }
    }

    public static void removeSound(String playlist, ResourceLocation sound) {
        File soundsFile = new File(MusicController.getPath().toFile(), String.format("assets%1$sminecraft%1$ssounds.json", File.separator));
        JsonObject parentObject = jsonReader(soundsFile);

        JsonObject playlistObject = parentObject.getAsJsonObject(playlist);
        JsonElement soundsArray = playlistObject.get("sounds");
        if(soundsArray == null) {
            playlistObject.add("sounds", new JsonArray());
            soundsArray = playlistObject.get("sounds");
        }

        boolean removed = false;
        for(JsonElement element: soundsArray.getAsJsonArray()) {
            if(element.isJsonObject() && element.getAsJsonObject().get("name").getAsString().equals(sound.getPath())) {
                soundsArray.getAsJsonArray().remove(element);
                removed = true;
                break;
            }
        }
        if(!removed) {
            LOGGER.warn("{} was not removed in {}", sound.getPath(), playlist);
        }

        try(FileWriter writer = new FileWriter(soundsFile)) {
            writer.write(GSON.toJson(parentObject));
        } catch (IOException e) {
            System.out.println("Error occurred while adding sound");
            e.printStackTrace();
        }
    }}

