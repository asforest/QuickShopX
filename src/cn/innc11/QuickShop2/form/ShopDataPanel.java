package cn.innc11.QuickShop2.form;

import java.util.Arrays;

import cn.innc11.QuickShop2.Main;
import cn.innc11.QuickShop2.config.LangConfig.Lang;
import cn.innc11.QuickShop2.shop.Shop;
import cn.innc11.QuickShop2.shop.ShopType;
import cn.innc11.QuickShop2.utils.L;
import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;

public class ShopDataPanel extends FormWindowCustom implements FormRespone
{
	String shopKey;
	String playerName;
	
	public ShopDataPanel(Shop shop, String playerName)
	{
//		super("商店信息面板");
		super(L.get(Lang.FORM_SHOPDATA__TITLE));
		
		this.shopKey = shop.getShopKey();
		this.playerName = playerName;
		
		Player player = Main.instance.getServer().getPlayerExact(playerName);
		
//		addElement(new ElementInput("价格", "", shop.getFormatPrice()));
		addElement(new ElementInput(L.get(Lang.FORM_SHOPDATA__UNIT_PRICE), "", shop.getFormatPrice()));
		
//		addElement(new ElementInput("店长", "", shop.data.owner));
		addElement(new ElementInput(L.get(Lang.FORM_SHOPDATA__SHOP_OWNER), "", shop.data.owner));
		
//		addElement(new ElementDropdown("类型", Arrays.asList("购买", "回收"), shop.data.type==ShopType.BUY ? 0 : 1));
		addElement(new ElementDropdown(L.get(Lang.FORM_SHOPDATA__SHOP_TYPE), Arrays.asList(L.get(Lang.BUY), L.get(Lang.SELL)), shop.data.type==ShopType.BUY ? 0 :1));
	
		if(player.isOp())
			addElement(new ElementToggle(L.get(Lang.FORM_SHOPDATA__UNLIMITED_SHOP), shop.data.unlimited));
	//		addElement(new ElementToggle("无限商店", shop.data.unlimited));

	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		Shop shop = Shop.getShopInstance(shopKey);
		Player player = Main.instance.getServer().getPlayerExact(playerName);

		String ePrice = getResponse().getInputResponse(0);
		String eOwner = getResponse().getInputResponse(1);
		int eShopType = getResponse().getDropdownResponse(2).getElementID();
		
		
		
		if(!Main.isPrice(ePrice))
		{
//			e.getPlayer().sendMessage("价格不是一个数字");
			e.getPlayer().sendMessage(L.get(Lang.IM_PRICE_WRONG_FORMAT));
			return;
		}
		
		
		shop.data.price = Float.parseFloat(ePrice);
		shop.data.type = eShopType==0 ? ShopType.BUY : ShopType.SELL;
		shop.data.owner = eOwner;
		
		if(player.isOp())
			shop.data.unlimited = getResponse().getToggleResponse(3);
		
		shop.updateSignText();
		
		Main.instance.shopConfig.save();
		
		player.sendMessage(L.get(Lang.IM_SHOP_INFO_UPDATED));
	}

}
