package ssr.events;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import ssr.config.SoulConfig;
import ssr.gameObjs.ObjHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CreateShard 
{
    public static Boolean isNorthSouthOrientation;

    @SubscribeEvent
    public void createShard(PlayerInteractEvent event)
    {
        World world = event.entityPlayer.worldObj;
        EntityPlayer player = event.entityPlayer;

        if (world.isRemote || player.getHeldItem() == null) return;

        Block block;
        if (player.getHeldItem().getItem() == Items.diamond)
        {
            block = world.getBlock(event.x, event.y, event.z);

            if (block == Blocks.glowstone && (isFormed(world, event.x, event.y, event.z) ||
            		isFormedVertically(world, event.x, event.y, event.z)))
            {
                if (!player.capabilities.isCreativeMode)
                	player.getHeldItem().stackSize -= 1;
                world.spawnEntityInWorld(new EntityItem(world, event.x, event.y, event.z, new ItemStack(ObjHandler.sShard)));

                if(SoulConfig.enableEndStoneRecipe)
                {
                	if(isFormedVertically(world, event.x, event.y, event.z))
                	{
                		world.setBlockToAir(event.x, event.y + 2, event.z);
                		world.setBlockToAir(event.x, event.y - 2, event.z);
                		if(isNorthSouthOrientation)
                		{
                			for(int i = -1; i < 2; i++)
                			{
                				for(int ii = -1; ii < 2; ii++)
                				{
                					world.setBlockToAir(event.x, event.y + ii, event.z + i);
                					if(ii == 0 && i == -1) world.setBlockToAir(event.x, event.y, event.z - 2);
                					if(ii == 0 && i == 1) world.setBlockToAir(event.x, event.y, event.z + 2);
                				}
                			}
                		}
                		else
                		{
                			for(int i = -1; i < 2; i++)
                			{
                				for(int ii = -1; ii < 2; ii++)
                				{
                					world.setBlockToAir(event.x + i, event.y + ii, event.z);
                					if(ii == 0 && i == -1) world.setBlockToAir(event.x - 2, event.y, event.z);
                					if(ii == 0 && i == 1) world.setBlockToAir(event.x + 2, event.y, event.z);
                				}
                			}
                		}
                	}
                	else
                	{
            			for(int i = -1; i < 2; i++)
            			{
            				for(int ii = -1; ii < 2; ii++)
            				{
            					world.setBlockToAir(event.x + i, event.y, event.z + ii);
            					if(ii == 0 && i == -1)
            					{
            						world.setBlockToAir(event.x - 2, event.y, event.z);
            						world.setBlockToAir(event.x, event.y, event.z - 2);
            					}
            					if(ii == 0 && i == 1)
            					{
            						world.setBlockToAir(event.x + 2, event.y, event.z);
            						world.setBlockToAir(event.x, event.y, event.z + 2);
            					}
            				}
            			}
                	}
                }
                else world.setBlockToAir(event.x, event.y, event.z);
            }
        }
    }

    private boolean isFormed(World world, int x, int y, int z)
    {
        Block blocks1[] = {world.getBlock(x + 1, y, z), world.getBlock(x - 1, y, z), world.getBlock(x, y, z + 1),
        		world.getBlock(x, y, z - 1)};
        Block blocks2[] = {world.getBlock(x + 2, y, z), world.getBlock(x - 2 , y, z), world.getBlock(x, y, z + 2),
        		world.getBlock(x, y, z - 2),
                         world.getBlock(x + 1, y, z + 1), world.getBlock(x + 1, y, z - 1), world.getBlock(x - 1, y, z + 1),
                         world.getBlock(x - 1, y, z - 1)};
        return Check(blocks1, blocks2);
    }

    private boolean isFormedVertically(World world, int x, int y, int z)
    {
        boolean result;
        Block blocks1[] = {world.getBlock(x, y + 1, z), world.getBlock(x, y - 1, z), world.getBlock(x, y, z + 1),
        		world.getBlock(x, y, z - 1)};
        Block blocks2[] = {world.getBlock(x, y + 1, z - 1), world.getBlock(x, y - 1, z - 1), world.getBlock(x, y + 1, z + 1),
        		world.getBlock(x, y - 1, z + 1), world.getBlock(x, y + 2, z), world.getBlock(x, y - 2, z),
        		world.getBlock(x, y, z + 2), world.getBlock(x, y, z - 2)};
        result = Check(blocks1, blocks2);
        isNorthSouthOrientation = true;

        if (result) return result;

        Block blocks3[] = {world.getBlock(x, y + 1, z), world.getBlock(x, y - 1, z), world.getBlock(x + 1, y, z),
        		world.getBlock(x + 1, y, z)};
        Block blocks4[] = {world.getBlock(x - 1, y + 1, z), world.getBlock(x - 1, y - 1, z),
        		world.getBlock(x + 1, y + 1, z), world.getBlock(x + 1, y - 1, z), world.getBlock(x, y + 2, z),
        		world.getBlock(x, y - 2, z), world.getBlock(x + 2, y, z), world.getBlock(x - 2, y, z)};
        result = Check(blocks3, blocks4);
        isNorthSouthOrientation = false;
        return result;
    }

    private boolean Check(Block blocks[], Block blocks2[])
    {
        for (int i = 0; i < blocks.length - 1; i++)
        {
            int j = i + 1;

            if (blocks[i] != blocks[j] || blocks[i] != Blocks.netherrack)
                return false;
        }

        for (int i = 0; i < blocks2.length - 1; i++)
        {
            int j = i + 1;

            if (blocks2[i] != blocks2[j] || blocks2[i] != Blocks.end_stone)
                return false;
        }

        return true;
    }
}
