package github.pitbox46.musiccontroller;


import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod(MusicController.MODID)
public class MusicController {
    public static final String MODID = "musiccontroller";
    private static final Logger LOGGER = LogManager.getLogger();

    public static KeyBinding nextTrack;
    public static KeyBinding togglePauseTrack;
    public static KeyBinding toggleMusic;

    public MusicController() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        MusicLists.init();

        MinecraftForge.EVENT_BUS.register(new MusicGUI());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);

        BackgroundMusicTracks.MAIN_MENU_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_MENU, Config.MENU_MUSIC_RANGE.get().get(0), Config.MENU_MUSIC_RANGE.get().get(1), true);
        BackgroundMusicTracks.CREATIVE_MODE_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_CREATIVE, Config.CREATIVE_MUSIC_RANGE.get().get(0), Config.CREATIVE_MUSIC_RANGE.get().get(1), false);
        BackgroundMusicTracks.END_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_END, Config.END_MUSIC_RANGE.get().get(0), Config.END_MUSIC_RANGE.get().get(1), true);
        BackgroundMusicTracks.UNDER_WATER_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_UNDER_WATER, Config.UNDER_WATER_MUSIC_RANGE.get().get(0), Config.UNDER_WATER_MUSIC_RANGE.get().get(1), false);
        BackgroundMusicTracks.WORLD_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_GAME, Config.GAME_MUSIC_RANGE.get().get(0), Config.GAME_MUSIC_RANGE.get().get(1), false);

        nextTrack = new KeyBinding("key.musiccontroller.skip_track", 79, "key.musiccontroller.category");
        togglePauseTrack = new KeyBinding("key.musiccontroller.pause_track", 85, "key.musiccontroller.category");
        toggleMusic = new KeyBinding("key.musiccontroller.toggle_music", 73, "key.musiccontroller.category");

        ClientRegistry.registerKeyBinding(nextTrack);
        ClientRegistry.registerKeyBinding(toggleMusic);
        ClientRegistry.registerKeyBinding(togglePauseTrack);
        LOGGER.debug("Music Controller client setup");
    }
}
