package github.pitbox46.musiccontroller;

import github.pitbox46.musiccontroller.screen.ScreenEvents;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@OnlyIn(Dist.CLIENT)
@Mod(MusicController.MODID)
public class MusicController {
    public static final String MODID = "musiccontroller";
    private static final Path path = FMLLoader.getLoadingModList().getModFileById(MODID).getFile().getFilePath();
    private static final Logger LOGGER = LogManager.getLogger();

    public static final KeyBinding nextTrack = new KeyBinding("key.musiccontroller.skip_track", 79, "key.musiccontroller.category");
    public static final KeyBinding togglePauseTrack = new KeyBinding("key.musiccontroller.pause_track", 85, "key.musiccontroller.category");
    public static final KeyBinding toggleMusic = new KeyBinding("key.musiccontroller.toggle_music", 73, "key.musiccontroller.category");

    public MusicController() {
        MusicLists.init();
        MinecraftForge.EVENT_BUS.register(new MusicGUI());
        MinecraftForge.EVENT_BUS.register(new ScreenEvents());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        BackgroundMusicTracks.MAIN_MENU_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_MENU, Config.MENU_MUSIC_RANGE.get().get(0), Config.MENU_MUSIC_RANGE.get().get(1), true);
        BackgroundMusicTracks.CREATIVE_MODE_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_CREATIVE, Config.CREATIVE_MUSIC_RANGE.get().get(0), Config.CREATIVE_MUSIC_RANGE.get().get(1), false);
        BackgroundMusicTracks.END_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_END, Config.END_MUSIC_RANGE.get().get(0), Config.END_MUSIC_RANGE.get().get(1), true);
        BackgroundMusicTracks.UNDER_WATER_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_UNDER_WATER, Config.UNDER_WATER_MUSIC_RANGE.get().get(0), Config.UNDER_WATER_MUSIC_RANGE.get().get(1), false);
        BackgroundMusicTracks.WORLD_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_GAME, Config.GAME_MUSIC_RANGE.get().get(0), Config.GAME_MUSIC_RANGE.get().get(1), false);

        ClientRegistry.registerKeyBinding(nextTrack);
        ClientRegistry.registerKeyBinding(toggleMusic);
        ClientRegistry.registerKeyBinding(togglePauseTrack);
        LOGGER.debug("Music Controller client setup");
    }

    public static Path getPath() {
        return path;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
