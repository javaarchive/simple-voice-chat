package de.maxhenkel.voicechat;

import de.maxhenkel.corelib.CommonRegistry;
import de.maxhenkel.voicechat.command.VoicechatCommands;
import de.maxhenkel.voicechat.net.*;
import de.maxhenkel.voicechat.voice.client.ClientVoiceEvents;
import de.maxhenkel.voicechat.voice.server.ServerVoiceEvents;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lwjgl.glfw.GLFW;

import java.util.regex.Pattern;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "voicechat";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static SimpleChannel SIMPLE_CHANNEL;

    public static ServerConfig SERVER_CONFIG;
    public static ClientConfig CLIENT_CONFIG;
    public static PlayerVolumeConfig VOLUME_CONFIG;

    public static ServerVoiceEvents SERVER_VOICE_EVENTS;
    @OnlyIn(Dist.CLIENT)
    public static ClientVoiceEvents CLIENT_VOICE_EVENTS;

    @OnlyIn(Dist.CLIENT)
    public static KeyBinding KEY_PTT;

    @OnlyIn(Dist.CLIENT)
    public static KeyBinding KEY_MUTE;

    @OnlyIn(Dist.CLIENT)
    public static KeyBinding KEY_DISABLE;

    @OnlyIn(Dist.CLIENT)
    public static KeyBinding KEY_VOICE_CHAT;

    @OnlyIn(Dist.CLIENT)
    public static KeyBinding KEY_HIDE_ICONS;

    @OnlyIn(Dist.CLIENT)
    public static KeyBinding KEY_VOICE_CHAT_SETTINGS;

    @OnlyIn(Dist.CLIENT)
    public static KeyBinding KEY_GROUP;

    public static final Pattern GROUP_REGEX = Pattern.compile("^[a-zA-Z0-9-_]{1,16}$");

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        SERVER_CONFIG = CommonRegistry.registerConfig(ModConfig.Type.SERVER, ServerConfig.class, true);
        CLIENT_CONFIG = CommonRegistry.registerConfig(ModConfig.Type.CLIENT, ClientConfig.class);
        VOLUME_CONFIG = new PlayerVolumeConfig();
    }


    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        SERVER_VOICE_EVENTS = new ServerVoiceEvents();
        MinecraftForge.EVENT_BUS.register(SERVER_VOICE_EVENTS);

        SIMPLE_CHANNEL = CommonRegistry.registerChannel(Main.MODID, "default");
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 0, AuthenticationMessage.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 1, PlayerStateMessage.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 2, PlayerStatesMessage.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 3, SetPlayerStateMessage.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 4, SetGroupMessage.class);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        CLIENT_VOICE_EVENTS = new ClientVoiceEvents();
        MinecraftForge.EVENT_BUS.register(CLIENT_VOICE_EVENTS);

        KEY_PTT = new KeyBinding("key.push_to_talk", GLFW.GLFW_KEY_CAPS_LOCK, "key.categories.voicechat");
        ClientRegistry.registerKeyBinding(KEY_PTT);

        KEY_MUTE = new KeyBinding("key.mute_microphone", GLFW.GLFW_KEY_M, "key.categories.voicechat");
        ClientRegistry.registerKeyBinding(KEY_MUTE);

        KEY_DISABLE = new KeyBinding("key.disable_voice_chat", GLFW.GLFW_KEY_N, "key.categories.voicechat");
        ClientRegistry.registerKeyBinding(KEY_DISABLE);

        KEY_HIDE_ICONS = new KeyBinding("key.hide_icons", GLFW.GLFW_KEY_H, "key.categories.voicechat");
        ClientRegistry.registerKeyBinding(KEY_HIDE_ICONS);

        KEY_VOICE_CHAT = new KeyBinding("key.voice_chat", GLFW.GLFW_KEY_V, "key.categories.voicechat");
        ClientRegistry.registerKeyBinding(KEY_VOICE_CHAT);

        KEY_VOICE_CHAT_SETTINGS = new KeyBinding("key.voice_chat_settings", InputMappings.UNKNOWN.getValue(), "key.categories.voicechat");
        ClientRegistry.registerKeyBinding(KEY_VOICE_CHAT_SETTINGS);

        KEY_GROUP = new KeyBinding("key.voice_chat_group", GLFW.GLFW_KEY_G, "key.categories.voicechat");
        ClientRegistry.registerKeyBinding(KEY_GROUP);
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartedEvent event) {
        SERVER_VOICE_EVENTS.serverStarting(event);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        VoicechatCommands.register(event.getDispatcher());
    }

}
