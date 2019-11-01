package cn.innc11.QuickShop2.shop;

import cn.innc11.QuickShop2.QuickShopXPlugin;
import cn.innc11.QuickShop2.config.LangConfig.Lang;

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
