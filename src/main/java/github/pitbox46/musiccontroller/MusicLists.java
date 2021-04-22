package github.pitbox46.musiccontroller;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.nio.charset.StandardCharsets;
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

        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/menu/menu1"), "C418 - Mutation");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/menu/menu2"), "C418 - Moog City 2");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/menu/menu3"), "C418 - Beginning 2");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/menu/menu4"), "C418 - Floating Trees");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative1"), "C418 - Biome Fest");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative2"), "C418 - Blind Spots");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative3"), "C418 - Haunt Muskie");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative4"), "C418 - Aria Math");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative5"), "C418 - Dreiton");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/creative/creative6"), "C418 - Taswell");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/calm1"), "C418 - Minecraft");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/calm2"), "C418 - Clark");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/calm3"), "C418 - Sweden");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/hal1"), "C418 - Subwoofer Lullaby");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/hal2"), "C418 - Living Mice");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/hal3"), "C418 - Haggstrom");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/hal4"), "C418 - Danny");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nuance1"), "C418 - Key");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nuance2"), "C418 - Oxygène");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/piano1"), "C418 - Dry Hands");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/piano2"), "C418 - Wet Hands");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/piano3"), "C418 - Mice on Venus");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether1"), "C418 - Concrete Halls");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether2"), "C418 - Dead Voxel");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether3"), "C418 - Warmth");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether4"), "C418 - Ballad of the Cats");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/soulsand_valley/so_below"), "Lena Raine - So Below");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/nether_wastes/rubedo"), "Lena Raine - Rubedo");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/nether/crimson_forest/chrysopoeia"), "Lena Raine - Chrysopoeia");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/water/axolotl"), "C418 - Axolotl");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/water/dragon_fish"), "C418 - Dragon Fish");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/water/shuniji"), "C418 - Shuniji");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/end/boss"), "C418 - Boss");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/end/credits"), "C418 - Alpha");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","music/game/end/end"), "C418 - The End");

        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/11"), "C418 - Eleven");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/13"), "C418 - Thirteen");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/blocks"), "C418 - Blocks");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/cat"), "C418 - Cat");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/chirp"), "C418 - Chirp");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/far"), "C418 - Far");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/mall"), "C418 - Mall");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/mellohi"), "C418 - Mellohi");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/pigstep"), "Lena Raine - Pigstep");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/stal"), "C418 - Stal");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/strad"), "C418 - Strad");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/wait"), "C418 - Wait");
        MusicInfo.registerTrack(new ResourceLocation("minecraft","records/ward"), "C418 - Ward");
    }

    public static void init() {
    }

    public static List<SoundEvent> getMusicTypes() {
        return MUSIC_TYPES;
    }
}
