package cn.innc11.quickshopx.crossVersion;

import cn.innc11.quickshopx.Quickshopx;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;

public class Shops_v0
{
    public static void convert()
    {
        File file = new File(Quickshopx.ins.getDataFolder(), "shops.yml");

        if(file.exists())
        {
            Config config = new Config(file, Config.YAML);

            if(!config.exists("version"))
            {
                int i = 0;
                for(String key : config.getKeys(false))
                {
                    int id = config.getInt(key+".itemID");
                    int metadata = config.getInt(key+".itemMeta");

                    config.set(key+".itemID", "");
                    config.set(key+".itemMeta", "");

                    //config.remove(key+".itemID");
                    //config.remove(key+".itemMeta");

                    config.set(key+".item.itemId", id);
                    config.set(key+".item.metadata", metadata);

                    i++;
                }

                config.set("version", 0);
                config.save();

                Quickshopx.logger.info(TextFormat.colorize(String.format("&aShopData(%s) conversion completed: (%d)", Shops_v1.class.getSimpleName(), i)));
            }
        }

    }

}
