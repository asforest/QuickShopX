package cn.innc11.QuickShop2.config;

import java.util.ArrayList;
import java.util.HashMap;

import cn.innc11.QuickShop2.Main;
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
		for(Lang p : Lang.values())
		{
			config.set(p.name(), "simple");
		}
		
		config.save();
	}

	@Override
	public void reload() 
	{
		lang.clear();
		
		int ct = 0;
		
		for(String key : config.getKeys(false))
		{
			Object v = config.get(key);
			
			if(Lang.contains(key))
			{
				if(v instanceof String)
				{
					lang.put(Lang.valueOf(key), (String) v);
					
					ct++;
				} else if(v instanceof ArrayList)
				{
					@SuppressWarnings("unchecked")
					ArrayList<String> values = (ArrayList<String>) v;
					
					StringBuffer sb = new StringBuffer();
					
					for(int i=0;i<values.size();i++)
					{
						sb.append(values.get(i));
						if(i!=values.size()-1)
							sb.append("\n");
					}
					
					lang.put(Lang.valueOf(key), sb.toString());
					
					ct++;
				}
			}
			
			
		}
		
		Main.instance.getLogger().info("Loaded "+ct+" Languages.");
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
		
		IM_SHOP_TYPE_UPDATED,
		IM_SHOP_TYPE_NO_UPDATE,
		IM_PRICE_NO_UPDATE_NO_ONWER,
		IM_INTERACTOIN_TIMEOUT,
		IM_NO_SELECTED_SHOP,
		IM_INTERCEPT_CONSOLE,
		IM_SHOP_PRICE_UPDATED,
		IM_PRICE_WRONG_FORMAT,
		IM_PRICE_NO_UPDATE_WRONG_ARGS,
		IM_SHOP_UPDATED_UNLIMITED,
		IM_SHOP_UPDATED_LIMITED,
		IM_SHOP_INFO_SHOW,
		IM_ENTER_TRANSACTIONS_COUNT,
		IM_NO_ENTER_NUMBER,
		IM_SUCCEESSFULLY_REMOVED_SHOP,
		IM_NO_REMOVE_SHOP_NO_ONWER,
		IM_NO_REMOVE_SHOP_EXISTS_SIGN,
		IM_CREATING_SHOP_ENTER_PRICE,
		IM_SUCCEESSFULLY_CREATED_SHOP,
		IM_NO_ITEM_ON_HAND,
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
		
		FORM_SHOPDATA__TITLE,
		FORM_SHOPDATA__UNIT_PRICE,
		FORM_SHOPDATA__SHOP_OWNER,
		FORM_SHOPDATA__SHOP_TYPE,
		FORM_SHOPDATA__UNLIMITED_SHOP,
		FORM_TRADING__TITLE,
		FORM_TRADING__SHOP_INFO,
		FORM_TRADING__TRADING_VOLUME,
		FORM_MASTER__TITLE,
		FORM_MASTER__CONTENT,
		FORM_MASTER__BUTTON_SHOP_DATA_PANEL,
		FORM_MASTER__BUTTON_SHOP_TRADING_PANEL,
		FORM_MASTER__BUTTON_REMOVE_SHOP,
		
		PLUGIN_MESSAGE_RELOAD_DONE,
		PLUGIN_MESSAGE_PLUGIN_CONFIGURE_UPDATED,
		PLUGIN_MESSAGE_HELP_NORMAL,
		PLUGIN_MESSAGE_HELP_OPERATOR,
		FORM_CONTROL_PANEL__TITLE,
		FORM_CONTROL_PANEL__SHOP_INTERACTION_TIME,
		FORM_CONTROL_PANEL__SHOP_HOLOGRAM_ITEM,
		FORM_CONTROL_PANEL__FORM_OPERATE,
		FORM_CONTROL_PANEL__PACKET_SEND_PS,
		FORM_CONTROL_PANEL__INTERACTION_WITH_RESIDENCE_PLUGIN,
		FORM_CONTROL_PANEL__CREATE_SHOP_IN_RESIDENCE_NOLY,
		FORM_CONTROL_PANEL__OP_IGNORE_RESIDENCE,
		
		IM_NO_RESIDENCE_PERMISSION,
		IM_CREATE_SHOP_IN_RESIDENCE_NOLY,
		IM_CHEST_SIGN_DIFFERENT_RESIDENCE,
		IM_SIGN_NOT_IN_A_RESIDENCE_RANGE,
		IM_SIGN_NOT_ALLOWED_IN_A_RESIDENCE;
		
		
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
