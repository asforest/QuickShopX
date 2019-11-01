package cn.innc11.QuickShop2.form;

import java.util.Arrays;

import cn.innc11.QuickShop2.QuickShopXPlugin;
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
		
		Player player = QuickShopXPlugin.instance.getServer().getPlayerExact(playerName);
		
//		addElement(new ElementInput("价格", "", shop.getFormatPrice()));
		addElement(new ElementInput(L.get(Lang.FORM_SHOPDATA__UNIT_PRICE), "", shop.getStringPrice()));

//		addElement(new ElementDropdown("类型", Arrays.asList("购买", "回收"), shop.data.type==ShopType.BUY ? 0 : 1));
		addElement(new ElementDropdown(L.get(Lang.FORM_SHOPDATA__SHOP_TYPE), Arrays.asList(L.get(Lang.BUY), L.get(Lang.SELL)), shop.data.type==ShopType.BUY ? 0 :1));
	
		if(player.isOp())
		{
			//		addElement(new ElementInput("店长", "", shop.data.owner));
			addElement(new ElementInput(L.get(Lang.FORM_SHOPDATA__SHOP_OWNER), "", shop.data.owner));

			addElement(new ElementToggle(L.get(Lang.FORM_SHOPDATA__SERVER_SHOP), shop.data.serverShop));
			//		addElement(new ElementToggle("服务器商店", shop.data.serverShop));
		}
	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		Shop shop = Shop.getShopInstance(shopKey);
		Player player = QuickShopXPlugin.instance.getServer().getPlayerExact(playerName);

		if(!player.isOp() && !shop.data.owner.equals(player.getName()))
			return;

		String ePrice = getResponse().getInputResponse(0);
		int eShopType = getResponse().getDropdownResponse(1).getElementID();

		if(!QuickShopXPlugin.isPrice(ePrice))
		{
//			e.getPlayer().sendMessage("价格不是一个数字");
			e.getPlayer().sendMessage(L.get(Lang.IM_PRICE_WRONG_FORMAT));
			return;
		}
		
		shop.data.price = Float.parseFloat(ePrice);
		shop.data.type = eShopType==0 ? ShopType.BUY : ShopType.SELL;

		if(player.isOp())
		{
			shop.data.owner = getResponse().getInputResponse(2);
			shop.data.serverShop = getResponse().getToggleResponse(3);
		}

		shop.updateSignText();

		QuickShopXPlugin.instance.shopConfig.save();

		player.sendMessage(L.get(Lang.IM_SHOP_INFO_UPDATED));
	}

}
