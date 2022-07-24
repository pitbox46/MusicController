package github.pitbox46.musiccontroller.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;

@Mixin(MusicManager.class)
public class MusicTickerMixin {
    @Shadow private SoundInstance currentMusic;
    @Shadow @Final private Minecraft minecraft;
    private ResourceLocation previousMusic;

    @Inject(at=@At(value="FIELD", target="net/minecraft/client/sounds/MusicManager.minecraft:Lnet/minecraft/client/Minecraft;", opcode=Opcodes.GETFIELD, ordinal=0), method="startPlaying")
    private void selectRandomBackgroundMusic(Music selector, CallbackInfo ci) {
        currentMusic.resolve(minecraft.getSoundManager());
        int i = 0;
        while(i++ < 3 && currentMusic.getSound().getPath().equals(previousMusic)) {
            currentMusic = SimpleSoundInstance.forMusic(selector.getEvent());
            currentMusic.resolve(minecraft.getSoundManager());
        }
        previousMusic = currentMusic.getSound().getPath();
    }
}
