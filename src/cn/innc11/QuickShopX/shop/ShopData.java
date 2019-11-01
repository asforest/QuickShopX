package cn.innc11.QuickShopX.shop;

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
	public int itemID;
	public int itemMetadata;
	public boolean serverShop;
	
//	public 
	
	public ShopData() 
	{
	}
	/*
	public ShopData(String owner, ShopType type, float price, int chestX,
			int chestY, int chestZ, int signX, int signZ, String world,
			int itemID, int itemMetadata, boolean serverShop)
	{
		this.owner = owner;
		this.type = type;
		this.price = price;
		this.chestX = chestX;
		this.chestY = chestY;
		this.chestZ = chestZ;
		this.signX = signX;
		this.signZ = signZ;
		this.world = world;
		this.itemID = itemID;
		this.itemMetadata = itemMetadata;
		this.serverShop = serverShop;
	}
	*/
	public Shop getShop()
	{
		return Shop.getShopInstance(getShopKey());
	}
	
	public String getShopKey()
	{
		return String.format("%d:%d:%d:%s", chestX, chestY, chestZ, world);
	}

	/*
	public ShopData clonE()
	{
		ShopData sd = null;
		try {
			sd = (ShopData) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return sd;
	}*/
}
