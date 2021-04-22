package github.pitbox46.musiccontroller.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import github.pitbox46.musiccontroller.MusicLists;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.resource.VanillaResourceType;

public class PlaylistSelectorScreen extends Screen {
    private final Screen previousScreen;


    protected PlaylistSelectorScreen(Screen previousScreen, ITextComponent titleIn) {
        super(titleIn);
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        int i = 0;
        for(SoundEvent soundEvent: MusicLists.getMusicTypes()) {
            this.addButton(new Button(this.width/2 - 155 + 160 * (i % 2), this.height/6 - 12 + 24 * (i >> 1), 150, 20, new TranslationTextComponent(soundEvent.getName().toString()), button -> {
                minecraft.displayGuiScreen(new PlaylistScreen(soundEvent.getName().getPath(),this, new TranslationTextComponent(soundEvent.getName().toString())));
            }));
            i++;
        }
        this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, DialogTexts.GUI_DONE, (button) -> {
            assert this.minecraft != null;
            this.closeScreen();
        }));
    }

    @Override
    public void closeScreen() {
        this.minecraft.displayGuiScreen(this.previousScreen);
        assert minecraft != null;
        ForgeHooksClient.refreshResources(minecraft, VanillaResourceType.SOUNDS);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 15, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}