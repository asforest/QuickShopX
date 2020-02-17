package cn.innc11.quickshopx.form;

import java.util.Arrays;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.shop.Shop;
import cn.innc11.quickshopx.shop.ShopType;
import cn.innc11.quickshopx.utils.L;
import cn.innc11.quickshopx.utils.Lang;
import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;

public class ShopDataPanel extends FormWindowCustom implements FormResponse
{
	String shopKey;
	String playerName;
	
	public ShopDataPanel(Shop shop, String playerName)
	{
		super(L.get(Lang.shopdata_title));
		
		this.shopKey = shop.getShopKey();
		this.playerName = playerName;
		
		Player player = Quickshopx.server.getPlayerExact(playerName);
		
		addElement(new ElementInput(L.get(Lang.shopdata_price), "", shop.getStringPrice()));

		addElement(new ElementDropdown(L.get(Lang.shopdata_type), Arrays.asList(L.get(Lang.buy), L.get(Lang.sell)), shop.shopData.type==ShopType.BUY ? 0 :1));
	
		if(player.isOp())
		{
			addElement(new ElementInput(L.get(Lang.shopdata_owner), "", shop.shopData.owner));

			addElement(new ElementToggle(L.get(Lang.shopdata_server_shop), shop.shopData.serverShop));
		}
	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		Shop shop = Shop.getShopInstance(shopKey);
		Player player = Quickshopx.server.getPlayerExact(playerName);

		if(!player.isOp() && !shop.shopData.owner.equals(player.getName()))
			return;

		String price = getResponse().getInputResponse(0);
		int shopType = getResponse().getDropdownResponse(1).getElementID();

		if(!Quickshopx.isPrice(price))
		{
			e.getPlayer().sendMessage(L.get(Lang.im_price_wrong_format));
			return;
		}
		
		shop.shopData.price = Float.parseFloat(price);
		shop.shopData.type = shopType==0 ? ShopType.BUY : ShopType.SELL;

		if(player.isOp())
		{
			shop.shopData.owner = getResponse().getInputResponse(2);
			shop.shopData.serverShop = getResponse().getToggleResponse(3);
		}else{
			shop.shopData.serverShop = false;
		}

		shop.updateSignText();

		Quickshopx.ins.multiShopsConfig.getShopsConfig(shop, false).save();

		player.sendMessage(L.get(Lang.im_shop_data_updated));
	}

	@Override
	public void onFormClose(PlayerFormRespondedEvent e)
	{

	}

}
