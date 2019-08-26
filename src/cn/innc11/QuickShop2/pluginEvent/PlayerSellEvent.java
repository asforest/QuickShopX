package cn.innc11.QuickShop2.pluginEvent;

import cn.innc11.QuickShop2.shop.Shop;
import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

public class PlayerSellEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers()
    {
        return handlers;
    }

    private Shop shop;
    private int count;

    public PlayerSellEvent(Player player, Shop shop, int count)
    {
        this.player = player;
        this.shop = shop;
        this.count = count;
    }

    public Shop getShop()
    {
        return shop;
    }

    public Item getShopItem() {
        return shop.getItem();
    }

    public int getTradingVolume()
    {
        return count;
    }

    public float getTransactionAmount()
    {
        return count * shop.getShopData().price;
    }

    public String getShopOwner()
    {
        return shop.data.owner;
    }
}
