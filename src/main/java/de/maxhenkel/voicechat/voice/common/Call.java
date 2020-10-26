package de.maxhenkel.voicechat.voice.common;

import java.util.UUID;

public class Call {

    private UUID caller, receiver;
    private boolean connected;

    public Call(UUID caller, UUID receiver) {
        this.caller = caller;
        this.receiver = receiver;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public UUID getCaller() {
        return caller;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public UUID getPartner(UUID ownUUID) {
        return ownUUID.equals(caller) ? receiver : caller;
    }
}
