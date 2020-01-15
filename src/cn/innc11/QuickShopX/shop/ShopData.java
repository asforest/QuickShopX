package cn.innc11.QuickShopX.shop;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.nukkit.item.Item;

public class ShopData
{
	public String owner;
	public ShopType type;
	public float price;
	public int chestX;
	public int chestY;
	public int chestZ;
	public int signX;
	public int signZ;
	public String world;
	public Item item;
	public boolean serverShop;
	
	public ShopData()
	{
	}

	public Shop getShop()
	{
		return Shop.getShopInstance(getShopKey());
	}
	
	public String getShopKey()
	{
		return String.format("%d:%d:%d:%s", chestX, chestY, chestZ, world);
	}

	public boolean equals(Object obj)
	{
		if(obj instanceof ShopData)
		{
			ShopData sd = ((ShopData) obj);
			boolean ret = true;
			ret &= serverShop == sd.serverShop;
			ret &= owner.equals(sd.owner);
			ret &= type == sd.type;
			ret &= price == sd.price;
			ret &= chestX == sd.chestX;
			ret &= chestY == sd.chestY;
			ret &= chestZ == sd.chestZ;
			ret &= signX == sd.signX;
			ret &= signZ == sd.signZ;
			ret &= world.equals(sd.world);
			ret &= serverShop == sd.serverShop;
			ret &= item.equalsExact(sd.item);

			return ret;
		}else{
			return false;
		}
	}
}
