package github.pitbox46.musiccontroller;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;
import java.util.Map;

public class MusicInfo {
    private static final Map<ResourceLocation,MusicInfo> REGISTERED_TRACKS = new HashMap<>();
    private final ResourceLocation resourceLocation;
    private final Float volume;
    private final Float pitch;

    private MusicInfo(ResourceLocation resourceLocation, Float volume, Float pitch) {
        this.resourceLocation = resourceLocation;
        this.volume = volume;
        this.pitch = pitch;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public ITextComponent getDisplayText() {
        return new TranslationTextComponent(resourceLocation.toString());
    }

    public Float getVolume() {
        return volume;
    }

    public Float getPitch() {
        return pitch;
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

    public static ITextComponent getDisplayText(ResourceLocation resourceLocation) {
        MusicInfo musicInfo = REGISTERED_TRACKS.get(resourceLocation);
        return musicInfo == null ? new StringTextComponent("Null") : new TranslationTextComponent(resourceLocation.toString());
    }
}
