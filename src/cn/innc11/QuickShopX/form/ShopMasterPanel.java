package cn.innc11.QuickShopX.form;

import cn.innc11.QuickShopX.config.LangConfig.Lang;
import cn.innc11.QuickShopX.shop.Shop;
import cn.innc11.QuickShopX.shop.ShopType;
import cn.innc11.QuickShopX.utils.L;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;

public class ShopMasterPanel extends FormWindowSimple implements FormRespone
{
	String shopKey;
	String playerName;
	
	public ShopMasterPanel(Shop shop, String playerName) 
	{
//		super(shop.data.owner+"的商店", "");
		super(L.get(Lang.FORM_MASTER__TITLE, "{OWNER}", shop.data.owner), "");
		
		this.shopKey = shop.getShopKey();
		this.playerName = playerName;
		
//		StringBuffer sb = new StringBuffer(100);
//		
//		sb.append("商店的价格: " + shop.getFormatPrice()+"\n\n");
//		sb.append("商店的类型: " + L.get(shop.data.type==ShopType.BUY ? Lang.BUY : Lang.SELL)+"\n\n");
//		sb.append("商店的库存: " + shop.getStock()+"\n");
//		
//		sb.append("\n\n\n\n\n");
		
//		setContent(sb.toString());
		setContent(L.get(Lang.FORM_MASTER__CONTENT, 
				"{UNIT_PRICE}", shop.getStringPrice(),
				"{SHOP_TYPE}",  L.get(shop.data.type==ShopType.BUY ? Lang.BUY : Lang.SELL),
				"{SHOP_STOCK}", String.valueOf(shop.getStock())));

//		addButton(new ElementButton("打开交易界面"));
		addButton(new ElementButton(L.get(Lang.FORM_MASTER__BUTTON_SHOP_TRADING_PANEL)));

//		addButton(new ElementButton("打开设置界面"));
		addButton(new ElementButton(L.get(Lang.FORM_MASTER__BUTTON_SHOP_DATA_PANEL)));

//		addButton(new ElementButton("移除商店"));
		//addButton(new ElementButton(L.get(Lang.FORM_MASTER__BUTTON_REMOVE_SHOP)));
	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		Shop shop = Shop.getShopInstance(shopKey);

		if(!e.getPlayer().isOp() && !e.getPlayer().getName().equals(shop.data.owner))
		{
			return;
		}

		int clickedButtonindex = getResponse().getClickedButtonId();

		switch (clickedButtonindex)
		{
			case 0:
				e.getPlayer().showFormWindow(new TradingPanel(Shop.getShopInstance(shopKey), playerName));
				break;

			case 1:
				e.getPlayer().showFormWindow(new ShopDataPanel(Shop.getShopInstance(shopKey), playerName));
				break;
/*
			case 2:
			{
				shop.destoryShop(QuickShopXPlugin.instance.getServer().getPlayerExact(playerName));
				QuickShopXPlugin.instance.hologramListener.removeItemEntity(Server.getInstance().getOnlinePlayers().values(), shop.data);
					
				QuickShopXPlugin.instance.getServer().getPlayerExact(playerName).sendMessage(L.get(Lang.IM_SUCCEESSFULLY_REMOVED_SHOP));
				break;
			}
*/

	
		}
		
		
	 }
}
