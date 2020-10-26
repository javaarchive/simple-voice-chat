package de.maxhenkel.voicechat.voice.common;

import net.minecraft.network.PacketBuffer;

public class SoundPacket implements Packet<SoundPacket> {

    private byte[] data;
    private boolean isCall;

    public SoundPacket(byte[] data, boolean isCall) {
        this.data = data;
        this.isCall = isCall;
    }

    public SoundPacket(byte[] data) {
        this(data, false);
    }

    public SoundPacket() {

    }

    public byte[] getData() {
        return data;
    }

    public boolean isCall() {
        return isCall;
    }

    @Override
    public SoundPacket fromBytes(PacketBuffer buf) {
        SoundPacket soundPacket = new SoundPacket();
        soundPacket.data = buf.readByteArray();
        soundPacket.isCall = buf.readBoolean();
        return soundPacket;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeByteArray(data);
        buf.writeBoolean(isCall);
    }
}
