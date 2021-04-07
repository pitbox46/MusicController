package github.pitbox46.musicdisplay;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class MusicGUI extends IngameGui implements ISoundEventListener {
    private ISound currentMusic;
    private ISound currentRecord;

    public MusicGUI() {
        super(Minecraft.getInstance());
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if(event == null || event.getMatrixStack() == null) return;
        Minecraft.getInstance().getSoundHandler().addListener(this);

        if(!Minecraft.getInstance().getSoundHandler().isPlaying(currentMusic)) currentMusic = null;
        //Todo add attenuation distance
        if(!Minecraft.getInstance().getSoundHandler().isPlaying(currentRecord)) currentRecord = null;

        String musicString = currentMusic == null ? "None" : currentMusic.getSound().getSoundAsOggLocation().toString();
        String recordString = currentRecord == null ? "None" : currentRecord.getSound().getSoundAsOggLocation().toString();

        TranslationTextComponent nowPlaying = new TranslationTextComponent("gui.musicdisplay.nowplaying");
        getFontRenderer().drawText(event.getMatrixStack(), nowPlaying.appendSibling(new TranslationTextComponent(musicString)), 5, 5, 0);
        getFontRenderer().drawText(event.getMatrixStack(), nowPlaying.copyRaw().appendSibling(new TranslationTextComponent(recordString)), 5, 15, 0);
    }

    @Override
    public void onPlaySound(ISound soundIn, SoundEventAccessor accessor) {
        if(soundIn.getCategory().equals(SoundCategory.MUSIC)) {
            currentMusic = soundIn;
        }
        if(soundIn.getCategory().equals(SoundCategory.RECORDS)) {
            currentRecord = soundIn;
        }
    }
}
