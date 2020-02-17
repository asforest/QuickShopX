package cn.innc11.quickshopx.config;

import cn.innc11.quickshopx.shop.Shop;
import cn.innc11.quickshopx.shop.ShopData;

import java.io.File;

public class MultiShopsConfig extends BaseMultiConfig
{
	public MultiShopsConfig(File dir)
	{
		super(dir, ShopsConfig.class);
		reloadAllShops();
	}

	@Override
	protected String getFilenameFilterRegex(String suffix)
	{
		return String.format("^\\w+\\%s$", suffix);
	}

	public ShopsConfig getShopsConfig(Shop shop, boolean create)
	{
		return getShopsConfig(shop.shopData.world, create);
	}

	public ShopsConfig getShopsConfig(ShopData shopData, boolean create)
	{
		return getShopsConfig(shopData.world, create);
	}

	public ShopsConfig getShopsConfig(String world, boolean create)
	{
		return (ShopsConfig) getBaseConfigByFileName(world, create);
	}

	public void reloadAllShops()
	{
		reloadAll();
	}

	public ShopsConfig[] getAllShops()
	{
		return contents.values().toArray(new ShopsConfig[0]);
	}

}
