package de.maxhenkel.voicechat;

import de.maxhenkel.voicechat.item.ModItems;
import de.maxhenkel.voicechat.net.*;
import de.maxhenkel.voicechat.voice.client.AudioChannelConfig;
import de.maxhenkel.voicechat.voice.client.ClientVoiceEvents;
import de.maxhenkel.voicechat.voice.server.ServerVoiceEvents;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "voicechat";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static SimpleChannel SIMPLE_CHANNEL;

    public static ServerVoiceEvents SERVER_VOICE_EVENTS;
    @OnlyIn(Dist.CLIENT)
    public static ClientVoiceEvents CLIENT_VOICE_EVENTS;

    @OnlyIn(Dist.CLIENT)
    public static KeyBinding KEY_PTT;

    @OnlyIn(Dist.CLIENT)
    public static KeyBinding KEY_VOICE_CHAT_SETTINGS;

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(SoundEvent.class, this::registerSounds);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStarting);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);

    }

    @SubscribeEvent
    public void configEvent(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            AudioChannelConfig.onServerConfigUpdate();
        } else if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            AudioChannelConfig.onClientConfigUpdate();
        }
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        SERVER_VOICE_EVENTS = new ServerVoiceEvents();
        MinecraftForge.EVENT_BUS.register(SERVER_VOICE_EVENTS);

        SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Main.MODID, "default"), () -> "1.0.0", s -> true, s -> true);
        SIMPLE_CHANNEL.registerMessage(0, AuthenticationMessage.class, (msg, buf) -> msg.toBytes(buf), (buf) -> new AuthenticationMessage().fromBytes(buf), (msg, fun) -> msg.executeClientSide(fun.get()));
        SIMPLE_CHANNEL.registerMessage(1, CallMessage.class, (msg, buf) -> msg.toBytes(buf), (buf) -> new CallMessage().fromBytes(buf), (msg, fun) -> msg.executeServerSide(fun.get()));
        SIMPLE_CHANNEL.registerMessage(2, IncomingCallMessage.class, (msg, buf) -> msg.toBytes(buf), (buf) -> new IncomingCallMessage().fromBytes(buf), (msg, fun) -> msg.executeClientSide(fun.get()));
        SIMPLE_CHANNEL.registerMessage(3, AnswerCallMessage.class, (msg, buf) -> msg.toBytes(buf), (buf) -> new AnswerCallMessage().fromBytes(buf), (msg, fun) -> msg.executeServerSide(fun.get()));
        SIMPLE_CHANNEL.registerMessage(4, CallInfoMessage.class, (msg, buf) -> msg.toBytes(buf), (buf) -> new CallInfoMessage().fromBytes(buf), (msg, fun) -> msg.executeClientSide(fun.get()));
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::configEvent);

        CLIENT_VOICE_EVENTS = new ClientVoiceEvents();
        MinecraftForge.EVENT_BUS.register(CLIENT_VOICE_EVENTS);

        KEY_PTT = new KeyBinding("key.push_to_talk", GLFW.GLFW_KEY_CAPS_LOCK, "key.categories.misc");
        ClientRegistry.registerKeyBinding(KEY_PTT);

        KEY_VOICE_CHAT_SETTINGS = new KeyBinding("key.voice_chat_settings", GLFW.GLFW_KEY_V, "key.categories.misc");
        ClientRegistry.registerKeyBinding(KEY_VOICE_CHAT_SETTINGS);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                ModItems.PHONE
        );
    }

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                ModSounds.PHONE
        );
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartedEvent event) {
        SERVER_VOICE_EVENTS.serverStarting(event);
    }

}
