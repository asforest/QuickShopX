package cn.innc11.quickshopx.utils;

import cn.innc11.quickshopx.Quickshopx;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;

public class L //language
{
	public static String get(Lang l, String... args)
	{
		return Quickshopx.ins.langConfig.get(l, args);
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
					get(Lang.enchantment_per_prefix_first, "{PER_PREFIX}", get(Lang.enchantment_per_prefix))
					:
					get(Lang.enchantment_per_prefix)
				);

			String suffix = (i==enchantments.length-1?
					get(Lang.enchantment_per_suffix_last, "{PER_SUFFIX}", get(Lang.enchantment_per_suffix))
					:
					get(Lang.enchantment_per_suffix)
				);

			sb.append(get(Lang.enchantment_per_line,
					"{PREFIX}", prefix,
					"{ENCHANTMENT}", Quickshopx.ins.enchantmentNamesConfig.getEnchantmentName(enchantment.getId()),
					"{LEVEL}", String.valueOf(enchantment.getLevel()),
					"{SUFFIX}", suffix
				));

		}

		return get(Lang.enchantment_text, "{ENCHANTMENTS}", sb.toString());
	}
}
