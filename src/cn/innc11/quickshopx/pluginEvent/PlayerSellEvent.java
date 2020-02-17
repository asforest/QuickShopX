package cn.innc11.quickshopx.pluginEvent;

import cn.innc11.quickshopx.shop.SellShop;
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

    private SellShop shop;
    private int count;

    public PlayerSellEvent(Player player, SellShop shop, int count)
    {
        this.player = player;
        this.shop = shop;
        this.count = count;
    }

    public SellShop getShop()
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
        return shop.shopData.owner;
    }
}
