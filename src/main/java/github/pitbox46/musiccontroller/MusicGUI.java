package github.pitbox46.musiccontroller;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class MusicGUI extends IngameGui {
    private boolean isMusicOn = true;
    private boolean isPaused = false;
    private Pair<ISound, SoundSource> currentMusic;
    private Map<BlockPos, ISound> currentRecords = new HashMap<>();
    private ISound closestRecord;

    public MusicGUI() {
        super(Minecraft.getInstance());
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        GameSettings gameSettings = this.mc.gameSettings;
        if(gameSettings.showDebugInfo || gameSettings.hideGUI || Config.HIDE_CONTROLLER.get()) return;
        if(event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS || event.getMatrixStack() == null) return;

        String[] musicStrings = renderLogic();
        ArrayList<ITextComponent> stringList = new ArrayList<>();
        TranslationTextComponent nowPlaying = new TranslationTextComponent("gui.musiccontroller.nowplaying");

        if(isMusicOn) {
            if(isPaused) stringList.add(new TranslationTextComponent("gui.musiccontroller.musicpaused"));
            else stringList.add(new TranslationTextComponent("gui.musiccontroller.musicon"));
        }
        else stringList.add(new TranslationTextComponent("gui.musiccontroller.musicoff"));
        if(musicStrings[0] != null) stringList.add(nowPlaying.copyRaw().appendSibling(new TranslationTextComponent(musicStrings[0])));
        if(musicStrings[1] != null) stringList.add(nowPlaying.copyRaw().appendSibling(new TranslationTextComponent(musicStrings[1])));

        int y = 5;
        for(ITextComponent text: stringList) {
            getFontRenderer().drawText(event.getMatrixStack(), text, 5, y, TextFormatting.WHITE.getColor());
            y += 10;
        }
        //Rebind texture
        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        GameSettings gameSettings = this.mc.gameSettings;
        if(!gameSettings.showDebugInfo || gameSettings.hideGUI) return;

        String[] musicStrings = renderLogic();
        event.getRight().add(9,"");
        event.getRight().add(10,TextFormatting.UNDERLINE + "Music Controller");
        event.getRight().add(11,"Music: " + (isMusicOn ? (isPaused ? "Paused" : "On") : "Off"));
        event.getRight().add(12,"Background Track: "+musicStrings[0]);
        event.getRight().add(13,"Record Track: "+musicStrings[1]);
    }

    @SubscribeEvent
    public void onPlaySound(SoundEvent.SoundSourceEvent event) {
        if(event.getSound().getCategory().equals(SoundCategory.MUSIC)) {
            if(!isMusicOn) mc.getSoundHandler().stop(event.getSound());
            else currentMusic = new Pair<>(event.getSound(), event.getSource());
        }
        if(event.getSound().getCategory().equals(SoundCategory.RECORDS)) {
            currentRecords.put(soundPosition(event.getSound()), event.getSound());
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(MusicController.nextTrack.isPressed() && this.isMusicOn) {
            if(this.isPaused){
                currentMusic.getSecond().resume();
                this.isPaused = false;
            }
            mc.getMusicTicker().stop();
            mc.getMusicTicker().selectRandomBackgroundMusic(mc.getBackgroundMusicSelector());
        }
        if(MusicController.togglePauseTrack.isPressed() && currentMusic != null && isMusicOn){
            this.isPaused = !this.isPaused;
            if (this.isPaused) currentMusic.getSecond().pause();
            else currentMusic.getSecond().resume();
        }
        if(MusicController.toggleMusic.isPressed()) {
            this.isMusicOn = !this.isMusicOn;
            if (this.isMusicOn) mc.getMusicTicker().selectRandomBackgroundMusic(mc.getBackgroundMusicSelector());
            else mc.getMusicTicker().stop();
        }
    }

    private String[] renderLogic() {
        if (currentMusic == null || !super.mc.getSoundHandler().isPlaying(currentMusic.getFirst()))
            currentMusic = null;

        Map<BlockPos, ISound> tempMap = new HashMap<>(currentRecords);
        for (BlockPos pos : currentRecords.keySet()) {
            ISound sound = tempMap.get(pos);
            if (!super.mc.getSoundHandler().isPlaying(sound)) {
                tempMap.remove(pos);
                continue;
            }
            double distanceSq = distanceToPlayer(pos);
            if (closestRecord == null || (distanceSq < 64 && distanceSq < distanceToPlayer(soundPosition(closestRecord))))
                closestRecord = tempMap.get(pos);
        }
        currentRecords = tempMap;
        if (closestRecord != null && (currentRecords.isEmpty() || distanceToPlayer(soundPosition(closestRecord)) > 64))
            closestRecord = null;

        return new String[]{currentMusic == null ? null : currentMusic.getFirst().getSound().getSoundAsOggLocation().toString(),
                            closestRecord == null ? null : closestRecord.getSound().getSoundAsOggLocation().toString()};
    }

    private static double distanceToPlayer(BlockPos pos) {
        return Minecraft.getInstance().player != null ? Math.sqrt(pos.distanceSq(Minecraft.getInstance().player.getPosition())) : 1024;
    }

    private static BlockPos soundPosition(ISound sound) {
        return new BlockPos(sound.getX(), sound.getY(), sound.getZ());
    }
}
