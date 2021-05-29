package de.maxhenkel.voicechat.voice.common;

import net.minecraft.network.PacketBuffer;

public class MicPacket implements Packet<MicPacket> {

    private byte[] data;
    private long sequenceNumber;
    private float x;
    private float y;
    private float z;

    public MicPacket(byte[] data, long sequenceNumber) {
        this.data = data;
        this.sequenceNumber = sequenceNumber;
    }

    public MicPacket() {

    }

    public byte[] getData() {
        return data;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public MicPacket fromBytes(PacketBuffer buf) {
        MicPacket soundPacket = new MicPacket();
        soundPacket.data = buf.readByteArray();
        soundPacket.sequenceNumber = buf.readLong();
        soundPacket.x = buf.readFloat();
        soundPacket.y = buf.readFloat();
        soundPacket.z = buf.readFloat();
        return soundPacket;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeByteArray(data);
        buf.writeLong(sequenceNumber);
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
    }
}
