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
		Lang a = Lang.valueOf(this.name().toLowerCase());
		return Quickshopx.ins.langConfig.get(a);
	}
}
