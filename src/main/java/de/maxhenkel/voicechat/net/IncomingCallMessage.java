package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.gui.CallScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class IncomingCallMessage implements Message<IncomingCallMessage> {

    private UUID callerUUID;
    private UUID receiverUUID;

    public IncomingCallMessage(UUID callerUUID, UUID receiverUUID) {
        this.callerUUID = callerUUID;
        this.receiverUUID = receiverUUID;
    }

    public IncomingCallMessage() {

    }

    @Override
    public void executeServerSide(NetworkEvent.Context context) {

    }

    @Override
    public void executeClientSide(NetworkEvent.Context context) {
        onGetCalled();
    }

    @OnlyIn(Dist.CLIENT)
    private void onGetCalled() {
        //Minecraft.getInstance().displayGuiScreen(new CallScreen(callerUUID, true));
        //TODO
    }

    @Override
    public IncomingCallMessage fromBytes(PacketBuffer buf) {
        IncomingCallMessage message = new IncomingCallMessage();
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
