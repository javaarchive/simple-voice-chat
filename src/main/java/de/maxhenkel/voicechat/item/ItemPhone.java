package de.maxhenkel.voicechat.item;

import de.maxhenkel.voicechat.Main;
import de.maxhenkel.voicechat.net.CallInfoMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class ItemPhone extends Item {

    public ItemPhone() {
        super(new Item.Properties().maxStackSize(1).group(ItemGroup.MISC));
        setRegistryName(new ResourceLocation(Main.MODID, "phone"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (playerIn instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
            Main.SIMPLE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new CallInfoMessage(Main.SERVER_VOICE_EVENTS.getServer().getCalls().getCalls(player.getUniqueID())));
        }

        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }
}
