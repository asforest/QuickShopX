package cn.innc11.QuickShop2.config;

import java.util.HashMap;

import cn.innc11.QuickShop2.QuickShopXPlugin;
import cn.innc11.QuickShop2.pluginEvent.PlayerRemoveShopEvent;
import cn.innc11.QuickShop2.shop.Shop;
import cn.innc11.QuickShop2.shop.ShopData;
import cn.innc11.QuickShop2.shop.ShopType;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
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
        serverShop: false
    }"
     */
	
	private boolean modified = false;
	private boolean saving = false;
	
	private PluginTask<QuickShopXPlugin> saveTask;
	
	
	public Shop addShop(ShopData shop)
	{
		String key = String.format("%d:%d:%d:%s", shop.chestX, shop.chestY, shop.chestZ, shop.world);
		shopDataHashMap.put(key, shop);
		
		save();
		
		return Shop.getShopInstance(key);
	}
	
	public boolean destoryShop(ShopData shopData, Player player)
	{
		PlayerRemoveShopEvent event = new PlayerRemoveShopEvent(player, shopData.getShop());
		QuickShopXPlugin.instance.getServer().getPluginManager().callEvent(event);

		if(!event.isCancelled())
		{
			String key = String.format("%d:%d:%d:%s", shopData.chestX, shopData.chestY, shopData.chestZ, shopData.world);

//		    Shop.getShopInstance(key).destoryShop();

			shopDataHashMap.remove(key);

			save();

			return true;
		}

		return false;
	}

	public void removeShop(ShopData shopData)
	{
		String key = String.format("%d:%d:%d:%s", shopData.chestX, shopData.chestY, shopData.chestZ, shopData.world);

//		    Shop.getShopInstance(key).destoryShop();

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
			QuickShopXPlugin.instance.getServer().getScheduler().scheduleTask(QuickShopXPlugin.instance, saveTask, true);
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
			shopData.serverShop = config.getBoolean(key+".serverShop");
			
			shopDataHashMap.put(key, shopData);
		}
		
		QuickShopXPlugin.instance.getLogger().info("Loaded "+config.getKeys(false).size()+" Shops");
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
			config.set(key+".serverShop", shopData.serverShop);
			config.set(key+".common", QuickShopXPlugin.instance.itemNameConfig.getItemName(Item.get(shopData.itemID, shopData.itemMetadata)));
		}
		
		config.save();
	}
	
	public ShopConfig() 
	{
		super("shops.yml");
		
		reload();
		
		saveTask = new PluginTask<QuickShopXPlugin>(QuickShopXPlugin.instance)
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
