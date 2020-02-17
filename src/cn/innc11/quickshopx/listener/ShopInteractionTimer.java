package cn.innc11.quickshopx.listener;

import cn.innc11.quickshopx.utils.Pair;

public interface ShopInteractionTimer 
{
	// return null: not found the player
	// return object: <isVaild, shop>
	public Pair<Boolean, ?> isVaildInteraction(String player);
	
}
