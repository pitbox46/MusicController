package github.pitbox46.musiccontroller.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import github.pitbox46.musiccontroller.MusicInfo;
import github.pitbox46.musiccontroller.JsonHelper;
import io.netty.util.internal.AppendableCharSequence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class PlaylistScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private CurrentSongs currentSongs;
    private AvaliableSongs avaliableSongs;
    private final Screen previousScreen;
    private final String playlist;
    private TextFieldWidget search;
    private String lastSearch;

    protected PlaylistScreen(String playlist, Screen previousScreen, ITextComponent titleIn) {
        super(titleIn);
        this.previousScreen = previousScreen;
        this.playlist = playlist;
    }

    @Override
    protected void init() {
        this.currentSongs = new CurrentSongs(this.minecraft);
        this.avaliableSongs = new AvaliableSongs(this.minecraft);
        this.children.add(currentSongs);
        this.children.add(avaliableSongs);
        this.search = new TextFieldWidget(font, this.width / 2 - 155 + 160, this.height - 50, 150, 18, new StringTextComponent(""));
        this.addButton(new Button(this.width / 2 - 155, this.height - 51, 150, 20, DialogTexts.GUI_DONE, (button) -> {
            assert this.minecraft != null;
            this.closeScreen();
        }));
        super.init();
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return search.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(search.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if(search.isFocused()) {
            if(keyCode == 256) {
                search.setFocused2(false);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void tick() {
        this.search.tick();
        if(lastSearch == null || !lastSearch.equals(search.getText())) {
            lastSearch = search.getText();
            this.currentSongs.searchList(lastSearch);
            this.avaliableSongs.searchList(lastSearch);
            this.currentSongs.setScrollAmount(0);
            this.avaliableSongs.setScrollAmount(0);
        }
    }

    @Override
    public void closeScreen() {
        assert this.minecraft != null;
        this.minecraft.displayGuiScreen(previousScreen);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.currentSongs.render(matrixStack, mouseX, mouseY, partialTicks);
        this.avaliableSongs.render(matrixStack, mouseX, mouseY, partialTicks);
        this.search.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 15, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(search.mouseClicked(mouseX, mouseY, button)) return true;
        for(IGuiEventListener eventListener: this.getEventListeners()) {
            if(eventListener.mouseClicked(mouseX, mouseY, button)) {
                this.setListener(eventListener);
                return true;
            }
        }
        return true;
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        lastSearch = search.getText();
        this.currentSongs.searchList(lastSearch);
        this.avaliableSongs.searchList(lastSearch);
        this.currentSongs.setScrollAmount(0);
        this.avaliableSongs.setScrollAmount(0);
    }

    @OnlyIn(Dist.CLIENT)
    class CurrentSongs extends FilterableList<ResourceLocation,CurrentSongs.MusicEntry> {
        public CurrentSongs(Minecraft mcIn) {
            super(mcIn, PlaylistScreen.this.width/2, PlaylistScreen.this.height, 32, PlaylistScreen.this.height - 65 + 4, 26);
            JsonHelper jsonHelper = new JsonHelper(mcIn);
            try {
                Map<String, SoundList> map = jsonHelper.soundsReader(new ResourceLocation("minecraft", "sounds.json"));
                SoundList soundList = map.get(playlist);
                for(Sound sound: soundList.getSounds()) {
                    this.addWithKey(sound.getSoundLocation());
                }
            } catch (IOException e) {
                System.out.println("Issue reading modified sounds.json");
                e.printStackTrace();
            }
        }

        @Override
        public void removeWithKey(ResourceLocation song) {
            this.removeEntry(ENTRY_MAP.get(song));
            ENTRY_MAP.remove(song);
        }

        @Override
        public void addWithKey(ResourceLocation song) {
            CurrentSongs.MusicEntry entry = new CurrentSongs.MusicEntry(song);
            this.addEntry(entry);
            ENTRY_MAP.put(song, entry);
        }

        @Override
        public void searchList(String string) {
            filterList(entry -> {
                String temp = MusicInfo.getDisplayText(entry.getKey()).getString();
                if(string.length() == 0) return true;
                AppendableCharSequence charSequence = new AppendableCharSequence(string.length());
                for(char c: string.toLowerCase().toCharArray()) charSequence.append(c);
                return temp.toLowerCase().contains(charSequence);
            });
        }

        @Override
        public Collection<MusicEntry> sortList(Map<ResourceLocation, MusicEntry> entryMap) {
            Map<String, MusicEntry> tempMap = new TreeMap<>();
            entryMap.forEach((sound, entry) -> tempMap.put(MusicInfo.getDisplayText(sound).getString(), entry));
            return tempMap.values();
        }

        @Override
        protected int getScrollbarPosition() {
            return PlaylistScreen.this.width/2-6;
        }

        public class MusicEntry extends ExtendedList.AbstractListEntry<MusicEntry> {
            private final ResourceLocation sound;
            private static final int BUTTONX = -50;
            private static final int BUTTONY = 0;
            private static final int BUTTONWIDTH = 10;
            private static final int BUTTONHEIGHT = 10;

            public MusicEntry(ResourceLocation sound) {
                this.sound = sound;
            }

            @Override
            public void render(MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                String s = sound.toString();
                ITextComponent t = MusicInfo.getDisplayText(sound);
                PlaylistScreen.this.font.drawTextWithShadow(matrixStack, t, 10, (float)(top + 1), 16777215);
                PlaylistScreen.this.font.drawStringWithTransparency(matrixStack, s, 10, (float)(top + 11), ColorHelper.PackedColor.packColor(255,84,84,84), true);

                assert PlaylistScreen.this.minecraft != null;
                PlaylistScreen.this.minecraft.getTextureManager().bindTexture(new ResourceLocation("musiccontroller", "textures/controller_buttons.png"));
                if(mouseX >= left + width + BUTTONX && mouseX <= left + width + BUTTONX + BUTTONWIDTH
                        && mouseY >= top + BUTTONY && mouseY <= top + BUTTONY + BUTTONHEIGHT) {
                    blit(matrixStack, left + width + BUTTONX, (top + BUTTONY), 10, 10, 10, 10, 32, 32);
                }
                else blit(matrixStack, left + width + BUTTONX, (top + BUTTONY), 10, 0, 10, 10, 32, 32);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                double relX = mouseX - CurrentSongs.this.getRowLeft();
                double relY = mouseY - CurrentSongs.this.getRowTop(CurrentSongs.this.getEventListeners().indexOf(this));
                if(relX >= CurrentSongs.this.getRowWidth() + BUTTONX && relX <= CurrentSongs.this.getRowWidth() + BUTTONX + BUTTONWIDTH
                        && relY >= BUTTONY && relY <= BUTTONY + BUTTONHEIGHT) {
                    JsonHelper.removeSound(PlaylistScreen.this.playlist, sound);
                    PlaylistScreen.this.avaliableSongs.addWithKey(sound);
                    PlaylistScreen.this.currentSongs.removeWithKey(sound);
                }
                return true;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    class AvaliableSongs extends FilterableList<ResourceLocation,AvaliableSongs.MusicEntry> {
        public AvaliableSongs(Minecraft mcIn) {
            super(mcIn, PlaylistScreen.this.width/2, PlaylistScreen.this.height, 32, PlaylistScreen.this.height - 65 + 4, 26);
            setLeftPos(PlaylistScreen.this.width/2);
            JsonHelper jsonHelper = new JsonHelper(mcIn);
            try {
                Map<String, SoundList> jsonSoundList = jsonHelper.soundsReader(new ResourceLocation("minecraft", "sounds.json"));

                List<ResourceLocation> jsonSounds = new ArrayList<>();
                jsonSoundList.get(playlist).getSounds().forEach(sound -> jsonSounds.add(sound.getSoundLocation()));

                Map<ResourceLocation, MusicInfo> otherTracks = MusicInfo.getRegisteredTracks();
                for(Map.Entry<ResourceLocation, MusicInfo> entry: otherTracks.entrySet()) {
                    if(!jsonSounds.contains(entry.getKey())) {
                        this.addWithKey(entry.getKey());
                    }
                }

            } catch (IOException e) {
                System.out.println("Issue reading modified sounds.json");
                e.printStackTrace();
            }
        }

        @Override
        public void removeWithKey(ResourceLocation song) {
            this.removeEntry(ENTRY_MAP.get(song));
            ENTRY_MAP.remove(song);
        }

        @Override
        public void addWithKey(ResourceLocation song) {
            AvaliableSongs.MusicEntry entry = new AvaliableSongs.MusicEntry(song);
            this.addEntry(entry);
            ENTRY_MAP.put(song, entry);
        }

        @Override
        public void searchList(String string) {
            filterList(entry -> {
                String temp = MusicInfo.getDisplayText(entry.getKey()).getString();
                if(string.length() == 0) return true;
                AppendableCharSequence charSequence = new AppendableCharSequence(string.length());
                for(char c: string.toLowerCase().toCharArray()) charSequence.append(c);
                return temp.toLowerCase().contains(charSequence);
            });
        }

        @Override
        public Collection<MusicEntry> sortList(Map<ResourceLocation,MusicEntry> entryMap) {
            Map<String,MusicEntry> tempMap = new TreeMap<>();
            entryMap.forEach((sound, entry) -> tempMap.put(MusicInfo.getDisplayText(sound).getString(), entry));
            return tempMap.values();
        }

        @Override
        protected int getScrollbarPosition() {
            return PlaylistScreen.this.width-6;
        }

        public class MusicEntry extends ExtendedList.AbstractListEntry<MusicEntry> {
            private final ResourceLocation sound;
            private static final int BUTTONX = -50;
            private static final int BUTTONY = 0;
            private static final int BUTTONWIDTH = 10;
            private static final int BUTTONHEIGHT = 10;

            public MusicEntry(ResourceLocation sound) {
                this.sound = sound;
            }

            @Override
            public void render(MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                String s = sound.toString();
                ITextComponent t = MusicInfo.getDisplayText(sound);
                PlaylistScreen.this.font.drawTextWithShadow(matrixStack, t, (float)(PlaylistScreen.this.width/2 + 10), (float)(top + 1), 16777215);
                PlaylistScreen.this.font.drawStringWithTransparency(matrixStack, s, (float)(PlaylistScreen.this.width/2 + 10), (float)(top + 11), ColorHelper.PackedColor.packColor(255,84,84,84), true);

                assert PlaylistScreen.this.minecraft != null;
                PlaylistScreen.this.minecraft.getTextureManager().bindTexture(new ResourceLocation("musiccontroller", "textures/controller_buttons.png"));
                if(mouseX >= left + width + BUTTONX && mouseX <= left + width + BUTTONX + BUTTONWIDTH
                        && mouseY >= top + BUTTONY && mouseY <= top + BUTTONY + BUTTONHEIGHT) {
                    blit(matrixStack, left + width + BUTTONX, (top + BUTTONY), 0, 0, 10, 10, 10, 32, 32);
                }
                else blit(matrixStack, left + width + BUTTONX, (top + BUTTONY), 0, 0, 0, 10, 10, 32, 32);

            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                double relX = mouseX - AvaliableSongs.this.getRowLeft();
                double relY = mouseY - AvaliableSongs.this.getRowTop(AvaliableSongs.this.getEventListeners().indexOf(this));
                if(relX >= AvaliableSongs.this.getRowWidth() + BUTTONX && relX <= AvaliableSongs.this.getRowWidth() + BUTTONX + BUTTONWIDTH
                        && relY >= BUTTONY && relY <= BUTTONY + BUTTONHEIGHT) {
                    JsonHelper.addSound(PlaylistScreen.this.playlist, sound);
                    PlaylistScreen.this.currentSongs.addWithKey(sound);
                    PlaylistScreen.this.avaliableSongs.removeWithKey(sound);
                }
                return true;
            }
        }
    }
}
