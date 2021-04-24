package github.pitbox46.musiccontroller.screen;

import net.minecraft.client.AbstractOption;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.resource.VanillaResourceType;

public class ScreenEvents {

    @SubscribeEvent
    public void onGuiScreenEvent(GuiScreenEvent.InitGuiEvent.Post event) {
        if(event.getGui().getClass() == OptionsSoundsScreen.class) {
            event.removeWidget(event.getWidgetList().get(10));
            event.addWidget(new OptionButton(event.getGui().width / 2 - 155, event.getGui().height/6 - 12 + (24 * 6), 150, 20, AbstractOption.SHOW_SUBTITLES, AbstractOption.SHOW_SUBTITLES.func_238152_c_(event.getGui().getMinecraft().gameSettings), (p_213105_1_) -> {
                AbstractOption.SHOW_SUBTITLES.nextValue(event.getGui().getMinecraft().gameSettings);
                p_213105_1_.setMessage(AbstractOption.SHOW_SUBTITLES.func_238152_c_(event.getGui().getMinecraft().gameSettings));
                event.getGui().getMinecraft().gameSettings.saveOptions();
            }));
            event.addWidget(new Button(event.getGui().width/2 + 5, event.getGui().height/6 - 12 + (24 * 6), 150, 20, new TranslationTextComponent("button.musiccontroller.playlists"), button -> {
                PackLoadingManager packLoadingManager = new PackLoadingManager(() -> {}, a -> null, event.getGui().getMinecraft().getResourcePackList(), a -> {});
                packLoadingManager.func_238869_b_().filter(item -> item.func_230462_b_().getString().equals("MusicControllerPack.zip")).findFirst().ifPresent(PackLoadingManager.IPack::func_230472_i_);
                packLoadingManager.func_241618_c_();
                ForgeHooksClient.refreshResources(event.getGui().getMinecraft(), VanillaResourceType.SOUNDS);
                event.getGui().getMinecraft().displayGuiScreen(new PlaylistSelectorScreen(event.getGui(), packLoadingManager, new TranslationTextComponent("screen.musiccontroller.playlists")));
            }));
        }
    }
}
