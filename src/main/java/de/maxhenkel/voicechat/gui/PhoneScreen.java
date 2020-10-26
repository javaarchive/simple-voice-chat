package de.maxhenkel.voicechat.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.maxhenkel.voicechat.Main;
import de.maxhenkel.voicechat.net.CallMessage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class PhoneScreen extends Screen {

    protected static final int FONT_COLOR = 4210752;

    private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/gui/gui_adjust_volume.png");

    private int guiLeft;
    private int guiTop;
    private int xSize;
    private int ySize;

    private List<NetworkPlayerInfo> players;
    private int index;

    private Button previous;
    private Button call;
    private Button next;

    public PhoneScreen() {
        super(new TranslationTextComponent("gui.phone.title"));
        xSize = 248;
        ySize = 84;
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        //TODO check crash
        players = minecraft.getConnection().getPlayerInfoMap().stream().filter(networkPlayerInfo -> !networkPlayerInfo.getGameProfile().getId().equals(minecraft.player.getUniqueID())).collect(Collectors.toList());

        previous = new Button(guiLeft + 10, guiTop + 60, 60, 20, new TranslationTextComponent("message.previous").getFormattedText(), button -> {
            index = (index - 1 + players.size()) % players.size();
            updateButtons();
        });

        call = new Button(guiLeft + xSize / 2 - 30, guiTop + 60, 60, 20, new TranslationTextComponent("message.call").getFormattedText(), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new CallMessage(minecraft.player.getUniqueID(), getCurrentPlayer().getGameProfile().getId()));
        });

        next = new Button(guiLeft + xSize - 80, guiTop + 60, 60, 20, new TranslationTextComponent("message.next").getFormattedText(), button -> {
            index = (index + 1) % players.size();
            updateButtons();
        });

        addButton(previous);
        addButton(call);
        addButton(next);

        updateButtons();
    }

    public void updateButtons() {
        if (players.size() <= 1) {
            next.active = false;
            previous.active = false;
        }
        call.active = getCurrentPlayer() != null;
    }

    public NetworkPlayerInfo getCurrentPlayer() {
        if (players.size() <= 0) {
            return null;
        }
        return players.get(index);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == minecraft.gameSettings.keyBindInventory.getKey().getKeyCode() || keyCode == Main.KEY_VOICE_CHAT_SETTINGS.getKey().getKeyCode()) {
            minecraft.displayGuiScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();

        RenderSystem.color4f(1F, 1F, 1F, 1F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);

        super.render(mouseX, mouseY, partialTicks);

        // Title
        String title = new TranslationTextComponent("message.call_player").getFormattedText();
        //getCurrentPlayer() == null ? new TranslationTextComponent("message.no_player").getFormattedText() : new TranslationTextComponent("message.adjust_volume_player", getCurrentPlayer().getDisplayName()).getFormattedText();
        int titleWidth = font.getStringWidth(title);
        font.drawString(title, (float) (guiLeft + (xSize - titleWidth) / 2), guiTop + 7, FONT_COLOR);

        NetworkPlayerInfo player = getCurrentPlayer();
        if (player != null) {
            String name = player.getGameProfile().getName();
            int nameWidth = font.getStringWidth(title);
            font.drawString(name, (float) (guiLeft + (xSize - nameWidth) / 2), guiTop + 27, FONT_COLOR);
        }
    }
}
