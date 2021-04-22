package github.pitbox46.musiccontroller.mixins;

import github.pitbox46.musiccontroller.MusicController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(MusicTicker.class)
public class MusicTickerMixin {
    @Shadow private ISound currentMusic;
    @Shadow @Final private Minecraft client;
    private ResourceLocation previousMusic;

    @Inject(at=@At(value="FIELD", target="net/minecraft/client/audio/MusicTicker.client:Lnet/minecraft/client/Minecraft;", opcode=Opcodes.GETFIELD, ordinal=0), method="selectRandomBackgroundMusic(Lnet/minecraft/client/audio/BackgroundMusicSelector;)V")
    private void selectRandomBackgroundMusic(BackgroundMusicSelector selector, CallbackInfo ci) {
        currentMusic.createAccessor(client.getSoundHandler());
        int i = 0;
        while(i++ < 3 && currentMusic.getSound().getSoundAsOggLocation().equals(previousMusic)) {
            currentMusic = SimpleSound.music(selector.getSoundEvent());
            currentMusic.createAccessor(client.getSoundHandler());
        }
        previousMusic = currentMusic.getSound().getSoundAsOggLocation();
    }
}
