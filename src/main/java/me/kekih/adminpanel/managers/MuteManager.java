package me.kekih.adminpanel.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MuteManager {

    // UUID -> unmute timestamp (0 = permanent)
    private final Map<UUID, Long> muted = new ConcurrentHashMap<>();

    public void mute(UUID uuid, long durationMillis) {
        long until = durationMillis <= 0 ? 0L : System.currentTimeMillis() + durationMillis;
        muted.put(uuid, until);
    }

    public void unmute(UUID uuid) {
        muted.remove(uuid);
    }

    public boolean isMuted(UUID uuid) {
        Long until = muted.get(uuid);
        if (until == null) return false;
        if (until == 0L) return true;
        if (System.currentTimeMillis() >= until) {
            muted.remove(uuid);
            return false;
        }
        return true;
    }

    public boolean toggleMute(UUID uuid) {
        if (isMuted(uuid)) {
            unmute(uuid);
            return false;
        } else {
            mute(uuid, 0); // permanent by default
            return true;
        }
    }
}
