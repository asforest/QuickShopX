package cn.innc11.quickshopx.shop;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.pluginEvent.PlayerSellEvent;
import cn.innc11.quickshopx.utils.InvItem;
import cn.innc11.quickshopx.utils.L;
import cn.innc11.quickshopx.utils.Lang;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.ChestInventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import me.onebone.economyapi.EconomyAPI;

public class SellShop extends Shop 
{

	public SellShop(String shop) 
	{
		super(shop);
	}
	
	public void sellItme(Player player, int count)
	{
		if(count <=0)
		{
			player.sendMessage(L.get(Lang.im_trade_canceled));
			return;
		}

		EconomyAPI economyAPI = EconomyAPI.getInstance();
		double shopOwnerMoney =economyAPI.myMoney(shopData.owner);
		double price = shopData.price * count;
		Player shopOwner = Server.getInstance().getPlayerExact(shopData.owner);
		Item item = getItem();
		item.setCount(count);
		PlayerInventory playerInv = player.getInventory();
		ChestInventory shopChestInventory = getShopEntityChest().getRealInventory();

		if(getShopEntityChest()!=null)
		{
			if(shopOwnerMoney >= price || shopData.serverShop)
			{
				if(shopChestInventory.canAddItem(item) || shopData.serverShop)
				{
					if(InvItem.getItemInInventoryCount(playerInv, item) >= count)
					{
						PlayerSellEvent event = new PlayerSellEvent(player, this, count);
						Quickshopx.ins.getServer().getPluginManager().callEvent(event);

						if(!event.isCancelled())
						{
							economyAPI.addMoney(player, price);

							if(!shopData.serverShop)
							{
								economyAPI.reduceMoney(shopData.owner, price);

								shopChestInventory.addItem(item);

								if(shopOwner!=null) {
									shopOwner.sendMessage(L.get(Lang.im_sellshop_owner,
											"{ITEM}", Quickshopx.ins.itemNamesConfig.getItemName(item),
											"{COUNT}", String.valueOf(count),
											"{MONEY}", String.format("%.2f", price)));
								}
							}

							playerInv.removeItem(item);

							updateSignText();

							player.sendMessage(String.format(L.get(Lang.im_sellshop_customer,
									"{ITEM}", Quickshopx.ins.itemNamesConfig.getItemName(item),
									"{COUNT}", String.valueOf(count),
									"{MONEY}", String.format("%.2f", price))));
						}


					} else {
						player.sendMessage(L.get(Lang.im_sellshop_not_enough_item,
								"{ITEM}", Quickshopx.ins.itemNamesConfig.getItemName(item),
								"{TARGET_COUNT}", String.valueOf(count)));
					}

				} else {
					player.sendMessage(L.get(Lang.im_sellshop_stock_full, "{TARGET_COUNT}", String.valueOf(count), "{MAX_COUNT}", String.valueOf(getShopEntityChest().getInventory().getFreeSpace(item))));
				}

			} else {
				player.sendMessage(L.get(Lang.im_sellshop_not_enough_money, "{OWNER}", shopData.owner));
			}
		}


	}


	@Override
	public int getMaxTransactionVolume(float playerMoney, int playerItemCount)
	{
		int itemFree = getShopEntityChest().getInventory().getFreeSpace(getItem());
		int a = (int) (EconomyAPI.getInstance().myMoney(shopData.owner) / shopData.price);
		
		if(shopData.serverShop)
			a = itemFree;
		
		return Math.min(Math.min(itemFree, a), playerItemCount);
	}


}
