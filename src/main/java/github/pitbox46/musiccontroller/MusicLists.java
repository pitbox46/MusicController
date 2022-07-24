package github.pitbox46.musiccontroller;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class MusicLists {
    private static final List<SoundEvent> MUSIC_TYPES = new ArrayList<>();

    static {
        MUSIC_TYPES.add(SoundEvents.MUSIC_CREATIVE);
        MUSIC_TYPES.add(SoundEvents.MUSIC_CREDITS);
        MUSIC_TYPES.add(SoundEvents.MUSIC_DRAGON);
        MUSIC_TYPES.add(SoundEvents.MUSIC_END);
        MUSIC_TYPES.add(SoundEvents.MUSIC_GAME);
        MUSIC_TYPES.add(SoundEvents.MUSIC_MENU);
        MUSIC_TYPES.add(SoundEvents.MUSIC_NETHER_BASALT_DELTAS);
        MUSIC_TYPES.add(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST);
        MUSIC_TYPES.add(SoundEvents.MUSIC_NETHER_NETHER_WASTES);
        MUSIC_TYPES.add(SoundEvents.MUSIC_NETHER_SOUL_SAND_VALLEY);
        MUSIC_TYPES.add(SoundEvents.MUSIC_NETHER_WARPED_FOREST);
        MUSIC_TYPES.add(SoundEvents.MUSIC_UNDER_WATER);

        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/menu/menu1"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/menu/menu2"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/menu/menu3"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/menu/menu4"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative1"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative2"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative3"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative4"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative5"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative6"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/calm1"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/calm2"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/calm3"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/hal1"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/hal2"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/hal3"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/hal4"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nuance1"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nuance2"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/piano1"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/piano2"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/piano3"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether1"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether2"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether3"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether4"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/soulsand_valley/so_below"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether_wastes/rubedo"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/crimson_forest/chrysopoeia"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/water/axolotl"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/water/dragon_fish"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/water/shuniji"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/end/boss"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/end/credits"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/end/end"));

        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/11"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/13"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/blocks"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/cat"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/chirp"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/far"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/mall"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/mellohi"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/pigstep"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/stal"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/strad"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/wait"));
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/ward"));
    }

    public static void init() {
    }

    public static List<SoundEvent> getMusicTypes() {
        return MUSIC_TYPES;
    }
}
