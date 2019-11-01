package cn.innc11.QuickShop2.listener;

import cn.innc11.QuickShop2.utils.Pair;

public interface ShopInteractionTimer 
{
	// return null: not found the player
	// return object: <isVaild, shop>
	public Pair<Boolean, ?> isVaildInteraction(String player);
	
}
