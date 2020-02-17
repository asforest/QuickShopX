package cn.innc11.quickshopx.utils;

import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.item.Item;

public class InvItem 
{
	public static boolean hasItem(BaseInventory baseInventory, Item item) 
	{
		for(Item i : baseInventory.slots.values())
		{
			if (item.equals(i, true, true))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static Item getItemInInventory(BaseInventory baseInventory, Item item)
	{
		for(Item i : baseInventory.slots.values())
		{
			if (item.equals(i, true, true))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public static int getItemInInventoryCount(BaseInventory baseInventory, Item item)
	{
		int count = 0;
		for(Item i : baseInventory.slots.values()) 
		{
			//QuickShopXPlugin.instance.getLogger().info("["+item.toString()+"] <-> ["+i.toString()+"]  "+item.equals(i, true, true));
			if (item.equals(i, true, true))
			{
				count += i.count;
			}
        }
        return count;
	}
}
