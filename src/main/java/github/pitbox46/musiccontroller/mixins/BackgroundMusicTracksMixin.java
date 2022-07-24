package github.pitbox46.musiccontroller.mixins;

import github.pitbox46.musiccontroller.Config;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Musics.class)
public class BackgroundMusicTracksMixin {
    @Inject(at = @At("HEAD"), method = "createGameMusic", cancellable = true)
    private static void getDefaultBackgroundMusicSelector(SoundEvent soundEvent, CallbackInfoReturnable<Music> cir) {
        cir.setReturnValue(new Music(soundEvent, Config.OTHER_MUSIC_RANGE.get().get(0), Config.OTHER_MUSIC_RANGE.get().get(1), false));
    }
}
