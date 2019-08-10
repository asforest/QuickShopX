package cn.innc11.QuickShop2.utils;

import cn.innc11.QuickShop2.Main;
import cn.innc11.QuickShop2.config.LangConfig.Lang;

public class L //language
{
	public static String get(Lang l, String... args)
	{
		return Main.instance.langConfig.get(l, args);
	}
}
