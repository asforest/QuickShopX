package cn.innc11.QuickShop2.config;

import java.io.File;

import cn.innc11.QuickShop2.Main;
import cn.nukkit.utils.Config;

public abstract class MyConfig 
{
	Config config;
	
	public MyConfig(String fileName)
	{
		config = new Config(new File(Main.instance.getDataFolder(), fileName), Config.YAML);
	}
	
	public abstract void save();
	
	public abstract void reload();
}
