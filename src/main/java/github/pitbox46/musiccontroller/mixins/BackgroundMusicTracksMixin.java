package github.pitbox46.musiccontroller.mixins;

import github.pitbox46.musiccontroller.Config;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundMusicTracks.class)
public class BackgroundMusicTracksMixin {
    @Inject(at = @At("HEAD"), method = "getDefaultBackgroundMusicSelector(Lnet/minecraft/util/SoundEvent;)Lnet/minecraft/client/audio/BackgroundMusicSelector;", cancellable = true)
    private static void getDefaultBackgroundMusicSelector(SoundEvent soundEvent, CallbackInfoReturnable<BackgroundMusicSelector> cir) {
        cir.setReturnValue(new BackgroundMusicSelector(soundEvent, Config.OTHER_MUSIC_RANGE.get().get(0), Config.OTHER_MUSIC_RANGE.get().get(1), false));
    }
}
