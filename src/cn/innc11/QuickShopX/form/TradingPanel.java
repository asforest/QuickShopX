package cn.innc11.QuickShopX.form;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.config.LangConfig.Lang;
import cn.innc11.QuickShopX.shop.BuyShop;
import cn.innc11.QuickShopX.shop.SellShop;
import cn.innc11.QuickShopX.shop.Shop;
import cn.innc11.QuickShopX.utils.InvItem;
import cn.innc11.QuickShopX.utils.L;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.inventory.PlayerInventory;
import me.onebone.economyapi.EconomyAPI;

public class TradingPanel extends FormWindowCustom implements FormRespone 
{
	String shopKey;
	String playerName;
	
	public TradingPanel(Shop shop, String playerName) 
	{
//		super(shop.data.owner+"的商店交易界面");
		super(L.get(Lang.FORM_TRADING__TITLE, "{OWNER}", shop.data.owner));
		
		this.shopKey = shop.getShopKey();
		this.playerName = playerName;
		
		addElement(new ElementLabel(L.get(Lang.FORM_TRADING__SHOP_INFO, 
				"{GOODS_NAME}", QuickShopXPlugin.instance.itemNameConfig.getItemName(shop.getItem()),
				"{UNIT_PRICE}", shop.getStringPrice(), "{SHOP_TYPE}", shop.data.type.toString(),
				"{STOCK}", QuickShopXPlugin.instance.itemNameConfig.getItemName(shop.getItem())
				)));
//		addElement(new ElementLabel("商品: "+Main.instance.itemNameConfig.getItemName(shop.getItem())));
		
//		addElement(new ElementLabel("价格: "+shop.getFormatPrice()));
		
//		addElement(new ElementLabel("类型: "+shop.data.type));

//		addElement(new ElementLabel(TextFormat.colorize(Main.instance.signTextConfig.getStockText(shop))));

		PlayerInventory playerInv = Server.getInstance().getPlayerExact(playerName).getInventory();
		float playerMoney = (float) EconomyAPI.getInstance().myMoney(playerName);

		int m = shop.getMaxTranscationVolume(playerMoney, InvItem.getItemInInventoryCount(playerInv, shop.getItem()));
		
//		addElement(new ElementSlider("交易量 "+m, 0, m, 1, 0));
		addElement(new ElementSlider(L.get(Lang.FORM_TRADING__TRADING_VOLUME, "{TRADING_VOLUME}", String.valueOf(m)), 0, m, 1, 0));
	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		int tv = (int) getResponse().getSliderResponse(1);
		
		if(tv!=0)
		{
			Shop shop = Shop.getShopInstance(shopKey);
			Player player = QuickShopXPlugin.instance.getServer().getPlayerExact(playerName);
			
			if(shop instanceof BuyShop)
			{
				((BuyShop) shop).buyItem(player, tv);
			}else if(shop instanceof SellShop)
			{
				((SellShop) shop).sellItme(player, tv);
			}
		}
	}

}
