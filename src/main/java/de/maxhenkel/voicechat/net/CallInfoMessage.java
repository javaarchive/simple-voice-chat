package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.gui.CallScreen;
import de.maxhenkel.voicechat.gui.PhoneScreen;
import de.maxhenkel.voicechat.voice.common.Call;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class CallInfoMessage implements Message<CallInfoMessage> {

    private List<Call> calls;

    public CallInfoMessage(List<Call> calls) {
        this.calls = calls;
    }

    public CallInfoMessage() {

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
        if (calls.isEmpty()) {
            Minecraft.getInstance().displayGuiScreen(new PhoneScreen());
        } else {
            Call connectedCall = calls.stream().filter(call -> call.isConnected()).findFirst().orElse(null);
            if (connectedCall == null) {
                Minecraft.getInstance().displayGuiScreen(new CallScreen(calls.get(0)));
            } else {
                Minecraft.getInstance().displayGuiScreen(new CallScreen(connectedCall));
            }
        }
    }


    @Override
    public CallInfoMessage fromBytes(PacketBuffer buf) {
        CallInfoMessage message = new CallInfoMessage();
        message.calls = new ArrayList<>();

        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            Call call = new Call(buf.readUniqueId(), buf.readUniqueId());
            call.setConnected(buf.readBoolean());
            message.calls.add(call);
        }

        return message;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(calls.size());

        for (Call call : calls) {
            buf.writeUniqueId(call.getCaller());
            buf.writeUniqueId(call.getReceiver());
            buf.writeBoolean(call.isConnected());
        }
    }
}
