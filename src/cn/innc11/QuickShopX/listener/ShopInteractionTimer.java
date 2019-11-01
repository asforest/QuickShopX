package cn.innc11.QuickShopX.listener;

import cn.innc11.QuickShopX.utils.Pair;

public interface ShopInteractionTimer 
{
	// return null: not found the player
	// return object: <isVaild, shop>
	public Pair<Boolean, ?> isVaildInteraction(String player);
	
}
