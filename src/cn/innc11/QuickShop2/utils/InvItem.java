package cn.innc11.QuickShop2.utils;

import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.item.Item;

public class InvItem 
{
	public static boolean hasItem(BaseInventory baseInventory, Item item) 
	{
		for(Item i : baseInventory.slots.values())
		{
			if(i.getId()==item.getId() && i.getDamage()==item.getDamage())
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
	        if (i.getId() == item.getId() && i.getDamage() == item.getDamage()) {
	            return i;
	        }
		}
		
		return null;
	}
	
	public static int getItemInInventoryCount(BaseInventory baseInventory,Item item)
	{
		int count = 0;
		for(Item i : baseInventory.slots.values()) 
		{
            if (i.getId() == item.getId() && i.getDamage() == item.getDamage()) {
                count += i.count;
            }
        }
        return count;
	}
}
