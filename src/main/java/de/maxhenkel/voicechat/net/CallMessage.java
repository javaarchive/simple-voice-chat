package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.Main;
import de.maxhenkel.voicechat.ModSounds;
import de.maxhenkel.voicechat.voice.common.Call;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

public class CallMessage implements Message<CallMessage> {

    private UUID callerUUID;
    private UUID receiverUUID;

    public CallMessage(UUID callerUUID, UUID receiverUUID) {
        this.callerUUID = callerUUID;
        this.receiverUUID = receiverUUID;
    }

    public CallMessage() {

    }

    @Override
    public void executeServerSide(NetworkEvent.Context context) {
        ServerPlayerEntity receiver = context.getSender().server.getPlayerList().getPlayerByUUID(receiverUUID);
        if (receiver != null) {
            Main.SIMPLE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> receiver), new IncomingCallMessage(callerUUID, receiverUUID));
            receiver.playSound(ModSounds.PHONE, SoundCategory.MASTER, 1F, 1F);
            Main.SERVER_VOICE_EVENTS.getServer().getCalls().addCall(new Call(callerUUID, receiverUUID));
        }
        //TODO handle no receiver
    }

    @Override
    public void executeClientSide(NetworkEvent.Context context) {

    }

    @Override
    public CallMessage fromBytes(PacketBuffer buf) {
        CallMessage message = new CallMessage();
        message.callerUUID = buf.readUniqueId();
        message.receiverUUID = buf.readUniqueId();
        return message;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(callerUUID);
        buf.writeUniqueId(receiverUUID);
    }
}
