package github.pitbox46.musiccontroller;


import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.client.KeyMapping;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod(MusicController.MODID)
public class MusicController {
    public static final String MODID = "musiccontroller";
    private static final Logger LOGGER = LogManager.getLogger();

    public static KeyMapping nextTrack;
    public static KeyMapping togglePauseTrack;
    public static KeyMapping toggleMusic;

    public MusicController() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        MusicLists.init();

        MinecraftForge.EVENT_BUS.register(new MusicGUI());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);

        Musics.MENU = new Music(SoundEvents.MUSIC_MENU, Config.MENU_MUSIC_RANGE.get().get(0), Config.MENU_MUSIC_RANGE.get().get(1), true);
        Musics.CREATIVE = new Music(SoundEvents.MUSIC_CREATIVE, Config.CREATIVE_MUSIC_RANGE.get().get(0), Config.CREATIVE_MUSIC_RANGE.get().get(1), false);
        Musics.END = new Music(SoundEvents.MUSIC_END, Config.END_MUSIC_RANGE.get().get(0), Config.END_MUSIC_RANGE.get().get(1), true);
        Musics.UNDER_WATER = new Music(SoundEvents.MUSIC_UNDER_WATER, Config.UNDER_WATER_MUSIC_RANGE.get().get(0), Config.UNDER_WATER_MUSIC_RANGE.get().get(1), false);
        Musics.GAME = new Music(SoundEvents.MUSIC_GAME, Config.GAME_MUSIC_RANGE.get().get(0), Config.GAME_MUSIC_RANGE.get().get(1), false);

        nextTrack = new KeyMapping("key.musiccontroller.skip_track", 79, "key.musiccontroller.category");
        togglePauseTrack = new KeyMapping("key.musiccontroller.pause_track", 85, "key.musiccontroller.category");
        toggleMusic = new KeyMapping("key.musiccontroller.toggle_music", 73, "key.musiccontroller.category");

        ClientRegistry.registerKeyBinding(nextTrack);
        ClientRegistry.registerKeyBinding(toggleMusic);
        ClientRegistry.registerKeyBinding(togglePauseTrack);
        LOGGER.debug("Music Controller client setup");
    }
}
