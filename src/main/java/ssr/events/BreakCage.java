package ssr.events;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import ssr.gameObjs.CageTile;
import ssr.gameObjs.ObjHandler;
import ssr.utils.TierHandling;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BreakCage {
	@SubscribeEvent
	public void blockBreak(BlockEvent.BreakEvent event) {
		World world = event.world;
		if (!world.isRemote) {
			Block block = event.block;
			if (block == ObjHandler.sCage) {
				int meta = event.blockMetadata;
				int x = event.x;
				int y = event.y;
				int z = event.z;
				CageTile tile = (CageTile) world.getTileEntity(x, y, z);
				if (tile != null && tile.tier != 0) {
					int tier = tile.tier;
					ItemStack stack = new ItemStack(ObjHandler.sShard);
					stack.setTagCompound(new NBTTagCompound());
					NBTTagCompound nbt = stack.stackTagCompound;
					nbt.setInteger("Tier", tier);
					nbt.setInteger("KillCount", TierHandling.getMin(tier));
					nbt.setString("EntityType", tile.entName);
					nbt.setString("EntityId", tile.entId);
					ItemStack heldItem = tile.HeldItem;
					if (heldItem != null) {
						nbt.setBoolean("HasItem", true);
						NBTTagCompound nbt2 = new NBTTagCompound();
						heldItem.writeToNBT(nbt2);
						nbt.setTag("Item", nbt2);
					}

					stack.setItemDamage(tier + 1);
					world.spawnEntityInWorld(new EntityItem(world, x, y, z,
							stack));
				}
			}
		}
	}
}
