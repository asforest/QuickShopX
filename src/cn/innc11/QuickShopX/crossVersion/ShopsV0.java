package cn.innc11.QuickShopX.crossVersion;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.config.MyConfig;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

public class ShopsV0
{
    static void convert(MyConfig myconfig)
    {
        Config config = myconfig.config;

        int i = 0;
        for(String key : config.getKeys(false))
        {
            int id = config.getInt(key+".itemID");
            int metadata = config.getInt(key+".itemMeta");

            config.set(key+".itemID", "");
            config.set(key+".itemMeta", "");

            config.remove(key+".itemID");
            config.remove(key+".itemMeta");

            config.set(key+".item.itemId", id);
            config.set(key+".item.metadata", metadata);

            i++;

            //QuickShopXPlugin.instance.getLogger().info("Converting(V0): "+key+" "+config.get(key+".common"));
        }

        QuickShopXPlugin.instance.getLogger().info(TextFormat.colorize("&bConverted(V0): "+i+" shop's data"));

        config.set("version", 1);

        config.save();
    }

    static boolean matching(MyConfig config)
    {
        if(!config.config.exists("version"))
        {
            return true;
        }

        return false;
    }
}
