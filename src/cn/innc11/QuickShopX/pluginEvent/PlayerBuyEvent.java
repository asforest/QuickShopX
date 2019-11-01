package cn.innc11.QuickShopX.pluginEvent;

import cn.innc11.QuickShopX.shop.BuyShop;
import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

public class PlayerBuyEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers()
    {
        return handlers;
    }

    private BuyShop shop;
    private int count;

    public PlayerBuyEvent(Player player, BuyShop shop, int count)
    {
        this.player = player;
        this.shop = shop;
        this.count = count;
    }

    public BuyShop getShop()
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
