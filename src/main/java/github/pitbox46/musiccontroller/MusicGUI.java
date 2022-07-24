package github.pitbox46.musiccontroller;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
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
public class MusicGUI extends Gui {
    private MusicState musicState;
    private Pair<SoundInstance, Channel> currentMusic;
    private Map<BlockPos, SoundInstance> currentRecords = new HashMap<>();
    private SoundInstance closestRecord;

    public MusicGUI() {
        super(Minecraft.getInstance());
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        Options gameSettings = this.minecraft.options;
        if(gameSettings.renderDebug || gameSettings.hideGui || Config.HIDE_CONTROLLER.get()) return;
        if(event.getType() != RenderGameOverlayEvent.ElementType.LAYER || event.getMatrixStack() == null) return;

        ResourceLocation[] musicStrings = renderLogic();
        ArrayList<Component> stringList = new ArrayList<>();
        TranslatableComponent nowPlaying = new TranslatableComponent("gui.musiccontroller.nowplaying");

        if(musicState != MusicState.OFF) {
            if(musicState == MusicState.PAUSED) stringList.add(new TranslatableComponent("gui.musiccontroller.musicpaused"));
            else stringList.add(new TranslatableComponent("gui.musiccontroller.musicon"));
        }
        else stringList.add(new TranslatableComponent("gui.musiccontroller.musicoff"));
        if(musicStrings[0] != null) stringList.add(nowPlaying.plainCopy().append(MusicInfo.getMusicInfo(musicStrings[0]).getDisplayText()));
        if(musicStrings[1] != null) stringList.add(nowPlaying.plainCopy().append(MusicInfo.getMusicInfo(musicStrings[1]).getDisplayText()));

        int y = 5;
        for(Component text: stringList) {
            getFont().draw(event.getMatrixStack(), text, 5, y, ChatFormatting.WHITE.getColor());
            y += 10;
        }
//        //Rebind texture
//        minecraft.getTextureManager().bind(GUI_ICONS_LOCATION);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        Options gameSettings = this.minecraft.options;
        if(!gameSettings.renderDebug || gameSettings.hideGui) return;

        ResourceLocation[] musicStrings = renderLogic();
        event.getRight().add(9,"");
        event.getRight().add(10,ChatFormatting.UNDERLINE + "Music Controller");
        event.getRight().add(11,"Music: " + musicState.name());
        event.getRight().add(12,"Background Track: " + (musicStrings[0] == null ? "None" : musicStrings[0].toString()));
        event.getRight().add(13,"Record Track: " + (musicStrings[1] == null ? "None" : musicStrings[1].toString()));
    }

    @SubscribeEvent
    public void onPlaySound(SoundEvent.SoundSourceEvent event) {
        if(event.getSound().getSource().equals(SoundSource.MUSIC)) {
            if(musicState != MusicState.ON) minecraft.getSoundManager().stop(event.getSound());
            else currentMusic = new Pair<>(event.getSound(), event.getChannel());
        }
        if(event.getSound().getSource().equals(SoundSource.RECORDS)) {
            currentRecords.put(soundPosition(event.getSound()), event.getSound());
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(MusicController.nextTrack.consumeClick() && this.musicState != MusicState.OFF) {
            if(this.musicState == MusicState.PAUSED){
                currentMusic.getSecond().unpause();
                this.musicState = MusicState.ON;
            }
            minecraft.getMusicManager().stopPlaying();
            minecraft.getMusicManager().startPlaying(minecraft.getSituationalMusic());
        }
        if(MusicController.togglePauseTrack.consumeClick() && currentMusic != null && musicState != MusicState.OFF){
            if (this.musicState != MusicState.PAUSED) {
                currentMusic.getSecond().pause();
                this.musicState = MusicState.PAUSED;
            }
            else {
                currentMusic.getSecond().unpause();
                this.musicState = MusicState.ON;
            }
        }
        if(MusicController.toggleMusic.consumeClick()) {
            if (this.musicState != MusicState.ON) {
                minecraft.getMusicManager().startPlaying(minecraft.getSituationalMusic());
                this.musicState = MusicState.ON;
            }
            else {
                minecraft.getMusicManager().stopPlaying();
                this.musicState = MusicState.OFF;
            }
        }
    }

    private ResourceLocation[] renderLogic() {
        if (currentMusic == null || !super.minecraft.getSoundManager().isActive(currentMusic.getFirst()))
            currentMusic = null;

        Map<BlockPos, SoundInstance> tempMap = new HashMap<>(currentRecords);
        for (BlockPos pos : currentRecords.keySet()) {
            SoundInstance sound = tempMap.get(pos);
            if (!super.minecraft.getSoundManager().isActive(sound)) {
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

        return new ResourceLocation[]{currentMusic == null ? null : currentMusic.getFirst().getSound().getLocation(),
                            closestRecord == null ? null : closestRecord.getSound().getLocation()};
    }

    private static double distanceToPlayer(BlockPos pos) {
        return Minecraft.getInstance().player != null ? Math.sqrt(pos.distSqr(Minecraft.getInstance().player.blockPosition())) : 1024;
    }

    private static BlockPos soundPosition(SoundInstance sound) {
        return new BlockPos(sound.getX(), sound.getY(), sound.getZ());
    }

    public enum MusicState {
        OFF,
        ON,
        PAUSED
    }
}
