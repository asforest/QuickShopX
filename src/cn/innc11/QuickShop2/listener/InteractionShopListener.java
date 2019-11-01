package cn.innc11.QuickShop2.listener;

import java.util.Arrays;
import java.util.HashMap;

import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

import cn.innc11.QuickShop2.QuickShopXPlugin;
import cn.innc11.QuickShop2.utils.Pair;
import cn.innc11.QuickShop2.config.LangConfig.Lang;
import cn.innc11.QuickShop2.form.ShopMasterPanel;
import cn.innc11.QuickShop2.form.TradingPanel;
import cn.innc11.QuickShop2.shop.BuyShop;
import cn.innc11.QuickShop2.shop.SellShop;
import cn.innc11.QuickShop2.shop.Shop;
import cn.innc11.QuickShop2.utils.L;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.block.BlockWallSign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.ChestInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;

public class InteractionShopListener implements Listener, ShopInteractionTimer
{
	public HashMap<String, Pair<Long, Shop>> interactingShopHashMap = new HashMap<>();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) 
	{
		if(e.getBlock() instanceof BlockWallSign)
		{
			Block block = e.getBlock();
			Player player = e.getPlayer();
			
			Shop shop = Shop.findShopBySignPos(block);
			
			if(shop!=null)
			{
				shop.updateSignText();
				
				QuickShopXPlugin.instance.hologramListener.addShopItemEntity(Arrays.asList(player), shop.data);
				
				switch (QuickShopXPlugin.instance.pluginConfig.formOperate)
				{
					case DOUBLE_CLICK:
					{
						Pair<Boolean, Shop> t = isVaildInteraction(player.getName());
						
						if(t!=null && t.value.equals(shop) && t.key)
						{
							if(player.getName().equals(shop.data.owner) || player.isOp())
								player.showFormWindow(new ShopMasterPanel(shop, player.getName()));
							else
								player.showFormWindow(new TradingPanel(shop, player.getName()));
							interactingShopHashMap.remove(e.getPlayer().getName());
							break;
						}
					}
					case NEVER:
					{
						Item shopItem = Item.get(shop.data.itemID, shop.data.itemMetadata);
						
						// show this shop detail information
						player.sendMessage(L.get(Lang.IM_SHOP_INFO_SHOW, "{OWNER}", shop.data.serverShop? L.get(Lang.SERVER_SHOP_NICKNAME):shop.data.owner, "{GOODS}", QuickShopXPlugin.instance.itemNameConfig.getItemName(shopItem), "{PRICE}", String.format("%.2f", shop.data.price), "{SHOP_TYPE}", shop.data.type.toString(), "{SIGN_STOCK_TEXT}", QuickShopXPlugin.instance.signTextConfig.getStockText(shop)));
						
						if(!player.getName().equals(shop.data.owner))
							player.sendMessage(L.get(Lang.IM_ENTER_TRANSACTIONS_COUNT));
						
						interactingShopHashMap.put(player.getName(), new Pair<Long, Shop>(System.currentTimeMillis()+ QuickShopXPlugin.instance.pluginConfig.interactionInterval, shop));
						break;
					}
					
						
					case ALWAYS:
						if(player.getName().equals(shop.data.owner))
							player.showFormWindow(new ShopMasterPanel(shop, player.getName()));
						else
							player.showFormWindow(new TradingPanel(shop, player.getName()));
						
						interactingShopHashMap.put(player.getName(), new Pair<Long, Shop>(System.currentTimeMillis()+ QuickShopXPlugin.instance.pluginConfig.interactionInterval, shop));
						
						break;
				}
				
				e.setCancelled();
			}
			
		}
	}
	
	@EventHandler
	public void onPlayerChatEvent(PlayerChatEvent e)
	{
		Player player = e.getPlayer();
		String playerName = player.getName();
		
		if(interactingShopHashMap.containsKey(playerName))
		{
			// not is owner
			if(!interactingShopHashMap.get(playerName).value.data.owner.equals(playerName))
			{
				if(!QuickShopXPlugin.isInteger(e.getMessage()))
				{
					// not a number
					e.getPlayer().sendMessage(L.get(Lang.IM_NO_ENTER_NUMBER));
					e.setCancelled();
				}else{
					if(isVaildInteraction(playerName).key)
					{
						Shop shop = interactingShopHashMap.get(playerName).value;
						
						if(shop instanceof BuyShop)
						{
							((BuyShop) shop).buyItem(player, Integer.parseInt(e.getMessage()));
						}else if(shop instanceof SellShop)
						{
							((SellShop) shop).sellItme(player, Integer.parseInt(e.getMessage()));
						}
						
						e.setCancelled();
					}
				}
				
				interactingShopHashMap.remove(playerName);
			}
			
			
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerBrokeBlockEvent(BlockBreakEvent e)
	{
		if(e.getBlock() instanceof BlockChest)
		{
			Block block = e.getBlock();
			
			Player player = e.getPlayer();
			
			Shop shop = Shop.findShop(block);
			
			if(shop!=null)
			{
				if(player.getName().equals(shop.data.owner) || player.isOp())
				{
					player.sendMessage(L.get(Lang.IM_NO_REMOVE_SHOP_EXISTS_SIGN));
				} else {
					player.sendMessage(L.get(Lang.IM_NO_REMOVE_SHOP_NO_ONWER));
				}
				
				e.setCancelled();
				
			}
			
		}
		
		if(e.getBlock() instanceof BlockWallSign)
		{
			Player player = e.getPlayer();
			
			Shop shop = Shop.findShopBySignPos(e.getBlock());
			
			if(shop!=null)
			{
				boolean allow = true;
				
				if(QuickShopXPlugin.instance.residencePluginLoaded && QuickShopXPlugin.instance.pluginConfig.interactionWithResidencePlugin)
				{
					ClaimedResidence res = Residence.getResidenceManager().getByLoc(e.getBlock());
					
					if(res!=null && !QuickShopXPlugin.instance.pluginConfig.opIgnoreResidenceBuildPermission)
					{
						if(!player.getName().equals(shop.data.owner)/* && !player.isOp()*/)
						{
							if(!res.getPermissions().playerHas(player.getName(), "build", false))
							{
								player.sendMessage(L.get(Lang.IM_NO_RESIDENCE_PERMISSION, "{PERMISSION}", "build"));
								allow = false;
							}
						}
					}
				} else {
					if(!player.getName().equals(shop.data.owner) && !player.isOp())
					{
						allow = false;
						player.sendMessage(L.get(Lang.IM_NO_REMOVE_SHOP_NO_ONWER));
					}
				}
				
				if(allow)
				{
					if(QuickShopXPlugin.instance.pluginConfig.snakeModeDestroyShop && !player.isSneaking())
					{
						player.sendMessage(L.get(Lang.IM_SNAKE_MODE_DESTROY_SHOP));

						e.setCancelled();

						return;
					}

					if(QuickShopXPlugin.instance.shopConfig.destoryShop(shop.data, player))
					{
						QuickShopXPlugin.instance.hologramListener.removeItemEntity(Server.getInstance().getOnlinePlayers().values(), shop.data);
						e.setDrops(new Item[0]);
						player.sendMessage(L.get(Lang.IM_SUCCEESSFULLY_REMOVED_SHOP));
					}
/*
					if(QuickShop2Plugin.instance.pluginConfig.snakeModeDestroyShop && player.isSneaking())
					{
						QuickShop2Plugin.instance.hologramListener.removeItemEntity(Server.getInstance().getOnlinePlayers().values(), shop.data);

						QuickShop2Plugin.instance.shopConfig.removeShop(shop.data);

						e.setDrops(new Item[0]);

						player.sendMessage(L.get(Lang.IM_SUCCEESSFULLY_REMOVED_SHOP));
					} else {
						player.sendMessage(L.get(Lang.IM_SNAKE_MODE_DESTROY_SHOP));

						e.setCancelled();
					}
*/
				} else {
					player.sendMessage(L.get(Lang.IM_NO_RESIDENCE_PERMISSION, "{PERMISSION}", "build"));
					e.setCancelled();
				}
			}
			
		}
		
	}
	
	
	@Override
	public Pair<Boolean, Shop> isVaildInteraction(String player) 
	{
		if(interactingShopHashMap.containsKey(player))
		{
			Pair<Long, Shop> is = interactingShopHashMap.get(player);
			boolean noTimeout = System.currentTimeMillis() < is.key.longValue();
			return new Pair<>(noTimeout, is.value);
		}
		
		return null;
	}
	
}
