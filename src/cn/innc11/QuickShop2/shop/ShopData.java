package cn.innc11.QuickShop2.shop;

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
	public boolean unlimited;
	
//	public 
	
	public ShopData() 
	{
	}
	/*
	public ShopData(String owner, ShopType type, float price, int chestX,
			int chestY, int chestZ, int signX, int signZ, String world,
			int itemID, int itemMetadata, boolean unlimited) 
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
		this.unlimited = unlimited;
	}
	*/
	public Shop getShop()
	{
		return Shop.getShopInstance(toShopKey());
	}
	
	public String toShopKey()
	{
		return String.format("%d:%d:%d:%s", chestX, chestY, chestZ, world);
	}
	
	/*
	public Map<String, Object> toMap()
	{
		HashMap<String, Object> map = new HashMap<>();
		map.put("owner", owner);
		map.put("type", type.toString());
		map.put("price", price);
		map.put("chestX", chestX);
		map.put("chestY", chestY);
		map.put("chestZ", chestZ);
		map.put("signX", signX);
		map.put("signZ", signZ);
		map.put("world", world);
		map.put("itemID", itemID);
		map.put("itemMeta", itemMeta);
		map.put("unlimited", unlimited);
		return map;
	}
	*/
	
}
