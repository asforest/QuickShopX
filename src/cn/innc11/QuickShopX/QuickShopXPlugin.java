package cn.innc11.QuickShopX;

import java.io.File;
import java.util.regex.Pattern;

import cn.innc11.QuickShopX.command.QuickShopXCommand;
import cn.innc11.QuickShopX.config.*;
import cn.innc11.QuickShopX.crossVersion.Analyst;
import cn.innc11.QuickShopX.listener.*;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class QuickShopXPlugin extends PluginBase
{
	public static QuickShopXPlugin instance;

	public boolean residencePluginLoaded = false;
	
	public ShopConfig shopConfig;
	public ItemNamesConfig itemNameConfig;
	public SignTextConfig signTextConfig;
	public LangConfig langConfig;
	public PluginConfig pluginConfig;
	public EnchantmentNamesConfig enchantmentNamesConfig;
	
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
		//String[] pluginDepends = getDescription().getDepend().toArray(new String[0]);
		
		if(getServer().getPluginManager().getPlugin("QuickShop")!=null) 
		{
			getLogger().warning(TextFormat.colorize("&cQuickShop插件无法和"+pluginName+"插件一起工作"));
			getLogger().warning(TextFormat.colorize("&cThe QuickShop cannot coexist with "+pluginName));
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
		hologramListener = new HologramItemListener(this);
		formResponseListener = new FormResponseListener();
		shopProtectListener = new ShopProtectListener();
		itemAndInventoryListener = new ItemAndInventoryListener();

		registerEvents();

		registerCommands();
	}

	void registerEvents()
	{
		getServer().getPluginManager().registerEvents(interactionShopListenerInstance, this);
		getServer().getPluginManager().registerEvents(createShopListenerInstance, this);
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
		saveResource("enchantmentNames.yml", false);

		pluginConfig = new PluginConfig();
		shopConfig = new ShopConfig();
		itemNameConfig = new ItemNamesConfig();
		signTextConfig = new SignTextConfig();
		langConfig = new LangConfig();
		enchantmentNamesConfig = new EnchantmentNamesConfig();

		Analyst.check();
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
