package cn.innc11.QuickShopX.listener;

import java.util.ArrayList;

import cn.innc11.QuickShopX.shop.Shop;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.block.BlockWallSign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBurnEvent;
import cn.nukkit.event.block.BlockPistonChangeEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;

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
				 Shop shop = Shop.findShop(block);
				 
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
			 Shop shop = Shop.findShop(block);
			 
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
			 Shop shop = Shop.findShop(block);
			 
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
	
}
