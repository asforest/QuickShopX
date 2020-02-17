package cn.innc11.quickshopx.listener;

import java.util.HashMap;

import cn.innc11.quickshopx.utils.Lang;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.utils.Pair;
import cn.innc11.quickshopx.form.ShopOwnerPanel;
import cn.innc11.quickshopx.form.TradingPanel;
import cn.innc11.quickshopx.shop.BuyShop;
import cn.innc11.quickshopx.shop.SellShop;
import cn.innc11.quickshopx.shop.Shop;
import cn.innc11.quickshopx.utils.L;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.block.BlockWallSign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;

public class InteractionShopListener implements Listener, ShopInteractionTimer
{
	public HashMap<String, Pair<Long, Shop>> interactingShopHashMap = new HashMap<>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) 
	{
		if(e.getBlock() instanceof BlockWallSign && e.getAction()== PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			Block block = e.getBlock();
			Player player = e.getPlayer();
			String playerName = player.getName();
			
			Shop shop = Shop.findShopBySignPos(block);
			
			if(shop!=null)
			{
				shop.updateSignText();
				
				Quickshopx.ins.hologramListener.addShopItemEntity(Server.getInstance().getOnlinePlayers().values(), shop.shopData);

				switch (Quickshopx.ins.pluginConfig.interactionWay)
				{
					case DOUBLE_CLICK:
					{
						Pair<Boolean, Shop> interactionInfo = isVaildInteraction(playerName);
						Shop interactiveShop = interactionInfo!=null? interactionInfo.value:null;
						boolean noTimeout = interactionInfo!=null? interactionInfo.key:true;

						if(interactionInfo!=null && interactiveShop.equals(shop) && noTimeout)
						{
							if(player.getName().equals(shop.shopData.owner) || player.isOp())
								player.showFormWindow(new ShopOwnerPanel(shop, player.getName()));
							else
								player.showFormWindow(new TradingPanel(shop, player.getName()));
							interactingShopHashMap.remove(e.getPlayer().getName());
							break;
						}
						// no 'break;'
					}

					case NEVER:
					{
						Item shopItem = shop.getItem();
						
						// show the shop detail information
						player.sendMessage(L.get(Lang.im_shop_info,
								"{GOODS}", Quickshopx.ins.itemNamesConfig.getItemName(shopItem),
								"{PRICE}", String.format("%.1f", shop.shopData.price),
								"{OWNER}", shop.shopData.serverShop? L.get(Lang.server_shop_nickname):shop.shopData.owner,
								"{TYPE}", shop.shopData.type.toString(),
								"{STOCK}", Quickshopx.ins.signTextConfig.getStockText(shop),
                                "{ENCHANTMENTS}", L.getEnchantments(shopItem)
                        ));

						//if(!player.getName().equals(shop.shopData.owner))
						//{
						player.sendMessage(L.get(Lang.im_enter_transactions_volume));
						//}
						
						interactingShopHashMap.put(player.getName(), new Pair(System.currentTimeMillis()+ Quickshopx.ins.pluginConfig.interactionTime, shop));
						break;
					}
					
						
					case ALWAYS:
						if(player.getName().equals(shop.shopData.owner))
							player.showFormWindow(new ShopOwnerPanel(shop, player.getName()));
						else
							player.showFormWindow(new TradingPanel(shop, player.getName()));

						interactingShopHashMap.put(player.getName(), new Pair(System.currentTimeMillis()+ Quickshopx.ins.pluginConfig.interactionTime, shop));
						
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
		Pair<Boolean, Shop> interactionInfo = isVaildInteraction(playerName);
		Shop interactiveShop = interactionInfo!=null? interactionInfo.value:null;
		boolean noTimeout = interactionInfo!=null? interactionInfo.key:true;

		if(interactionInfo!=null)
		{
			if(noTimeout)
			{
				if (Quickshopx.isInteger(e.getMessage()))
				{
					if (interactiveShop instanceof BuyShop) {
						((BuyShop) interactiveShop).buyItem(player, Integer.parseInt(e.getMessage()));
					} else if (interactiveShop instanceof SellShop) {
						((SellShop) interactiveShop).sellItme(player, Integer.parseInt(e.getMessage()));
					}

				} else {
					player.sendMessage(L.get(Lang.im_not_a_number));
				}

				e.setCancelled();
			}

			interactingShopHashMap.remove(playerName);
		}


	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerBrokeBlockEvent(BlockBreakEvent e)
	{
		if(e.getBlock() instanceof BlockChest)
		{
			Block block = e.getBlock();
			Player player = e.getPlayer();
			Shop shop = Shop.getShopInstance(block);
			
			if(shop!=null)
			{
				if(player.getName().equals(shop.shopData.owner) || player.isOp())
				{
					player.sendMessage(L.get(Lang.im_not_allow_remove_shop_exists_sign));
				} else {
					player.sendMessage(L.get(Lang.im_not_allow_remove_shop_not_owner));
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
				
				if(Quickshopx.ins.residencePluginLoaded && Quickshopx.ins.pluginConfig.linkWithResidencePlugin)
				{
					ClaimedResidence res = Residence.getResidenceManager().getByLoc(e.getBlock());
					
					if(res!=null && !Quickshopx.ins.pluginConfig.ignoreOpBuildPermission)
					{
						if(!player.getName().equals(shop.shopData.owner))
						{
							if(!res.getPermissions().playerHas(player.getName(), "build", false))
							{
								player.sendMessage(L.get(Lang.im_no_residence_permission, "{PERMISSION}", "build"));
								allow = false;
							}
						}
					}
				} else {
					if(!player.getName().equals(shop.shopData.owner) && !player.isOp())
					{
						allow = false;
						player.sendMessage(L.get(Lang.im_not_allow_remove_shop_not_owner));
					}
				}
				
				if(allow)
				{
					if(Quickshopx.ins.pluginConfig.snakeModeDestroyShopOnly && !player.isSneaking())
					{
						player.sendMessage(L.get(Lang.im_snake_mode_destroy_shop));
						shop.updateSignText(5);
						e.setCancelled();

						return;
					}

					if(Quickshopx.ins.multiShopsConfig.getShopsConfig(shop, false).destroyShop(shop.shopData, player))
					{
						Quickshopx.ins.hologramListener.removeItemEntity(Server.getInstance().getOnlinePlayers().values(), shop.shopData);
						e.setDrops(new Item[0]);
						player.sendMessage(L.get(Lang.im_successfully_removed_shop));
					}
				} else {
					player.sendMessage(L.get(Lang.im_no_residence_permission, "{PERMISSION}", "build"));
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
