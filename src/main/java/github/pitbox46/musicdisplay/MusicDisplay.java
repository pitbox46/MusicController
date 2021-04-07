package github.pitbox46.musicdisplay;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MusicDisplay.MODID)
public class MusicDisplay {
    public static final String MODID = "musicdisplay";
    private static final Logger LOGGER = LogManager.getLogger();

    public MusicDisplay() {
        MinecraftForge.EVENT_BUS.register(new MusicGUI());
    }
}
