package cn.innc11.QuickShopX.pluginEvent;

import cn.innc11.QuickShopX.shop.Shop;
import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerCreateShopEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers()
    {
        return handlers;
    }

    private Shop shop;

    public PlayerCreateShopEvent(Player player, Shop shop)
    {
        this.player = player;
        this.shop = shop;
    }

    public Shop getShop()
    {
        return shop;
    }
}
