package github.pitbox46.musiccontroller;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.HashMap;
import java.util.Map;

public record MusicInfo(ResourceLocation resourceLocation, Float volume, Float pitch) {
    private static final Map<ResourceLocation, MusicInfo> REGISTERED_TRACKS = new HashMap<>();

    public Component getDisplayText() {
        return new TranslatableComponent(resourceLocation.toString());
    }

    /* Registry */
    public static void registerTrack(ResourceLocation resourceLocation, Float volume, Float pitch) {
        REGISTERED_TRACKS.put(resourceLocation, new MusicInfo(resourceLocation, volume, pitch));
    }

    public static void registerTrack(ResourceLocation resourceLocation) {
        registerTrack(resourceLocation, 1F, 1F);
    }

    public static Map<ResourceLocation, MusicInfo> getRegisteredTracks() {
        return new HashMap<>(REGISTERED_TRACKS);
    }

    public static MusicInfo getMusicInfo(ResourceLocation resourceLocation) {
        return REGISTERED_TRACKS.get(resourceLocation);
    }

    public static Component getDisplayText(ResourceLocation resourceLocation) {
        MusicInfo musicInfo = REGISTERED_TRACKS.get(resourceLocation);
        return musicInfo==null ? new TextComponent("Null"):new TranslatableComponent(resourceLocation.toString());
    }
}
