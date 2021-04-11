package github.pitbox46.musiccontroller;

import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Config {
    private static Logger LOGGER = LogManager.getLogger();

    public static final String CATEGORY_GENERAL = "general";
    public static final String SUBCATEGORY_DELAY = "music_delay";

    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue HIDE_CONTROLLER;
    private static final ArrayList<Integer> COMMON_MUSIC_DEFAULT = new ArrayList<>();
    private static final ArrayList<Integer> MENU_MUSIC_DEFAULT = new ArrayList<>();
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> MENU_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> CREATIVE_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> END_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> UNDER_WATER_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> WORLD_MUSIC_RANGE;

    static{
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);

        HIDE_CONTROLLER = CLIENT_BUILDER.comment("Hide music controller display")
                .define("hide_controller", false);

        /* DELAY START */
        CLIENT_BUILDER.comment("Delay between music tracks in ticks (20 ticks in a second)").push(SUBCATEGORY_DELAY);

        COMMON_MUSIC_DEFAULT.add(12000); COMMON_MUSIC_DEFAULT.add(24000);
        MENU_MUSIC_DEFAULT.add(20); MENU_MUSIC_DEFAULT.add(600);

        MENU_MUSIC_RANGE = CLIENT_BUILDER.comment("Menu music min and max")
                .define("menu_music_range", MENU_MUSIC_DEFAULT);
        CREATIVE_MUSIC_RANGE = CLIENT_BUILDER.comment("Creative music min and max")
                .define("creative_music_range", COMMON_MUSIC_DEFAULT);
        END_MUSIC_RANGE = CLIENT_BUILDER.comment("End music min and max")
                .define("end_music_range", COMMON_MUSIC_DEFAULT);
        UNDER_WATER_MUSIC_RANGE = CLIENT_BUILDER.comment("Under water music min and max")
                .define("under_water_music_range", COMMON_MUSIC_DEFAULT);
        WORLD_MUSIC_RANGE = CLIENT_BUILDER.comment("World music min and max")
                .define("world_music_range", COMMON_MUSIC_DEFAULT);

        CLIENT_BUILDER.pop();
        /* DELAY END */

        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();

        BackgroundMusicTracks.MAIN_MENU_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_MENU, MENU_MUSIC_RANGE.get().get(0), MENU_MUSIC_RANGE.get().get(1), true);
        BackgroundMusicTracks.CREATIVE_MODE_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_CREATIVE, CREATIVE_MUSIC_RANGE.get().get(0), CREATIVE_MUSIC_RANGE.get().get(1), false);
        BackgroundMusicTracks.END_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_END, END_MUSIC_RANGE.get().get(0), END_MUSIC_RANGE.get().get(1), true);
        BackgroundMusicTracks.UNDER_WATER_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_UNDER_WATER, UNDER_WATER_MUSIC_RANGE.get().get(0), UNDER_WATER_MUSIC_RANGE.get().get(1), false);
        BackgroundMusicTracks.WORLD_MUSIC = new BackgroundMusicSelector(SoundEvents.MUSIC_GAME, WORLD_MUSIC_RANGE.get().get(0), WORLD_MUSIC_RANGE.get().get(1), false);
    }
}
