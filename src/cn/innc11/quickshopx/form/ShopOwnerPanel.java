package cn.innc11.quickshopx.form;

import cn.innc11.quickshopx.shop.Shop;
import cn.innc11.quickshopx.shop.ShopType;
import cn.innc11.quickshopx.utils.L;
import cn.innc11.quickshopx.utils.Lang;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;

public class ShopOwnerPanel extends FormWindowSimple implements FormResponse
{
	String shopKey;
	String playerName;
	
	public ShopOwnerPanel(Shop shop, String playerName)
	{
		super(L.get(Lang.owner_title,
				"{OWNER}", (shop.shopData.serverShop? L.get(Lang.server_shop_nickname):shop.shopData.owner)), "");
		
		this.shopKey = shop.parseShopKey();
		this.playerName = playerName;

		setContent(L.get(Lang.owner_content,
				"{PRICE}", shop.getStringPrice(),
				"{TYPE}",  L.get(shop.shopData.type==ShopType.BUY ? Lang.buy : Lang.sell),
				"{STOCK}", String.valueOf(shop.getStock()),
				"{ENCHANTMENTS}", L.getEnchantments(shop.getItem())
		));

		addButton(new ElementButton(L.get(Lang.owner_button_shop_trading_panel)));

		addButton(new ElementButton(L.get(Lang.owner_button_shop_data_panel)));
	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		Shop shop = Shop.getShopByKey(shopKey);

		if(!e.getPlayer().isOp() && !e.getPlayer().getName().equals(shop.shopData.owner))
		{
			return;
		}

		int clickedButtonIndex = getResponse().getClickedButtonId();

		switch (clickedButtonIndex)
		{
			case 0:
				e.getPlayer().showFormWindow(new TradingPanel(shop, playerName));
				break;

			case 1:
				e.getPlayer().showFormWindow(new ShopDataPanel(shop, playerName));
				break;

		}
		
	 }

	@Override
	public void onFormClose(PlayerFormRespondedEvent e)
	{

	}
}
