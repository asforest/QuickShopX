package cn.innc11.QuickShopX.utils;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.config.LangConfig.Lang;

public class L //language
{
	public static String get(Lang l, String... args)
	{
		return QuickShopXPlugin.instance.langConfig.get(l, args);
	}
}
