package cn.innc11.QuickShopX.shop;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.config.LangConfig.Lang;

public enum ShopType 
{
	BUY,
	SELL;
	
	@Override
	public String toString() 
	{
		return QuickShopXPlugin.instance.langConfig.get(Lang.valueOf(this.name()));
	}
}
