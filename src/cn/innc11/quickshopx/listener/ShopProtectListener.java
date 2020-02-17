package cn.innc11.quickshopx.listener;

import java.util.ArrayList;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.shop.Shop;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.block.BlockWallSign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBurnEvent;
import cn.nukkit.event.block.BlockPistonChangeEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.inventory.ChestInventory;
import cn.nukkit.inventory.Inventory;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

public class ShopProtectListener implements Listener 
{
	 @EventHandler
	public void onEntityExplode(EntityExplodeEvent event) 
	{
		 ArrayList<Block> preserve = new ArrayList<Block>();
		 
		 for(Block block : event.getBlockList())
		 {
			 if(block instanceof BlockChest)
			 {
				 Shop shop = Shop.getShopInstance(block);
				 
				 if(shop!=null)
					 preserve.add(block);
			 }
			 
			 if(block instanceof BlockWallSign)
			 {
				 Shop shop = Shop.findShopBySignPos(block);
				 
				 if(shop!=null)
					 preserve.add(block);
			 }
		 }
		 
		 for(Block block : preserve)
		 {
			 event.getBlockList().remove(block);
		 }
		 
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) 
	{
		Block block = event.getBlock();
		
		if(block instanceof BlockChest)
		 {
			 Shop shop = Shop.getShopInstance(block);
			 
			 if(shop!=null)
				 event.setCancelled();
		 }
		 
		 if(block instanceof BlockWallSign)
		 {
			 Shop shop = Shop.findShopBySignPos(block);
			 
			 if(shop!=null)
				 event.setCancelled();
		 }
	}
	
	@EventHandler
	public void onBlockPistonChange(BlockPistonChangeEvent e)
	{
		Block block = e.getBlock();
		
		if(block instanceof BlockChest)
		 {
			 Shop shop = Shop.getShopInstance(block);
			 
			 if(shop!=null)
				 e.setCancelled();
		 }
		 
		 if(block instanceof BlockWallSign)
		 {
			 Shop shop = Shop.findShopBySignPos(block);
			 
			 if(shop!=null)
				 e.setCancelled();
		 }
	}

	@EventHandler
	public void onInventoryMoveItem2(InventoryMoveItemEvent e)
	{
		boolean hopperCanOnlyActiveInResidence = Quickshopx.ins.pluginConfig.hopperLimit;
		boolean residenceEnable = Quickshopx.ins.residencePluginLoaded && Quickshopx.ins.pluginConfig.linkWithResidencePlugin;

		if(!(residenceEnable && hopperCanOnlyActiveInResidence))
		{
			return;
		}

		Inventory sourceInventory = e.getInventory();
		Inventory targetInventory = e.getTargetInventory();

		if(sourceInventory instanceof ChestInventory
				&& e.getAction() == InventoryMoveItemEvent.Action.SLOT_CHANGE)
		{
			ChestInventory chestInventory = (ChestInventory) sourceInventory;
			Shop shop = Shop.getShopInstance(chestInventory.getHolder());
			ClaimedResidence claimedResidence = Residence.getResidenceManager().getByLoc(chestInventory.getHolder());

			if(shop!=null && claimedResidence==null)
			{
				e.setCancelled();
			}
		}


		if(targetInventory instanceof ChestInventory
				&& e.getAction() == InventoryMoveItemEvent.Action.SLOT_CHANGE)
		{
			ChestInventory chestInventory = (ChestInventory) targetInventory;
			Shop shop = Shop.getShopInstance(chestInventory.getHolder());
			ClaimedResidence claimedResidence = Residence.getResidenceManager().getByLoc(chestInventory.getHolder());

			if(shop!=null && claimedResidence==null)
			{
				e.setCancelled();
			}
		}
	}
	
}
