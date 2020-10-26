package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.Main;
import de.maxhenkel.voicechat.voice.common.Call;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

public class AnswerCallMessage implements Message<AnswerCallMessage> {

    private UUID callerUUID;
    private boolean accept;

    public AnswerCallMessage(UUID callerUUID, boolean accept) {
        this.callerUUID = callerUUID;
        this.accept = accept;
    }

    public AnswerCallMessage() {

    }

    @Override
    public void executeServerSide(NetworkEvent.Context context) {
        Call call = Main.SERVER_VOICE_EVENTS.getServer().getCalls().get(callerUUID, context.getSender().getUniqueID());
        if (call == null) {
            //TODO if not found
            return;
        }
        if (!accept) {
            Main.SERVER_VOICE_EVENTS.getServer().getCalls().removeCall(callerUUID, context.getSender().getUniqueID());
        } else if (!call.isConnected()) {
            call.setConnected(true);
            context.getSender().sendMessage(new StringTextComponent("Connected with " + callerUUID));
            ServerPlayerEntity caller = context.getSender().server.getPlayerList().getPlayerByUUID(callerUUID);
            if (caller != null) {
                caller.sendMessage(new StringTextComponent("Connected with " + context.getSender().getUniqueID()));
            }
            Main.SIMPLE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> context.getSender()), new CallInfoMessage(Main.SERVER_VOICE_EVENTS.getServer().getCalls().getCalls(context.getSender().getUniqueID())));
        }
    }

    @Override
    public void executeClientSide(NetworkEvent.Context context) {

    }

    @Override
    public AnswerCallMessage fromBytes(PacketBuffer buf) {
        AnswerCallMessage message = new AnswerCallMessage();
        message.callerUUID = buf.readUniqueId();
        message.accept = buf.readBoolean();
        return message;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(callerUUID);
        buf.writeBoolean(accept);
    }
}
