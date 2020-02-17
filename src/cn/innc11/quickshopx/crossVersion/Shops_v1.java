package cn.innc11.quickshopx.crossVersion;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.config.ShopsConfig;
import cn.innc11.quickshopx.shop.ShopData;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.util.*;

public class Shops_v1
{
	public static void convert()
	{
		File shopsFile = new File(Quickshopx.ins.getDataFolder(), "shops.yml");

		if(shopsFile.exists())
		{
			Config config = new Config(shopsFile, Config.YAML);

			int ver = config.getInt("version");

			if(ver==0 || ver==1)
			{
				File shopsDir = new File(Quickshopx.ins.getDataFolder(), "shops");
				shopsDir.mkdirs();

				ShopsConfig sConfig = new ShopsConfig(shopsFile);
				HashSet<String> worlds = new HashSet<>();

				for (ShopData shopData : sConfig.shopDataMapping.values())
				{
					worlds.add(shopData.world);
				}

				int count = 0;
				for(String world : worlds)
				{
					ShopsConfig sc = new ShopsConfig(new File(shopsDir, world+".yml"), true);
					for (ShopData shopData : sConfig.shopDataMapping.values())
					{
						if(shopData.world.equals(world))
						{
							sc.addOrUpdateShop(shopData);
							count++;
						}
					}
				}

				shopsFile.delete();

				Quickshopx.logger.info(TextFormat.colorize(String.format("&aShopData(%s) conversion completed: (%d)", Shops_v1.class.getSimpleName(), count)));
			}
		}


	}



}
