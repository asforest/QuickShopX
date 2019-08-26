package cn.innc11.QuickShop2;

import java.util.regex.Pattern;

import cn.innc11.QuickShop2.command.QuickShop2Command;
import cn.innc11.QuickShop2.config.ItemNameConfig;
import cn.innc11.QuickShop2.config.LangConfig;
import cn.innc11.QuickShop2.config.PluginConfig;
import cn.innc11.QuickShop2.config.ShopConfig;
import cn.innc11.QuickShop2.config.SignTextConfig;
import cn.innc11.QuickShop2.listener.CreateShopListener;
import cn.innc11.QuickShop2.listener.FormResponseListener;
import cn.innc11.QuickShop2.listener.HologramItem;
import cn.innc11.QuickShop2.listener.InteractionShopListener;
import cn.innc11.QuickShop2.listener.ShopProtectListener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class QuickShop2Plugin extends PluginBase
{
	public static QuickShop2Plugin instance;
	
	public boolean residencePluginLoaded = false;
	
	public ShopConfig shopConfig;
	public ItemNameConfig itemNameConfig;
	public SignTextConfig signTextConfig;
	public LangConfig langConfig;
	public PluginConfig pluginConfig;
	
	public CreateShopListener createShopListenerInstance;
	public InteractionShopListener interactionShopListenerInstance;
	public HologramItem hologramListener;
	public FormResponseListener formResponseListener;
	
	@Override
	public void onEnable() 
	{
		instance = this;
		
		if(getServer().getPluginManager().getPlugin("QuickShop")!=null) 
		{
			getLogger().warning(TextFormat.colorize("&4QuickShop插件和QuickShop2插件只能二选一"));
			getLogger().warning(TextFormat.colorize("&4Choose one of QuickShop and QuickShop2"));
			getServer().getPluginManager().disablePlugin(this);
			
			return;
		}
		
		residencePluginLoaded = getServer().getPluginManager().getPlugin("Residence")!=null;
		
		
		if(residencePluginLoaded)
		{
			getLogger().info(TextFormat.colorize("&aSuccessfully linked with Residence!"));
		}
		
		loadConfig();
		
		createShopListenerInstance = new CreateShopListener();
		interactionShopListenerInstance = new InteractionShopListener();
		hologramListener = new HologramItem(this);
		formResponseListener = new FormResponseListener();
		
		getServer().getPluginManager().registerEvents(createShopListenerInstance, this);
		getServer().getPluginManager().registerEvents(interactionShopListenerInstance, this);
		getServer().getPluginManager().registerEvents(hologramListener, this);
		getServer().getPluginManager().registerEvents(formResponseListener, this);
		
		/////
		getServer().getPluginManager().registerEvents(new ShopProtectListener(), this);

		getServer().getCommandMap().register("", new QuickShop2Command());
		
	}
	
	void loadConfig()
	{
		saveResource("shops.yml", false);
		saveResource("itemName.yml", false);
		saveResource("signText.yml", false);
		saveResource("language.yml", false);
		saveResource("config.yml", false);
		
		shopConfig = new ShopConfig();
		itemNameConfig = new ItemNameConfig();
		signTextConfig = new SignTextConfig();
		langConfig = new LangConfig();
		pluginConfig = new PluginConfig();
		
	}
	
	public static boolean isInteger(String str)
	{
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
	
	public static boolean isPrice(String str)
	{
		Pattern pattern = Pattern.compile("^[-\\+]?\\d+(\\.\\d+)?$");
		return pattern.matcher(str).matches();
	}
}
