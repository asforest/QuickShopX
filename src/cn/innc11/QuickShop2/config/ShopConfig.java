package cn.innc11.QuickShop2.config;

import java.util.HashMap;

import cn.innc11.QuickShop2.Main;
import cn.innc11.QuickShop2.shop.Shop;
import cn.innc11.QuickShop2.shop.ShopData;
import cn.innc11.QuickShop2.shop.ShopType;
import cn.nukkit.scheduler.PluginTask;

public class ShopConfig extends MyConfig
{
	public HashMap<String, ShopData> shopDataHashMap = new HashMap<String, ShopData>();
	/*
	 * chest x, y, z, world
    "233:666:233:world" =>
    "{
        owner: "WetABQ",
        chestX: 233,
        chestY: 666,
        chestZ: 233,
        signX : 233,
        signZ : 234,
        level: "world",
        itemId: 123,
        itemMeta: 1,
        unlimited: false
    }"
     */
	
	private boolean modified = false;
	private boolean saving = false;
	
	private PluginTask<Main> saveTask;
	
	
	public Shop addShop(ShopData shop)
	{
		String key = String.format("%d:%d:%d:%s", shop.chestX, shop.chestY, shop.chestZ, shop.world);
		shopDataHashMap.put(key, shop);
		
		save();
		
		return Shop.getShopInstance(key);
	}
	
	public void removeShop(ShopData shop)
	{
		String key = String.format("%d:%d:%d:%s", shop.chestX, shop.chestY, shop.chestZ, shop.world);
		
//		Shop.getShopInstance(key).destoryShop();
		
		shopDataHashMap.remove(key);
		
		save();
	}
	
	@Override
	public void save()
	{
		if(!saving)
		{
			modified = true;
			saving = true;
			Main.instance.getServer().getScheduler().scheduleTask(Main.instance, saveTask, true);
		} else {
			modified = true;
		}
		
		
	}
	
	@Override
	public void reload()
	{
		config.reload();
		
		shopDataHashMap.clear();
		for(String key : config.getKeys(false))
		{
			ShopData shopData = new ShopData();
			shopData.owner = config.getString(key+".owner");
			shopData.type = ShopType.valueOf(config.getString(key+".shopType"));
			shopData.price = (float) config.getDouble(key+".price");
			shopData.chestX = config.getInt(key+".chestX");
			shopData.chestY = config.getInt(key+".chestY");
			shopData.chestZ = config.getInt(key+".chestZ");
			shopData.signX = config.getInt(key+".signX");
			shopData.signZ = config.getInt(key+".signZ");
			shopData.world = config.getString(key+".world");
			shopData.itemID = config.getInt(key+".itemID");
			shopData.itemMetadata = config.getInt(key+".itemMeta");
			shopData.unlimited = config.getBoolean(key+".unlimited");
			
			shopDataHashMap.put(key, shopData);
		}
		
		Main.instance.getLogger().info("Loaded "+config.getKeys(false).size()+" Shops");
	}
	
	private void SAVE()
	{
		config.getRootSection().clear();
		
		for(String key : shopDataHashMap.keySet())
		{
			ShopData shopData = shopDataHashMap.get(key);
			
			config.set(key+".owner", shopData.owner);
			config.set(key+".shopType", shopData.type.name());
			config.set(key+".price", shopData.price);
			config.set(key+".chestX", shopData.chestX);
			config.set(key+".chestY", shopData.chestY);
			config.set(key+".chestZ", shopData.chestZ);
			config.set(key+".signX", shopData.signX);
			config.set(key+".signZ", shopData.signZ);
			config.set(key+".world", shopData.world);
			config.set(key+".itemID", shopData.itemID);
			config.set(key+".itemMeta", shopData.itemMetadata);
			config.set(key+".unlimited", shopData.unlimited);
		}
		
		config.save();
	}
	
	public ShopConfig() 
	{
		super("shops.yml");
		
		reload();
		
		saveTask = new PluginTask<Main>(Main.instance) 
		{
			@Override
			public void onRun(int currentTicks) 
			{
				while(modified)
				{
					modified = false;
					SAVE();
				}
				
				saving = false;
			}
		};
	}
	
	
}
