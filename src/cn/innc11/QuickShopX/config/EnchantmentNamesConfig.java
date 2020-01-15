package cn.innc11.QuickShopX.config;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.nukkit.item.Item;

import java.util.HashMap;

public class EnchantmentNamesConfig  extends MyConfig
{
    HashMap<Integer, String> enchantmentNameMap = new HashMap<>();

    public EnchantmentNamesConfig()
    {
        super("enchantmentNames.yml");

        reload();
    }

    @Override
    public void save()
    {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void reload()
    {
        config.reload();

        enchantmentNameMap.clear();

        Object configMap = config.get("enchantmentNameMap");

        if(configMap!=null)
        {
            HashMap<String, String> cmap = (HashMap<String, String>) configMap;

            for(String key : cmap.keySet())
            {
                int enchantmentId = -1;

                try {
                    enchantmentId = Integer.parseInt(key.substring(1));
                }catch (NumberFormatException e) {e.printStackTrace(); continue;}

                if(enchantmentId>=0)
                {
                    enchantmentNameMap.put(enchantmentId, ((HashMap<String, String>) configMap).get(key));
                }

            }
        }

        QuickShopXPlugin.instance.getLogger().info("Loaded "+enchantmentNameMap.size()+" enchantment names");
    }

    public String getEnchantmentName(int enchantmentId)
    {
        if(enchantmentNameMap.containsKey(enchantmentId))
        {
            return enchantmentNameMap.get(enchantmentId);
        }

        return "Unknow";
    }

}
