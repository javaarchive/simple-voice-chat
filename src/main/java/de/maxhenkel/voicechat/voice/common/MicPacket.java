package de.maxhenkel.voicechat.voice.common;

import net.minecraft.network.PacketBuffer;

public class MicPacket implements Packet<MicPacket> {

    private byte[] data;
    private long sequenceNumber;
    private int x;
    private int y;
    private int z;

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
        soundPacket.x = buf.readInt();
        soundPacket.y = buf.readInt();
        soundPacket.z = buf.readInt();
        return soundPacket;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeByteArray(data);
        buf.writeLong(sequenceNumber);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
}
