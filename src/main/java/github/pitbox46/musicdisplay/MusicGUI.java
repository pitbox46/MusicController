package github.pitbox46.musicdisplay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class MusicGUI extends IngameGui implements ISoundEventListener {
    private ISound currentMusic;
    private Map<BlockPos, ISound> previousRecords = new HashMap<>();
    private Map<BlockPos, ISound> currentRecords = new HashMap<>();
    private ISound closestRecord;

    public MusicGUI() {
        super(Minecraft.getInstance());
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if(event == null || event.getMatrixStack() == null) return;
        Minecraft.getInstance().getSoundHandler().addListener(this);

        if(!Minecraft.getInstance().getSoundHandler().isPlaying(currentMusic)) currentMusic = null;

        for(BlockPos pos: currentRecords.keySet()){
            ISound sound = currentRecords.get(pos);
            if (!Minecraft.getInstance().getSoundHandler().isPlaying(sound)) {
                previousRecords.remove(pos);
                continue;
            }
            double distanceSq = distanceToPlayer(pos);
            if(closestRecord == null || (distanceSq < sound.getSound().getAttenuationDistance() && distanceSq < distanceToPlayer(soundPosition(closestRecord)))) {
                closestRecord = currentRecords.get(pos);
            }
        }
        currentRecords = previousRecords;
        if(closestRecord == null);
        else if(currentRecords.isEmpty() || distanceToPlayer(soundPosition(closestRecord)) > closestRecord.getSound().getAttenuationDistance()) closestRecord = null;

        String musicString = currentMusic == null ? "None" : currentMusic.getSound().getSoundAsOggLocation().toString();
        String recordString = closestRecord == null ? "None" : closestRecord.getSound().getSoundAsOggLocation().toString();

        TranslationTextComponent nowPlaying = new TranslationTextComponent("gui.musicdisplay.nowplaying");
        getFontRenderer().drawText(event.getMatrixStack(), nowPlaying.appendSibling(new TranslationTextComponent(musicString)), 5, event.getWindow().getScaledHeight() - 15, 0);
        if(closestRecord != null) {
            getFontRenderer().drawText(event.getMatrixStack(), nowPlaying.copyRaw().appendSibling(new TranslationTextComponent(recordString)), 5, event.getWindow().getScaledHeight() - 25, 0);
        }
    }

    @Override
    public void onPlaySound(ISound soundIn, SoundEventAccessor accessor) {
        if(soundIn.getCategory().equals(SoundCategory.MUSIC)) {
            currentMusic = soundIn;
        }
        if(soundIn.getCategory().equals(SoundCategory.RECORDS)) {
            currentRecords.put(soundPosition(soundIn), soundIn);
        }
    }

    private static double distanceToPlayer(BlockPos pos) {
        if(Minecraft.getInstance().player == null) return 128;
        return Math.sqrt(pos.distanceSq(Minecraft.getInstance().player.getPosition()));
    }

    private static BlockPos soundPosition(ISound sound) {
        if(sound == null) return BlockPos.ZERO;
        return new BlockPos(sound.getX(), sound.getY(), sound.getZ());
    }
}
