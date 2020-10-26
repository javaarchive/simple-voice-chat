package de.maxhenkel.voicechat.voice.server;

import de.maxhenkel.voicechat.voice.common.Call;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Calls {

    private List<Call> calls;

    public Calls() {
        calls = new ArrayList<>();
    }

    public Call byCaller(UUID caller) {
        return calls.stream().filter(call -> call.getCaller().equals(caller)).findFirst().orElse(null);
    }

    public Call byReceiver(UUID receiver) {
        return calls.stream().filter(call -> call.getReceiver().equals(receiver)).findFirst().orElse(null);
    }

    public Call get(UUID caller, UUID receiver) {
        return calls.stream().filter(call -> call.getReceiver().equals(receiver) && call.getCaller().equals(caller)).findFirst().orElse(null);
    }

    public void addCall(Call c) {
        calls.add(c);
    }

    public List<UUID> getConnections(UUID caller) {
        return calls.stream().filter(Call::isConnected).filter(call -> call.getCaller().equals(caller) || call.getReceiver().equals(caller)).map(call -> call.getCaller().equals(caller) ? call.getReceiver() : call.getCaller()).collect(Collectors.toList());
    }

    public List<Call> getCalls(UUID caller) {
        return calls.stream().filter(call -> call.getCaller().equals(caller) || call.getReceiver().equals(caller)).collect(Collectors.toList());
    }

    public void removeCall(UUID caller, UUID receiver) {
        calls.removeIf(call -> call.getReceiver().equals(receiver) && call.getCaller().equals(caller));
    }

}
