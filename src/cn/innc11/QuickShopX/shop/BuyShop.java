package cn.innc11.QuickShopX.shop;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.config.LangConfig.Lang;
import cn.innc11.QuickShopX.pluginEvent.PlayerBuyEvent;
import cn.innc11.QuickShopX.utils.InvItem;
import cn.innc11.QuickShopX.utils.L;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.ChestInventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import me.onebone.economyapi.EconomyAPI;

public class BuyShop extends Shop {

	public BuyShop(String shop) 
	{
		super(shop);
	}
	
	public void buyItem(Player player, int count)
	{
		if(count <=0 )
		{
			player.sendMessage(L.get(Lang.IM_TRADE_CANCELED));
			return;
		}

		EconomyAPI economyAPI = EconomyAPI.getInstance();
		double playerMoney = economyAPI.myMoney(player);
		double price = data.price * count;
		Player shopOwner = Server.getInstance().getPlayerExact(data.owner);
		Item item = Item.get(data.itemID, data.itemMetadata, count);
		PlayerInventory playerInv = player.getInventory();
		ChestInventory shopChestInventory = getShopChest().getRealInventory();
		int itemCountInChest = InvItem.getItemInInventoryCount(shopChestInventory, item);

		if(getShopChest()!=null)
		{
			if(itemCountInChest >= count || data.serverShop)
			{
				if(playerMoney >= price)
				{
					if(playerInv.canAddItem(item))
					{
						PlayerBuyEvent event = new PlayerBuyEvent(player, this, count);
						QuickShopXPlugin.instance.getServer().getPluginManager().callEvent(event);

						if(!event.isCancelled())
						{
							economyAPI.reduceMoney(player, price);

							playerInv.addItem(item);

							if(!data.serverShop)
							{
								economyAPI.addMoney(data.owner, price);

								getShopChest().getInventory().removeItem(item);

								if(shopOwner!=null) {
									shopOwner.sendMessage(L.get(Lang.IM_BUYSHOP_OWNER, "{ITEM_NAME}", QuickShopXPlugin.instance.itemNameConfig.getItemName(item), "{ITEM_COUNT}", String.valueOf(count), "{MONEY}", String.format("%.2f", price)));
								}
							}

							updateSignText();

							player.sendMessage(L.get(Lang.IM_BUYSHOP_CUSTOMER, "{ITEM_NAME}", QuickShopXPlugin.instance.itemNameConfig.getItemName(item), "{ITEM_COUNT}", String.valueOf(count), "{MONEY}", String.format("%.2f", price)));
						}


					} else {
						player.sendMessage(L.get(Lang.IM_BACKPACK_FULL, "{TARGET_COUNT}", String.valueOf(count), "{ITEM_NAME}", QuickShopXPlugin.instance.itemNameConfig.getItemName(item)));
					}
				}  else {
					player.sendMessage(L.get(Lang.IM_NOT_ENOUGH_MONEY_TO_BUYING, "{MONEY}", String.format("%.2f", playerMoney), "{ITEM_PRICE}", String.format("%.2f", data.price), "{ITEM_COUNT}", String.valueOf(count), "{SUBTOTAL}", String.format("%.2f", data.price*count), "{LACKING_MONEY}", String.format("%.2f", price-playerMoney)));
				}
			}else {
				if(itemCountInChest==0)
					player.sendMessage(L.get(Lang.IM_SHOP_SOLD_OUT));
				else
					player.sendMessage(L.get(Lang.IM_INSUFFICIENT_SHOP_STOCK, "{TARGET_COUNT}", String.valueOf(count), "{MAX_COUNT}", String.valueOf(itemCountInChest)));
			}
		}
	}

	@Override
	public int getMaxTranscationVolume(float playerMoney, int playerItemCount)
	{
		int itemCountInChest = InvItem.getItemInInventoryCount(getShopChest().getInventory(), getItem());
		int a = (int) (playerMoney / data.price);
		
		if(data.serverShop)
			itemCountInChest = a;
		
		return Math.min(itemCountInChest, a);
	}

}
