package cn.innc11.quickshopx.form;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.shop.BuyShop;
import cn.innc11.quickshopx.shop.SellShop;
import cn.innc11.quickshopx.shop.Shop;
import cn.innc11.quickshopx.utils.InvItem;
import cn.innc11.quickshopx.utils.L;
import cn.innc11.quickshopx.utils.Lang;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.inventory.PlayerInventory;
import me.onebone.economyapi.EconomyAPI;

public class TradingPanel extends FormWindowCustom implements FormResponse
{
	String shopKey;
	String playerName;
	
	public TradingPanel(Shop shop, String playerName) 
	{
		super(L.get(Lang.trading_title, "{OWNER}", (shop.shopData.serverShop? L.get(Lang.server_shop_nickname):shop.shopData.owner)));
		
		this.shopKey = shop.parseShopKey();
		this.playerName = playerName;
		
		addElement(new ElementLabel(L.get(Lang.trading_shop_info,
				"{GOODS}", Quickshopx.ins.itemNamesConfig.getItemName(shop.getItem()),
				"{PRICE}", shop.getStringPrice(),
				"{TYPE}", shop.shopData.type.toString(),
				"{STOCK}", Quickshopx.ins.itemNamesConfig.getItemName(shop.getItem()),
				"{ENCHANTMENTS}", L.getEnchantments(shop.getItem())
				)));

		PlayerInventory playerInv = Server.getInstance().getPlayerExact(playerName).getInventory();
		float playerMoney = (float) EconomyAPI.getInstance().myMoney(playerName);

		int m = shop.getMaxTransactionVolume(playerMoney, InvItem.getItemInInventoryCount(playerInv, shop.getItem()));
		
		addElement(new ElementSlider(L.get(Lang.trading_trading_volume, "{TRADING_VOLUME}", String.valueOf(m)), 0, m, 1, 0));
	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		int tv = (int) getResponse().getSliderResponse(1);
		
		if(tv!=0)
		{
			Shop shop = Shop.getShopByKey(shopKey);
			Player player = Quickshopx.ins.getServer().getPlayerExact(playerName);
			
			if(shop instanceof BuyShop)
			{
				((BuyShop) shop).buyItem(player, tv);
			}else if(shop instanceof SellShop)
			{
				((SellShop) shop).sellItme(player, tv);
			}
		}
	}

	@Override
	public void onFormClose(PlayerFormRespondedEvent e)
	{

	}

}
