package cn.innc11.QuickShopX.utils;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.config.LangConfig.Lang;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;

public class L //language
{
	public static String get(Lang l, String... args)
	{
		return QuickShopXPlugin.instance.langConfig.get(l, args);
	}

	public static String getEnchantments(Item item)
	{
		if(!item.hasEnchantments())
		{
			return "";
		}

		Enchantment[] enchantments = item.getEnchantments();
		StringBuffer sb = new StringBuffer();

		for (int i =0; i<enchantments.length; i++)
		{
			Enchantment enchantment = enchantments[i];

					String prefix = (i==0?
					get(Lang.ENCHANTMENT__PER_PREFIX_FIRST, "{ENCHANTMENT__PER_PREFIX}", get(Lang.ENCHANTMENT__PER_PREFIX))
					:
					get(Lang.ENCHANTMENT__PER_PREFIX)
				);

			String suffix = (i==enchantments.length-1?
					get(Lang.ENCHANTMENT__PER_SUFFIX_LAST, "{ENCHANTMENT__PER_SUFFIX}", get(Lang.ENCHANTMENT__PER_SUFFIX))
					:
					get(Lang.ENCHANTMENT__PER_SUFFIX)
			);

			String ms = get(Lang.ENCHANTMENT__MULTIPLE_SIGN);

			sb.append(get(Lang.ENCHANTMENT__PER_LINE,
					"{ENCHANTMENT__PER_PREFIX}", prefix,
					"{ENCHANTMENT_NAME}", QuickShopXPlugin.instance.enchantmentNamesConfig.getEnchantmentName(enchantment.getId()),
					"{ENCHANTMENT__MULTIPLE_SIGN}", ms,
					"{LEVEL}", String.valueOf(enchantment.getLevel()),
					"{ENCHANTMENT__PER_SUFFIX}", suffix
			));

		}

		return get(Lang.ENCHANTMENT__TEXT, "{ENCHANTMENTS}", sb.toString());
	}
}
