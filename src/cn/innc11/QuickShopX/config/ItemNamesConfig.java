package cn.innc11.QuickShopX.config;

import java.util.HashMap;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.nukkit.item.Item;

public class ItemNamesConfig extends MyConfig
{
	HashMap<String, String> itemNameMap = new HashMap<>();
	
	public ItemNamesConfig()
	{
		super("itemName.yml");
		
		reload();
	}

	@Override
	public void save() 
	{

	}

	@SuppressWarnings("unchecked")
	@Override
	public void reload() 
	{
		config.reload();
		
		itemNameMap.clear();
		
		Object configMap = config.get("itemNameMap");
		
		if(configMap!=null)
		{
			HashMap<String, String> cmap = (HashMap<String, String>) configMap;
			for(String key : cmap.keySet())
			{
				Item item = Item.fromString(key);
				
				if(item.getId()!=0)
				{
					itemNameMap.put(item.getId()+":"+item.getDamage(), cmap.get(key));
				}
			}
			
//			for(String key : itemNameMap.keySet())
//				Main.instance.getLogger().warning(key+" -> "+itemNameMap.get(key));
		}
		
		QuickShopXPlugin.instance.getLogger().info("Loaded "+itemNameMap.size()+" item names");
	}

	
	public String getItemName(Item item) 
	{
		if ((item.hasCustomName()) && (item.getCustomName()!=null) && (!item.getCustomName().isEmpty()))
		{
			return item.getCustomName()+"&r";
		}

		if(QuickShopXPlugin.instance.pluginConfig.useCustomItemName)
		{
			String key = item.getId()+":"+item.getDamage();

			if(itemNameMap.containsKey(key))
			{
				return itemNameMap.get(key)+"&r";
			}
		}

		return item.getName();
	}
}
