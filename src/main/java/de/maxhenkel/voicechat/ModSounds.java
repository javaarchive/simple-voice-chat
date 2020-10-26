package de.maxhenkel.voicechat;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModSounds {

    public static SoundEvent PHONE = registerSound("phone");

    public static SoundEvent registerSound(String soundName) {
        SoundEvent event = new SoundEvent(new ResourceLocation(Main.MODID, soundName));
        event.setRegistryName(new ResourceLocation(Main.MODID, soundName));
        return event;
    }

}
