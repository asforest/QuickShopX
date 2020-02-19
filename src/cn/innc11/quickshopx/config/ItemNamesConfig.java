package cn.innc11.quickshopx.config;

import java.io.File;
import java.util.HashMap;

import cn.innc11.quickshopx.Quickshopx;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;

public class ItemNamesConfig extends BaseConfig
{
	HashMap<String, String> itemNameMap = new HashMap<>();
	
	public ItemNamesConfig(File file)
	{
		super(file, file.exists(), false);

		if(file.exists())
		{
			Quickshopx.logger.info(TextFormat.colorize("&6The custom item naming file found"));
			reload();
		}
	}

	@Override
	public void Save()
	{

	}

	@Override
	public void reload() 
	{
		config.reload();
		itemNameMap.clear();
		
		Object configMap = config.get("mapping");
		
		if(configMap!=null)
		{
			HashMap<String, String> cMap = (HashMap<String, String>) configMap;
			for(String key : cMap.keySet())
			{
				Item item = Item.fromString(key);
				
				if(item.getId()!=0)
				{
					itemNameMap.put(item.getId()+":"+item.getDamage(), cMap.get(key));
				}
			}
			
		}
		
		Quickshopx.logger.info(TextFormat.colorize(String.format("Loaded &6%d&r item names", itemNameMap.size())));
	}

	
	public String getItemName(Item item) 
	{
		return getPureItemName(item)+"&r";
	}

	public String getPureItemName(Item item)
	{
		if ((item.hasCustomName()) && (item.getCustomName()!=null) && (!item.getCustomName().isEmpty()))
		{
			return item.getCustomName();
		}

		if(Quickshopx.ins.pluginConfig.useCustomItemNames)
		{
			String key = item.getId()+":"+item.getDamage();

			if(itemNameMap.containsKey(key))
			{
				return itemNameMap.get(key);
			}
		}

		return item.getName();
	}

}
