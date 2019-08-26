package cn.innc11.QuickShop2.shop;

import cn.innc11.QuickShop2.QuickShop2Plugin;
import cn.innc11.QuickShop2.config.LangConfig.Lang;

public enum ShopType 
{
	
	BUY,
	SELL;
	
	@Override
	public String toString() 
	{
		return QuickShop2Plugin.instance.langConfig.get(Lang.valueOf(this.name()));
	}
}
