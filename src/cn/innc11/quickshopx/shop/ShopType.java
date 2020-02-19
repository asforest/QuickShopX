package cn.innc11.quickshopx.shop;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.utils.Lang;

public enum ShopType 
{
	BUY,
	SELL;
	
	@Override
	public String toString() 
	{
		return Quickshopx.ins.langConfig.get(Lang.valueOf(this.name().toLowerCase()));
	}
}
