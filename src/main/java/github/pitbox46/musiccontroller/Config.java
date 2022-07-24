package github.pitbox46.musiccontroller;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Config {
    private static Logger LOGGER = LogManager.getLogger();
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue HIDE_CONTROLLER;
    private static final ArrayList<Integer> COMMON_MUSIC_DEFAULT = new ArrayList<>(2);
    private static final ArrayList<Integer> MENU_MUSIC_DEFAULT = new ArrayList<>(2);
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> MENU_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> CREATIVE_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> END_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> UNDER_WATER_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> GAME_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> OTHER_MUSIC_RANGE;

    static{
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General Settings").push("general");

        HIDE_CONTROLLER = CLIENT_BUILDER.comment("Hide music controller display")
                .define("hide_controller", false);

        /* DELAY START */
        CLIENT_BUILDER.comment("Delay between music tracks in ticks (20 ticks in a second)").push("music_delay");

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
        GAME_MUSIC_RANGE = CLIENT_BUILDER.comment("Game music min and max")
                .define("game_music_range", COMMON_MUSIC_DEFAULT);
        OTHER_MUSIC_RANGE = CLIENT_BUILDER.comment("Other music min and max - mainly Nether music")
                .define("other_music_range", COMMON_MUSIC_DEFAULT);

        CLIENT_BUILDER.pop();
        /* DELAY END */

        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }
}
