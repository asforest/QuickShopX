package cn.innc11.QuickShopX.listener;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.config.LangConfig;
import cn.innc11.QuickShopX.shop.Shop;
import cn.innc11.QuickShopX.utils.L;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.ChestInventory;
import cn.nukkit.inventory.Inventory;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;

public class ItemAndInventoryListener implements Listener
{
	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent e)
	{
		Inventory sourceInventory = e.getInventory();
		Inventory targetInventory = e.getTargetInventory();

		if(sourceInventory instanceof ChestInventory && e.getAction() == InventoryMoveItemEvent.Action.SLOT_CHANGE)
		{
			ChestInventory chestInventory = (ChestInventory) sourceInventory;
			Shop shop = Shop.findShop(chestInventory.getHolder());

			if(shop!=null)
			{
				shop.updateSignText(5);
			}
		}

		if(targetInventory instanceof ChestInventory && e.getAction() == InventoryMoveItemEvent.Action.SLOT_CHANGE)
		{
			ChestInventory chestInventory = (ChestInventory) targetInventory;
			Shop shop = Shop.findShop(chestInventory.getHolder());

			if(shop!=null)
			{
				shop.updateSignText(5);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent e)
	{
		if(e.getInventory() instanceof ChestInventory)
		{
			Player player = e.getPlayer();
			ChestInventory chestInventory = (ChestInventory) e.getInventory();
			Shop shop = Shop.findShop(chestInventory.getHolder());
			boolean residenceEnable = QuickShopXPlugin.instance.residencePluginLoaded && QuickShopXPlugin.instance.pluginConfig.interactionWithResidencePlugin;

			if(shop==null)
				return;

			boolean allow = true;

			if(residenceEnable)
			{
				ClaimedResidence claimedResidence = Residence.getResidenceManager().getByLoc(chestInventory.getHolder());
				if(claimedResidence!=null)
				{
					boolean hp = claimedResidence.getPermissions().playerHas(player.getName(), "build", false);

					if(hp || (player.isOp() && QuickShopXPlugin.instance.pluginConfig.opIgnoreResidenceBuildPermission))
					{
						allow = true;
					}else {
						allow = false;
					}
				}

				if(claimedResidence!=null)
				{
					if(!player.getName().equals(shop.data.owner) && !allow)
					{
						e.setCancelled();
						player.sendMessage(L.get(LangConfig.Lang.IM_NO_RESIDENCE_PERMISSION, "{PERMISSION}", "build"));
					}
				}else {
					if(!player.getName().equals(shop.data.owner))
					{
						e.setCancelled();
						player.sendMessage(L.get(LangConfig.Lang.IM_NOT_SHOP_OWNER_CANNOT_OPEN_CHEST));
					}
				}
			}else{
				if(!player.getName().equals(shop.data.owner))
				{
					e.setCancelled();
					player.sendMessage(L.get(LangConfig.Lang.IM_NOT_SHOP_OWNER_CANNOT_OPEN_CHEST));
				}
			}

		}
	}

	@EventHandler
	public void onInventoryTransactionEvent(InventoryTransactionEvent e)
	{
		for(Inventory inv : e.getTransaction().getInventories().toArray(new Inventory[0]))
		{
			if(inv instanceof ChestInventory)
			{
				ChestInventory ci = (ChestInventory) inv;
				Shop shop = Shop.findShop(ci.getHolder());
				if(shop!=null)
					shop.updateSignText(5);
			}
		}
	}
}
