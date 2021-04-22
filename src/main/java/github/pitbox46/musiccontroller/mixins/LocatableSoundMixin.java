package github.pitbox46.musiccontroller.mixins;

import net.minecraft.client.audio.LocatableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocatableSound.class)
public abstract class LocatableSoundMixin {
    @Shadow @Final protected ResourceLocation positionedSoundLocation;

    @Shadow public abstract Sound getSound();

    @Inject(at = @At("HEAD"), method = "createAccessor(Lnet/minecraft/client/audio/SoundHandler;)Lnet/minecraft/client/audio/SoundEventAccessor;", cancellable = true)
    private void createAccessor(SoundHandler handler, CallbackInfoReturnable<SoundEventAccessor> cir) {
        if(this.getSound() != null && this.getSound() != SoundHandler.MISSING_SOUND)
            cir.setReturnValue(handler.getAccessor(this.positionedSoundLocation));
    }
}
