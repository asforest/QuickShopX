package cn.innc11.QuickShop2.shop;

import cn.innc11.QuickShop2.Main;
import cn.innc11.QuickShop2.config.LangConfig.Lang;
import cn.innc11.QuickShop2.utils.InvItem;
import cn.innc11.QuickShop2.utils.L;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.ChestInventory;
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
		
		double shopOwnerMoney = EconomyAPI.getInstance().myMoney(data.owner);
		double price = data.price * count;
		
		if(shopOwnerMoney >= price)
		{
			if(getShopChest()!=null)
			{
				Item item = Item.get(data.itemID, data.itemMetadata, count);
				
				if(InvItem.hasItem(player.getInventory(), item) && 
						(InvItem.getItemInInventoryCount(player.getInventory(), item) >= count)) 
				{
					ChestInventory chestInventory = (ChestInventory) getShopChest().getInventory();
					
					if(!chestInventory.isFull() && chestInventory.canAddItem(item))
					{
						EconomyAPI.getInstance().addMoney(player, price);
					
						if(!data.unlimited)
						{
							EconomyAPI.getInstance().reduceMoney(data.owner, price);
							
							Player owner = Server.getInstance().getPlayerExact(data.owner);
							
							if(owner!=null) 
								owner.sendMessage(L.get(Lang.IM_SELLSHOP_OWNER, "{ITEM_NAME}", Main.instance.itemNameConfig.getItemName(item), "{ITEM_COUNT}", String.valueOf(count), "{MONEY}", String.format("%.2f", price)));
							
							getShopChest().getInventory().addItem(item);
						}
						
						player.getInventory().removeItem(item);
						
						updateSignText();
						
						player.sendMessage(String.format(L.get(Lang.IM_SELLSHOP_CUSTOMER, "{ITEM_NAME}", Main.instance.itemNameConfig.getItemName(item), "{ITEM_COUNT}", String.valueOf(count), "{MONEY}", String.format("%.2f", price))));
					} else {
						player.sendMessage(L.get(Lang.IM_STOCK_FULL, "{TARGET_COUNT}", String.valueOf(count), "{MAX_COUNT}", String.valueOf(getShopChest().getInventory().getFreeSpace(item))));
					}
					
				} else {
					player.sendMessage(L.get(Lang.IM_ITEM_NOT_ENOUGH, "{ITEM_NAME}", Main.instance.itemNameConfig.getItemName(item), "{TARGET_COUNT}", String.valueOf(count)));
				}
				
			}
		} else {
			player.sendMessage(L.get(Lang.IM_SHOP_OWNER_NOT_ENOUGH_MONEY, "{OWNER}", data.owner));
		}
	}


	@Override
	public int getMaxTranscationVolume(float price) 
	{
		int itemFree = getShopChest().getInventory().getFreeSpace(getItem());
		int a = (int) (EconomyAPI.getInstance().myMoney(data.owner) / data.price);
		
		if(data.unlimited)
			itemFree = a;
		
		return Math.min(itemFree, a);
	}


}
