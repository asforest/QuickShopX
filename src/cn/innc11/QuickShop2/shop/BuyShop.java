package cn.innc11.QuickShop2.shop;

import cn.innc11.QuickShop2.Main;
import cn.innc11.QuickShop2.config.LangConfig.Lang;
import cn.innc11.QuickShop2.utils.InvItem;
import cn.innc11.QuickShop2.utils.L;
import cn.nukkit.Player;
import cn.nukkit.Server;
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
		
		double playerMoney = EconomyAPI.getInstance().myMoney(player);
		double price = data.price * count;
		
		if(playerMoney >= price)
		{
			if(getShopChest()!=null)
			{
				Item item = Item.get(data.itemID, data.itemMetadata, count);
				
				int itemCountInChest = InvItem.getItemInInventoryCount(getShopChest().getInventory(), item);
				
				if(data.unlimited || itemCountInChest >= count)
				{
					PlayerInventory playerInventory = player.getInventory();
					
					if(!playerInventory.isFull() && playerInventory.canAddItem(item))
					{
						EconomyAPI.getInstance().reduceMoney(player, price);
						
						if(!data.unlimited)
						{
							Player owner = Server.getInstance().getPlayerExact(data.owner);
							
							EconomyAPI.getInstance().addMoney(data.owner, price);
							
							if(owner!=null)
								owner.sendMessage(L.get(Lang.IM_BUYSHOP_OWNER, "{ITEM_NAME}", Main.instance.itemNameConfig.getItemName(item), "{ITEM_COUNT}", String.valueOf(count), "{MONEY}", String.format("%.2f", price)));
							
							getShopChest().getInventory().removeItem(item);
						}
						
						player.getInventory().addItem(item);
						
						updateSignText();
						
						player.sendMessage(L.get(Lang.IM_BUYSHOP_CUSTOMER, "{ITEM_NAME}", Main.instance.itemNameConfig.getItemName(item), "{ITEM_COUNT}", String.valueOf(count), "{MONEY}", String.format("%.2f", price)));
						
					} else {
						player.sendMessage(L.get(Lang.IM_BACKPACK_FULL, "{TARGET_COUNT}", String.valueOf(count), "{ITEM_NAME}", Main.instance.itemNameConfig.getItemName(item)));
					}
				} else {
					if(itemCountInChest==0)
						player.sendMessage(L.get(Lang.IM_SHOP_SOLD_OUT));
					else
						player.sendMessage(L.get(Lang.IM_INSUFFICIENT_SHOP_STOCK, "{TARGET_COUNT}", String.valueOf(count), "{MAX_COUNT}", String.valueOf(itemCountInChest)));
				}
			}
		} else {
			player.sendMessage(L.get(Lang.IM_NOT_ENOUGH_MONEY_TO_BUYING, "{MONEY}", String.format("%.2f", playerMoney), "{ITEM_PRICE}", String.format("%.2f", data.price), "{ITEM_COUNT}", String.valueOf(count), "{SUBTOTAL}", String.format("%.2f", data.price*count), "{LACK_MONEY}", String.format("%.2f", price-playerMoney)));
		}
	}

	@Override
	public int getMaxTranscationVolume(float price) 
	{
		int itemCountInChest = InvItem.getItemInInventoryCount(getShopChest().getInventory(), getItem());
		int a = (int) (price / data.price);
		
		if(data.unlimited)
			itemCountInChest = a;
		
		return Math.min(itemCountInChest, a);
	}

}
