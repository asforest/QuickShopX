package cn.innc11.QuickShopX.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.utils.TextFormat;

public class LangConfig extends MyConfig 
{
	HashMap<Lang, String> lang = new HashMap<Lang, String>();
	
	public LangConfig()
	{
		super("language.yml");
		
		reload();
	}

	@Override
	public void save() 
	{
		config.save();
	}

	@Override
	public void reload() 
	{
		config.reload();
		lang.clear();

		boolean supplement = false;
		int ct = 0;
		
		for(Lang key : Lang.values())
		{
			Object v = config.get(key.name());

			if(v==null)
			{
				config.set(key.name(), key.getDefaultLangText());
				QuickShopXPlugin.instance.getLogger().info(TextFormat.colorize("&4set default language for language.yml("+key.name()+")"));
				supplement = true;
				lang.put(key, key.getDefaultLangText());
			}

			if(v instanceof String)
			{
				lang.put(key, (String) v);
				ct++;
			} else if(v instanceof ArrayList)
			{
				ArrayList<String> values = (ArrayList<String>) v;

				StringBuffer sb = new StringBuffer();

				for(int i=0;i<values.size();i++)
				{
					sb.append(values.get(i));
					if(i!=values.size()-1) {
						sb.append("\n");
					}
				}

				lang.put(key, sb.toString());
				ct++;
			}

			
		}

		if(supplement)
			save();
		
		QuickShopXPlugin.instance.getLogger().info("Loaded "+ct+" languages.");
	}
	
	public String get(Lang l, String... argsPair)
	{
		String rawStr = lang.get(l);
		
		int argCount = argsPair.length / 2;
		
		for(int i=0;i<argCount;i++)
		{
			String reg = argsPair[i*2];
			String replacement = argsPair[i*2+1];
			
			if(reg.startsWith("{") && reg.endsWith("}"))
			{
				reg = reg.replaceAll("\\{", "\\\\{");
				reg = reg.replaceAll("\\}", "\\\\}");
				rawStr = rawStr.replaceAll("\\$"+reg, replacement);
			}
			
		}
        
		
		return TextFormat.colorize(rawStr);
	}

	public static enum Lang
	{
		BUY,
		SELL,
		SERVER_SHOP_NICKNAME,

		ENCHANTMENT__TEXT,
		ENCHANTMENT__PER_LINE,
		ENCHANTMENT__PER_PREFIX,
		ENCHANTMENT__PER_SUFFIX,
		ENCHANTMENT__PER_PREFIX_FIRST,
		ENCHANTMENT__PER_SUFFIX_LAST,
		ENCHANTMENT__MULTIPLE_SIGN,
		
		IM_SHOP_TYPE_UPDATED,
		IM_SHOP_TYPE_DONT_NEED_UPDATE,
		IM_PRICE_CANT_UPDATE_NOT_ONWER,
		IM_INTERACTOIN_TIMEOUT,
		IM_NOT_SELECTED_SHOP,
		IM_INTERCEPT_CONSOLE,
		IM_SHOP_PRICE_UPDATED,
		IM_PRICE_WRONG_FORMAT,
		IM_PRICE_WRONG_ARGS,
		IM_SHOP_UPDATED_SERVER_SHOP,
		IM_SHOP_UPDATED_ORDINARY,
		IM_SHOP_INFO_SHOW,
		IM_ENTER_TRANSACTIONS_VOLUME,
		IM_NOT_A_NUMBER,
		IM_SUCCEESSFULLY_REMOVED_SHOP,
		IM_NO_REMOVE_SHOP_NO_ONWER,
		IM_NO_REMOVE_SHOP_EXISTS_SIGN,
		IM_CREATING_SHOP_ENTER_PRICE,
		IM_SUCCEESSFULLY_CREATED_SHOP,
		IM_NO_ITEM_IN_HAND,
		IM_SHOP_SIGN_BLOCKED,
		IM_TRADE_CANCELED,
		IM_BUYSHOP_OWNER,
		IM_BUYSHOP_CUSTOMER,
		IM_STOCK_FULL,
		IM_ITEM_NOT_ENOUGH,
		IM_SHOP_OWNER_NOT_ENOUGH_MONEY,
		IM_SELLSHOP_OWNER,
		IM_SELLSHOP_CUSTOMER,
		IM_BACKPACK_FULL,
		IM_SHOP_SOLD_OUT,
		IM_INSUFFICIENT_SHOP_STOCK,
		IM_NOT_ENOUGH_MONEY_TO_BUYING,
		IM_SHOP_INFO_UPDATED,
		IM_SNAKE_MODE_DESTROY_SHOP,
		IM_NO_RESIDENCE_PERMISSION,
		IM_CREATE_SHOP_IN_RESIDENCE_NOLY,
		IM_CHEST_SIGN_DIFFERENT_RESIDENCE,
		IM_SIGN_NOT_IN_A_RESIDENCE_RANGE,
		IM_SIGN_NOT_ALLOWED_IN_A_RESIDENCE,
		IM_NOT_SHOP_OWNER_CANNOT_OPEN_CHEST,

		FORM_SHOPDATA__TITLE,
		FORM_SHOPDATA__UNIT_PRICE,
		FORM_SHOPDATA__SHOP_OWNER,
		FORM_SHOPDATA__SHOP_TYPE,
		FORM_SHOPDATA__SERVER_SHOP,
		FORM_TRADING__TITLE,
		FORM_TRADING__SHOP_INFO,
		FORM_TRADING__TRADING_VOLUME,
		FORM_MASTER__TITLE,
		FORM_MASTER__CONTENT,
		FORM_MASTER__BUTTON_SHOP_DATA_PANEL,
		FORM_MASTER__BUTTON_SHOP_TRADING_PANEL,
		FORM_MASTER__BUTTON_REMOVE_SHOP,
		FORM_CONTROL_PANEL__TITLE,
		FORM_CONTROL_PANEL__SHOP_INTERACTION_TIME,
		FORM_CONTROL_PANEL__SHOP_HOLOGRAM_ITEM,
		FORM_CONTROL_PANEL__FORM_OPERATE,
		FORM_CONTROL_PANEL__PACKET_SEND_PS,
		FORM_CONTROL_PANEL__INTERACTION_WITH_RESIDENCE_PLUGIN,
		FORM_CONTROL_PANEL__CREATE_SHOP_IN_RESIDENCE_NOLY,
		FORM_CONTROL_PANEL__OP_IGNORE_RESIDENCE_BUILD_PERMISSION,
		FORM_CONTROL_PANEL__SNAKE_MODE_DESTROY_SHOP,
		FORM_CONTROL_PANEL__HOPPER_ACTIVE_IN_RESIDENCE_ONLY,
		FORM_CONTROL_PANEL__USE_CUSTOM_ITEM_NAME,

		PLUGIN_MESSAGE_RELOAD_DONE,
		PLUGIN_MESSAGE_PLUGIN_CONFIGURE_UPDATED,
		PLUGIN_MESSAGE_HELP_NORMAL,
		PLUGIN_MESSAGE_HELP_OPERATOR;

		public String getDefaultLangText()
		{
			return name();
		}

		public static boolean contains(String value)
		{
			for(Lang lang : values())
			{
				if(lang.name().equals(value))
					return true;
			}
			return false;
		}
	}

}
