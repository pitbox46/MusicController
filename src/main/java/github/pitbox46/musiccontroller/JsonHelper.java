package github.pitbox46.musiccontroller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundListSerializer;
import net.minecraft.resources.IResource;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class JsonHelper {
    private final static Logger LOGGER = LogManager.getLogger();
    private final Minecraft minecraft;
    private static final Gson GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
    private static final TypeToken<Map<String, SoundList>> TYPE = new TypeToken<Map<String, SoundList>>() {};
    private final static int BUF_SIZE = 32768;

    public JsonHelper(Minecraft minecraft) {
        this.minecraft = minecraft;
        try {
            createResourcePack();
        } catch(IOException e) {
            System.out.println("Error occurred while populating musiccontroller sounds.json");
            e.printStackTrace();
        }
    }

    public Map<String,SoundList> soundsReader(String jsonPath) throws IOException {
        JsonObject jsonObject = readJsonFromZip(jsonPath);
        Reader reader = new InputStreamReader(new ByteArrayInputStream(jsonObject.toString().getBytes()));
        return JSONUtils.fromJSONUnlenient(GSON, reader, TYPE);
    }

    public void addSound(String playlist, ResourceLocation sound) {
        JsonObject parentObject = readJsonFromZip("assets/minecraft/sounds.json");

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

        writeJsonToZip(parentObject, "assets/minecraft/sounds.json");
    }

    public void removeSound(String playlist, ResourceLocation sound) {
        JsonObject parentObject = readJsonFromZip("assets/minecraft/sounds.json");

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

        writeJsonToZip(parentObject, "assets/minecraft/sounds.json");
    }

    public JsonObject readJsonFromZip(String jsonPath) {
        try {
            ZipFile resourcePack = new ZipFile(new File(this.minecraft.getFileResourcePacks(), "MusicControllerPack.zip"));

            ZipEntry jsonFile = resourcePack.getEntry(jsonPath);
            if(jsonFile == null)
                throw new IOException(jsonPath + " was not found in MusicControllerPack.zip");

            JsonObject jsonObject = JSONUtils.fromJson(new InputStreamReader(resourcePack.getInputStream(jsonFile)));
            resourcePack.close();
            return jsonObject;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void writeJsonToZip(JsonObject jsonObject, String jsonPath) {
        Path resourcePackPath = Paths.get(this.minecraft.getFileResourcePacks().getPath() + File.separatorChar + "MusicControllerPack.zip");

        try(FileSystem fs = FileSystems.newFileSystem(resourcePackPath, null)) {
            Path jsonInZip = fs.getPath(jsonPath);
            Files.write(jsonInZip, jsonObject.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createResourcePack() throws IOException {
        InputStream victimStream = this.getClass().getResourceAsStream("/MusicControllerPack.zip");
        File targetFile = new File(this.minecraft.getFileResourcePacks(), "MusicControllerPack.zip");
        FileOutputStream targetStream;

        if(targetFile.exists()) return;

        try {
            targetStream = new FileOutputStream(targetFile);
        } catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        targetFile.createNewFile();
        byte[] buf = new byte[BUF_SIZE];
        int i;
        while ((i = victimStream.read(buf)) > 0) {
            targetStream.write(buf, 0, i);
        }
        victimStream.close();
        targetStream.close();
    }
}


