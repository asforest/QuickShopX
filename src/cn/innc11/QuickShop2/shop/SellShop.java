package cn.innc11.QuickShop2.shop;

import cn.innc11.QuickShop2.QuickShop2Plugin;
import cn.innc11.QuickShop2.config.LangConfig.Lang;
import cn.innc11.QuickShop2.utils.InvItem;
import cn.innc11.QuickShop2.utils.L;
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
			player.sendMessage(L.get(Lang.IM_TRADE_CANCELED));
			return;
		}

		EconomyAPI economyAPI = EconomyAPI.getInstance();
		double shopOwnerMoney =economyAPI.myMoney(data.owner);
		double price = data.price * count;
		Player shopOwner = Server.getInstance().getPlayerExact(data.owner);
		Item item = Item.get(data.itemID, data.itemMetadata, count);
		PlayerInventory playerInv = player.getInventory();
		ChestInventory shopChestInventory = (ChestInventory) getShopChest().getInventory();

		if(getShopChest()!=null)
		{
			if(shopOwnerMoney >= price || data.serverShop)
			{
				if(shopChestInventory.canAddItem(item) || data.serverShop)
				{
					if(InvItem.getItemInInventoryCount(playerInv, item) >= count)
					{
						economyAPI.addMoney(player, price);

						if(!data.serverShop)
						{
							economyAPI.reduceMoney(data.owner, price);

							shopChestInventory.addItem(item);

							if(shopOwner!=null) {
								shopOwner.sendMessage(L.get(Lang.IM_SELLSHOP_OWNER, "{ITEM_NAME}", QuickShop2Plugin.instance.itemNameConfig.getItemName(item), "{ITEM_COUNT}", String.valueOf(count), "{MONEY}", String.format("%.2f", price)));
							}
						}

						playerInv.removeItem(item);

						updateSignText();

						player.sendMessage(String.format(L.get(Lang.IM_SELLSHOP_CUSTOMER, "{ITEM_NAME}", QuickShop2Plugin.instance.itemNameConfig.getItemName(item), "{ITEM_COUNT}", String.valueOf(count), "{MONEY}", String.format("%.2f", price))));

					} else {
						player.sendMessage(L.get(Lang.IM_ITEM_NOT_ENOUGH, "{ITEM_NAME}", QuickShop2Plugin.instance.itemNameConfig.getItemName(item), "{TARGET_COUNT}", String.valueOf(count)));
					}

				} else {
					player.sendMessage(L.get(Lang.IM_STOCK_FULL, "{TARGET_COUNT}", String.valueOf(count), "{MAX_COUNT}", String.valueOf(getShopChest().getInventory().getFreeSpace(item))));
				}

			} else {
				player.sendMessage(L.get(Lang.IM_SHOP_OWNER_NOT_ENOUGH_MONEY, "{OWNER}", data.owner));
			}
		}


	}


	@Override
	public int getMaxTranscationVolume(float playerMoney, int playerItemCount)
	{
		int itemFree = getShopChest().getInventory().getFreeSpace(getItem());
		int a = (int) (EconomyAPI.getInstance().myMoney(data.owner) / data.price);
		
		if(data.serverShop)
			a = itemFree;
		
		return Math.min(Math.min(itemFree, a), playerItemCount);
	}


}
