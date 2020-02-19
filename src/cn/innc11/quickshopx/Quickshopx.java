package cn.innc11.quickshopx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import cn.innc11.quickshopx.command.QuickShopXCommand;
import cn.innc11.quickshopx.config.*;
import cn.innc11.quickshopx.crossVersion.Shops_v0;
import cn.innc11.quickshopx.crossVersion.Shops_v1;
import cn.innc11.quickshopx.listener.*;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.ServerScheduler;
import cn.nukkit.utils.Logger;
import cn.nukkit.utils.TextFormat;

public class Quickshopx extends PluginBase
{
	public static Quickshopx ins;
	public static ServerScheduler scheduler;
	public static Server server;
	public static Logger logger;

	public boolean residencePluginLoaded = false;
	
	public MultiShopsConfig multiShopsConfig;
	public ItemNamesConfig itemNamesConfig;
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
		ins = this;
		scheduler = getServer().getScheduler();
		server = getServer();
		logger  = getLogger();

		String pluginName = getDescription().getName();

		if(getServer().getPluginManager().getPlugin("QuickShop")!=null) 
		{
			getLogger().warning(TextFormat.colorize("&cQuickShop插件无法和"+pluginName+"插件同时被加载"));
			getLogger().warning(TextFormat.colorize("&cThe QuickShop cannot coexist with "+pluginName));
			getServer().getPluginManager().disablePlugin(this);
			
			return;
		}
		
		residencePluginLoaded = getServer().getPluginManager().getPlugin("Residence")!=null;

		if(residencePluginLoaded)
		{
			getLogger().info(TextFormat.colorize("&aSuccessfully linked with Residence!"));
		}

		try
		{
			loadConfig();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			server.getPluginManager().disablePlugin(this);
			return;
		}

		createShopListenerInstance = new CreateShopListener();
		interactionShopListenerInstance = new InteractionShopListener();
		hologramListener = new HologramItemListener(this);
		formResponseListener = new FormResponseListener();
		shopProtectListener = new ShopProtectListener();
		itemAndInventoryListener = new ItemAndInventoryListener();

		registerListeners();

		registerCommands();
	}

	void registerListeners()
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
	
	public void loadConfig() throws FileNotFoundException
	{
		saveResource("lang/enchantments-cn.yml", false);
		saveResource("lang/enchantments-en.yml", false);
		saveResource("lang/languages-cn.yml", false);
		saveResource("lang/languages-en.yml", false);
		saveResource("lang/signTexts-cn.yml", false);
		saveResource("lang/signTexts-en.yml", false);

		analyse();

		File itemNamesFile = new File(getDataFolder(), "item-names.yml");
		File shopsDir = new File(getDataFolder(), "shops");

		pluginConfig = new PluginConfig(new File(getDataFolder(), "config.yml"));
		multiShopsConfig = new MultiShopsConfig(shopsDir);
		itemNamesConfig = new ItemNamesConfig(itemNamesFile, pluginConfig.useCustomItemNames);
		signTextConfig = new SignTextConfig(new File(getDataFolder(), String.format("lang/signTexts-%s.yml", pluginConfig.language)));
		langConfig = new LangConfig(new File(getDataFolder(), String.format("lang/languages-%s.yml", pluginConfig.language)));
		enchantmentNamesConfig = new EnchantmentNamesConfig(new File(getDataFolder(), String.format("lang/enchantments-%s.yml", pluginConfig.language)));
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

	public static void analyse()
	{
		Shops_v0.convert();
		Shops_v1.convert();
	}


}
