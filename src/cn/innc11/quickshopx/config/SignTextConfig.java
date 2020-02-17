package cn.innc11.quickshopx.config;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.shop.Shop;
import cn.innc11.quickshopx.shop.ShopType;
import cn.innc11.quickshopx.utils.L;
import cn.innc11.quickshopx.utils.Lang;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.io.FileNotFoundException;

public class SignTextConfig extends BaseConfig
{
	public String buyText1;
	public String buyText2;
	public String buyText3;
	public String buyText4;
	public String buyStockText;
	
	public String sellText1;
	public String sellText2;
	public String sellText3;
	public String sellText4;
	public String sellStockText;
	
	public SignTextConfig(File file) throws FileNotFoundException
	{
		super(checkFile(file), true, false);

		reload();
	}

	@Override
	public void Save()
	{

	}

	@Override
	public void reload() 
	{
		config.reload();
		
		buyText1 = config.getString("buy.text1");
		buyText2 = config.getString("buy.text2");
		buyText3 = config.getString("buy.text3");
		buyText4 = config.getString("buy.text4");
		buyStockText = config.getString("buy.stock");

		sellText1 = config.getString("sell.text1");
		sellText2 = config.getString("sell.text2");
		sellText3 = config.getString("sell.text3");
		sellText4 = config.getString("sell.text4");
		sellStockText = config.getString("sell.stock");
	}
	
	public String getStockText(Shop shop)
	{
		String st = (shop.shopData.type==ShopType.BUY ? buyStockText : sellStockText);

		st = st.replaceAll("\\{STOCK_SPACE\\}", String.valueOf(shop.getStock()));

		return st;
	}
	
	private String variableReplace(String str, Shop shop)
	{
		ItemNamesConfig inc = Quickshopx.ins.itemNamesConfig;
		
		str = str.replaceAll("\\{ITEM_NAME\\}", inc.getItemName(shop.getItem()));
		str = str.replaceAll("\\{PRICE\\}", shop.getStringPrice());
		str = str.replaceAll("\\{STOCK\\}", shop.shopData.serverShop ? "" : getStockText(shop));
		str = str.replaceAll("\\{OWNER\\}", shop.shopData.serverShop? L.get(Lang.server_shop_nickname):shop.shopData.owner);
		str = str.replaceAll("\\{DAMAGE\\}", String.valueOf(shop.shopData.item.getDamage()));
		
		return str;
	}

	public String[] getLang(Shop shop)
	{
		String[] text = new String[4];
		boolean isBuy = shop.shopData.type==ShopType.BUY;
		
		text[0] = TextFormat.colorize(variableReplace((isBuy ? buyText1 : sellText1), shop));
		text[1] = TextFormat.colorize(variableReplace((isBuy ? buyText2 : sellText2), shop));
		text[2] = TextFormat.colorize(variableReplace((isBuy ? buyText3 : sellText3), shop));
		text[3] = TextFormat.colorize(variableReplace((isBuy ? buyText4 : sellText4), shop));
		
		return text;
	}
}
