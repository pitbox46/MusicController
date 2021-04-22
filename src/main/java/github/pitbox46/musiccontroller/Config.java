package github.pitbox46.musiccontroller;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Config {
    private static Logger LOGGER = LogManager.getLogger();

    public static final String CATEGORY_GENERAL = "general";
    public static final String SUBCATEGORY_DELAY = "music_delay";
    public static final String SUBCATEGORY_POOL = "music_pools";

    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue HIDE_CONTROLLER;

    private static final ArrayList<Integer> COMMON_MUSIC_DEFAULT = new ArrayList<>(2);
    private static final ArrayList<Integer> MENU_MUSIC_DEFAULT = new ArrayList<>(2);
    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> MENU_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<String>> MENU_MUSIC_POOL;

    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> CREATIVE_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<String>> CREATIVE_MUSIC_POOL;

    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> END_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<String>> END_MUSIC_POOL;

    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> UNDER_WATER_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<String>> UNDER_WATER_MUSIC_POOL;

    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> GAME_MUSIC_RANGE;
    public static ForgeConfigSpec.ConfigValue<ArrayList<String>> GAME_MUSIC_POOL;

    public static ForgeConfigSpec.ConfigValue<ArrayList<Integer>> OTHER_MUSIC_RANGE;

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
        GAME_MUSIC_RANGE = CLIENT_BUILDER.comment("Game music min and max")
                .define("game_music_range", COMMON_MUSIC_DEFAULT);
        OTHER_MUSIC_RANGE = CLIENT_BUILDER.comment("Other music min and max - mainly Nether music")
                .define("other_music_range", COMMON_MUSIC_DEFAULT);

        CLIENT_BUILDER.pop();
        /* DELAY END */

        /* MUSIC POOL START */
        CLIENT_BUILDER.comment("Other types of music to contain in a music type pool. eg: {\"music.creative\",\"music.menu\"}").push(SUBCATEGORY_POOL);

        MENU_MUSIC_POOL = CLIENT_BUILDER.comment("Menu music pool additions")
                .define("music_menu", new ArrayList<>(Collections.singletonList("music.menu")));
        CREATIVE_MUSIC_POOL = CLIENT_BUILDER.comment("Creative music pool additions")
                .define("music_creative", new ArrayList<>(Arrays.asList("music.creative", "music.game")));
        END_MUSIC_POOL = CLIENT_BUILDER.comment("End music pool additions")
                .define("music_end", new ArrayList<>(Collections.singletonList("music.end")));
        UNDER_WATER_MUSIC_POOL = CLIENT_BUILDER.comment("Under water music pool additions")
                .define("music_under_water", new ArrayList<>(Collections.singletonList("music.under_water")));
        GAME_MUSIC_POOL = CLIENT_BUILDER.comment("World music pool additions")
                .define("music_game", new ArrayList<>(Collections.singletonList("music.game")));

        CLIENT_BUILDER.pop();
        /* MUSIC POOL END */

        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }
}
