package cn.innc11.QuickShopX.crossVersion;

import cn.innc11.QuickShopX.QuickShopXPlugin;

public class Analyst
{
    static public void check()
    {
        if(ShopsV0.matching(QuickShopXPlugin.instance.shopConfig))
        {
            ShopsV0.convert(QuickShopXPlugin.instance.shopConfig);
        }
    }
}
