package cn.innc11.QuickShopX.config;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.shop.Shop;
import cn.innc11.QuickShopX.shop.ShopType;
import cn.innc11.QuickShopX.utils.L;
import cn.nukkit.utils.TextFormat;

public class SignTextConfig extends MyConfig
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
	
	public SignTextConfig()
	{
		super("signText.yml");
		
		reload();
	}

	@Override
	public void save() 
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
		String st = (shop.data.type==ShopType.BUY ? buyStockText : sellStockText);
		
		if(shop.data.type==ShopType.BUY)
		{
			st = st.replaceAll("\\$\\{STOCK_SPACE\\}", String.valueOf(shop.getStock()));
		} else {
			st = st.replaceAll("\\$\\{STOCK_SPACE\\}", String.valueOf(shop.getStock()));
		}
		
		return st;
	}
	
	private String variableReplace(String str, Shop shop)
	{
		ItemNameConfig inc = QuickShopXPlugin.instance.itemNameConfig;
		
		str = str.replaceAll("\\$\\{ITEM_NAME\\}", inc.getItemName(shop.getItem()));
		str = str.replaceAll("\\$\\{PRICE\\}", shop.getStringPrice());
		str = str.replaceAll("\\$\\{STOCK\\}", shop.data.serverShop ? "" : getStockText(shop));
		str = str.replaceAll("\\$\\{OWNER\\}", shop.data.serverShop? L.get(LangConfig.Lang.SERVER_SHOP_NICKNAME):shop.data.owner);
		str = str.replaceAll("\\$\\{DAMAGE\\}", String.valueOf(shop.data.itemMetadata));
		
		return str;
	}

	public String[] getLang(Shop shop)
	{
		String[] text = new String[4];
		
		text[0] = TextFormat.colorize(variableReplace((shop.data.type==ShopType.BUY ? buyText1 : sellText1), shop));
		text[1] = TextFormat.colorize(variableReplace((shop.data.type==ShopType.BUY ? buyText2 : sellText2), shop));
		text[2] = TextFormat.colorize(variableReplace((shop.data.type==ShopType.BUY ? buyText3 : sellText3), shop));
		text[3] = TextFormat.colorize(variableReplace((shop.data.type==ShopType.BUY ? buyText4 : sellText4), shop));
		
		return text;
	}
}
