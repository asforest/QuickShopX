package cn.innc11.QuickShop2.config;

import java.util.HashMap;

import cn.innc11.QuickShop2.QuickShop2Plugin;
import cn.nukkit.item.Item;

public class ItemNameConfig extends MyConfig 
{
	HashMap<String, String> itemNameMap = new HashMap<>();
	
	public ItemNameConfig()
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
		
		QuickShop2Plugin.instance.getLogger().info("Loaded "+itemNameMap.size()+" ItemName");
	}

	
	public String getItemName(Item item) 
	{
		String key = item.getId()+":"+item.getDamage();
		
//		Main.instance.getLogger().warning("find: "+key);
		
		
		if(itemNameMap.containsKey(key))
		{
			return itemNameMap.get(key);
		}
		
		return item.getName();
	}
}
