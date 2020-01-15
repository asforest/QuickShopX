package cn.innc11.QuickShopX.config;

import java.io.File;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.nukkit.utils.Config;

public abstract class MyConfig 
{
	public Config config;
	
	public MyConfig(String fileName)
	{
		config = new Config(new File(QuickShopXPlugin.instance.getDataFolder(), fileName), Config.YAML);
	}
	
	public abstract void save();
	
	public abstract void reload();
}
