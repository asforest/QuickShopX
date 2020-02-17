package cn.innc11.quickshopx.config;

import cn.innc11.quickshopx.Quickshopx;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class EnchantmentNamesConfig  extends BaseConfig
{
    HashMap<Integer, String> enchantmentNameMap = new HashMap<>();

    public EnchantmentNamesConfig(File file) throws FileNotFoundException
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

        enchantmentNameMap.clear();

        Object configMap = config.get("mapping");

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

        Quickshopx.logger.info(TextFormat.colorize("Loaded &6"+enchantmentNameMap.size()+"&r enchantment names"));
    }

    public String getEnchantmentName(int enchantmentId)
    {
        if(enchantmentNameMap.containsKey(enchantmentId))
        {
            return enchantmentNameMap.get(enchantmentId);
        }

        return "UnknownEnchantment";
    }

}
