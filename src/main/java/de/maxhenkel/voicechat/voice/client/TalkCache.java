package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

import java.util.*;

public class TalkCache {

    private static final long TIMEOUT = 500L;

    private final Map<UUID, Long> cache;

    public TalkCache() {
        this.cache = new HashMap<>();
    }

    public void updateTalking(UUID player) {
        cache.put(player, System.currentTimeMillis());
    }

    public boolean isTalking(PlayerEntity player) {
        return isTalking(player.getUUID());
    }

    public boolean isTalking(UUID player) {
        if (player.equals(Minecraft.getInstance().player.getUUID())) {
            Client client = Main.CLIENT_VOICE_EVENTS.getClient();
            if (client != null) {
                return client.getMicThread().isTalking();
            }
        }

        Long lastTalk = cache.getOrDefault(player, 0L);
        return System.currentTimeMillis() - lastTalk < TIMEOUT;
    }

}
