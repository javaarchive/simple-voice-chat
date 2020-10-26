package de.maxhenkel.voicechat.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.maxhenkel.voicechat.Main;
import de.maxhenkel.voicechat.net.AnswerCallMessage;
import de.maxhenkel.voicechat.voice.common.Call;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class CallScreen extends Screen {

    protected static final int FONT_COLOR = 4210752;

    private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/gui/gui_adjust_volume.png");

    private int guiLeft;
    private int guiTop;
    private int xSize;
    private int ySize;

    private NetworkPlayerInfo caller;
    private Call call;
    private boolean isCaller;

    private Button accept;
    private Button decline;

    public CallScreen(Call call) {
        super(new TranslationTextComponent("gui.phone.title"));
        xSize = 248;
        ySize = 84;

        Minecraft minecraft = Minecraft.getInstance();
        //TODO check crash
        this.caller = minecraft.getConnection().getPlayerInfo(call.getPartner(minecraft.player.getUniqueID()));
        this.call = call;
        this.isCaller = call.getCaller().equals(minecraft.player.getUniqueID());
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        accept = new Button(guiLeft + 10, guiTop + 60, 60, 20, new TranslationTextComponent("message.accept").getFormattedText(), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new AnswerCallMessage(caller.getGameProfile().getId(), true));
        });

        decline = new Button(guiLeft + xSize - 80, guiTop + 60, 60, 20, new TranslationTextComponent("message.decline").getFormattedText(), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new AnswerCallMessage(caller.getGameProfile().getId(), false));
            minecraft.displayGuiScreen(null);
        });

        accept.active = !call.isConnected();
        //decline.active = call.isConnected();

        addButton(accept);
        addButton(decline);
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
        String title = isCaller || call.isConnected() ? new TranslationTextComponent("message.outgoing_call", caller.getGameProfile().getName()).getFormattedText() : new TranslationTextComponent("message.incoming_call", caller.getGameProfile().getName()).getFormattedText();
        int titleWidth = font.getStringWidth(title);
        font.drawString(title, (float) (guiLeft + (xSize - titleWidth) / 2), guiTop + 7, FONT_COLOR);
    }
}
