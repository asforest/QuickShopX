package cn.innc11.quickshopx.listener;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.shop.Shop;
import cn.innc11.quickshopx.utils.L;
import cn.innc11.quickshopx.utils.Lang;
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
			Shop shop = Shop.getShopInstance(chestInventory.getHolder());

			if(shop!=null)
			{
				shop.updateSignText(5);
				Quickshopx.ins.hologramListener.addShopItemEntity(Server.getInstance().getOnlinePlayers().values(), shop.shopData);/////////////////
			}
		}

		if(targetInventory instanceof ChestInventory && e.getAction() == InventoryMoveItemEvent.Action.SLOT_CHANGE)
		{
			ChestInventory chestInventory = (ChestInventory) targetInventory;
			Shop shop = Shop.getShopInstance(chestInventory.getHolder());

			if(shop!=null)
			{
				shop.updateSignText(5);
				Quickshopx.ins.hologramListener.addShopItemEntity(Server.getInstance().getOnlinePlayers().values(), shop.shopData);//////////////
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
			Shop shop = Shop.getShopInstance(chestInventory.getHolder());
			boolean residenceEnable = Quickshopx.ins.residencePluginLoaded && Quickshopx.ins.pluginConfig.linkWithResidencePlugin;

			if(shop==null)
				return;

			boolean allow = true;

			if(residenceEnable)
			{
				ClaimedResidence claimedResidence = Residence.getResidenceManager().getByLoc(chestInventory.getHolder());
				if(claimedResidence!=null)
				{
					boolean hp = claimedResidence.getPermissions().playerHas(player.getName(), "build", false);

					if(hp || (player.isOp() && Quickshopx.ins.pluginConfig.ignoreOpBuildPermission))
					{
						allow = true;
					}else {
						allow = false;
					}
				}

				if(claimedResidence!=null)
				{
					if(!player.getName().equals(shop.shopData.owner) && !allow)
					{
						e.setCancelled();
						player.sendMessage(L.get(Lang.im_no_residence_permission, "{PERMISSION}", "build"));
					}
				}else {
					if(!player.getName().equals(shop.shopData.owner))
					{
						e.setCancelled();
						player.sendMessage(L.get(Lang.im_not_allow_others_open_chest));
					}
				}
			}else{
				if(!player.getName().equals(shop.shopData.owner))
				{
					e.setCancelled();
					player.sendMessage(L.get(Lang.im_not_allow_others_open_chest));
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
				Shop shop = Shop.getShopInstance(ci.getHolder());
				if(shop!=null)
				{
					shop.updateSignText(5);
					Quickshopx.ins.hologramListener.addShopItemEntity(Server.getInstance().getOnlinePlayers().values(), shop.shopData);/////////////////
				}
			}
		}
	}
}
