package github.pitbox46.musiccontroller.mixins;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSoundInstance.class)
public abstract class LocatableSoundMixin {
    @Shadow @Final protected ResourceLocation location;

    @Shadow public abstract Sound getSound();

    @Inject(at = @At("HEAD"), method = "resolve", cancellable = true)
    private void createAccessor(SoundManager handler, CallbackInfoReturnable<WeighedSoundEvents> cir) {
        if(this.getSound() != null && this.getSound() != SoundManager.EMPTY_SOUND)
            cir.setReturnValue(handler.getSoundEvent(this.location));
    }
}
