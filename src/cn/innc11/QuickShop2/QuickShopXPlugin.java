package cn.innc11.QuickShop2;

import java.util.regex.Pattern;

import cn.innc11.QuickShop2.command.QuickShopXCommand;
import cn.innc11.QuickShop2.config.ItemNameConfig;
import cn.innc11.QuickShop2.config.LangConfig;
import cn.innc11.QuickShop2.config.PluginConfig;
import cn.innc11.QuickShop2.config.ShopConfig;
import cn.innc11.QuickShop2.config.SignTextConfig;
import cn.innc11.QuickShop2.listener.*;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class QuickShopXPlugin extends PluginBase
{
	public static QuickShopXPlugin instance;
	
	public boolean residencePluginLoaded = false;
	
	public ShopConfig shopConfig;
	public ItemNameConfig itemNameConfig;
	public SignTextConfig signTextConfig;
	public LangConfig langConfig;
	public PluginConfig pluginConfig;
	
	public CreateShopListener createShopListenerInstance;
	public InteractionShopListener interactionShopListenerInstance;
	public HologramItemListener hologramListener;
	public FormResponseListener formResponseListener;
	public ShopProtectListener shopProtectListener;
	public ItemAndInventoryListener itemAndInventoryListener;
	
	@Override
	public void onEnable() 
	{
		instance = this;

		String pluginName = getDescription().getName();
		String[] pluginDepends = getDescription().getDepend().toArray(new String[0]);
		
		if(getServer().getPluginManager().getPlugin("QuickShop")!=null) 
		{
			getLogger().warning(TextFormat.colorize("&4QuickShop插件和"+pluginName+"插件只能二选一"));
			getLogger().warning(TextFormat.colorize("&4Choose one of QuickShop or "+pluginName));
			getServer().getPluginManager().disablePlugin(this);
			
			return;
		}
		
		residencePluginLoaded = getServer().getPluginManager().getPlugin("Residence")!=null;

		if(residencePluginLoaded) getLogger().info(TextFormat.colorize("&aSuccessfully linked with Residence!"));
		
		loadConfig();
		
		createShopListenerInstance = new CreateShopListener();
		interactionShopListenerInstance = new InteractionShopListener();
		hologramListener = new HologramItemListener(this);
		formResponseListener = new FormResponseListener();
		shopProtectListener = new ShopProtectListener();
		itemAndInventoryListener = new ItemAndInventoryListener();

		registerEvents();

		registerCommands();
	}

	void registerEvents()
	{
		getServer().getPluginManager().registerEvents(createShopListenerInstance, this);
		getServer().getPluginManager().registerEvents(interactionShopListenerInstance, this);
		getServer().getPluginManager().registerEvents(hologramListener, this);
		getServer().getPluginManager().registerEvents(formResponseListener, this);
		getServer().getPluginManager().registerEvents(shopProtectListener, this);
		getServer().getPluginManager().registerEvents(itemAndInventoryListener, this);
	}

	void registerCommands()
	{
		getServer().getCommandMap().register("", new QuickShopXCommand());
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
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
		return pattern.matcher(str).matches();
	}
}
