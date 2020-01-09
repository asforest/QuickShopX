package cn.innc11.QuickShopX.config;

public class PluginConfig extends MyConfig 
{
	public int configVersion;
	
	public int interactionInterval;
	public boolean hologramItemShow;
	public FormOperate formOperate;
	public int packetSendPerSecondMax;
	public boolean interactionWithResidencePlugin;
	public boolean createShopInResidenceOnly;
	public boolean opIgnoreResidenceBuildPermission;
	public boolean snakeModeDestroyShop;
	public boolean hopperActiveInResidenceOnly;
	public boolean useCustomItemName;
	
	public PluginConfig()
	{
		super("config.yml");
		
		reload();
	}

	@Override
	public void save() 
	{
		config.getRootSection().clear();
		
		config.set("version", configVersion);
		
		config.set("interactionInterval", interactionInterval);
		config.set("hologramItemShow", hologramItemShow);
		config.set("formOperate", formOperate.text);
		config.set("packetSendPerSecondMax", packetSendPerSecondMax);
		config.set("interactionWithResidencePlugin", interactionWithResidencePlugin);
		config.set("createShopInResidenceOnly", createShopInResidenceOnly);
		config.set("opIgnoreResidenceBuildPermission", opIgnoreResidenceBuildPermission);
		config.set("snakeModeDestroyShop", snakeModeDestroyShop);
		config.set("hopperActiveInResidenceOnly", hopperActiveInResidenceOnly);
		config.set("useCustomItemName", useCustomItemName);

		config.save();
	}

	@Override
	public void reload() 
	{
		config.reload();
		
		configVersion = config.getInt("version");
		
		interactionInterval = config.getInt("interactionInterval", 7000);
		
		hologramItemShow = config.getBoolean("hologramItemShow", false);
		
		String temp = config.getString("formOperate", "doubleclick");
		
		switch (temp) {
		case "never":
			formOperate = FormOperate.NEVER;
			break;
		case "doubleclick":
			formOperate = FormOperate.DOUBLE_CLICK;
			break;
		case "always":
			formOperate = FormOperate.ALWAYS;
			break;
		}
		
		packetSendPerSecondMax = config.getInt("packetSendPerSecondMax", 40);
		
		interactionWithResidencePlugin = config.getBoolean("interactionWithResidencePlugin", true);
		
		createShopInResidenceOnly = config.getBoolean("createShopInResidenceOnly", true);
		
		opIgnoreResidenceBuildPermission = config.getBoolean("opIgnoreResidenceBuildPermission", false);

		snakeModeDestroyShop = config.getBoolean("snakeModeDestroyShop", true);

		hopperActiveInResidenceOnly = config.getBoolean("hopperActiveInResidenceOnly", true);

		useCustomItemName = config.getBoolean("useCustomItemName", false);

	}

	public static enum FormOperate
	{
		NEVER("never"),
		DOUBLE_CLICK("doubleclick"),
		ALWAYS("always");
		
		public String text;
		
		FormOperate(String text)
		{
			this.text = text;
		}
	}
}
