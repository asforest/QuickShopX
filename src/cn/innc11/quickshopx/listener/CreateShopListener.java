package cn.innc11.quickshopx.listener;

import java.lang.reflect.Field;
import java.util.HashMap;

import cn.innc11.quickshopx.config.ShopsConfig;
import cn.innc11.quickshopx.pluginEvent.PlayerCreateShopEvent;
import cn.innc11.quickshopx.shop.ShopData;
import cn.innc11.quickshopx.utils.Lang;
import cn.nukkit.event.EventPriority;
import cn.nukkit.permission.PermissibleBase;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.utils.Pair;
import cn.innc11.quickshopx.shop.Shop;
import cn.innc11.quickshopx.utils.L;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;

public class CreateShopListener implements Listener, ShopInteractionTimer
{
	public HashMap<String,Pair<Long,Block>> creatingShopPlayers = new HashMap<>();
	
	private void createShop(Player player, Block block)
	{
		boolean allowCreateShop = false;

		// check permission for residence plugin
		if(Quickshopx.ins.residencePluginLoaded && Quickshopx.ins.pluginConfig.linkWithResidencePlugin)
		{
			ClaimedResidence res = Residence.getResidenceManager().getByLoc(block);

			if(res!=null)
			{
				boolean hasBuildPerm = res.getPermissions().playerHas(player.getName(), "build", false);
				hasBuildPerm |= res.getOwner().equals(player.getName());

				if(player.isOp() && Quickshopx.ins.pluginConfig.ignoreOpBuildPermission)
				{
					hasBuildPerm = true;
				}

				if(hasBuildPerm)
				{
					allowCreateShop = true;
				} else {
					player.sendMessage(L.get(Lang.im_no_residence_permission, "{PERMISSION}", "build"));
				}
				
			} else {
				allowCreateShop = true;
				
				if(Quickshopx.ins.pluginConfig.createShopInResidenceOnly)
				{
					allowCreateShop = false;
					
					// create shop in a residence only
					player.sendMessage(L.get(Lang.im_create_shop_in_residence_only));
				}
			}
			
		} else {
			allowCreateShop = true;
		}


		// check for quickshopx.create.<Number>
		PermissibleBase pb = null;

		try {
			Field perm = player.getClass().getDeclaredField("perm");
			perm.setAccessible(true);
			pb = (PermissibleBase) perm.get(player);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		int countLimit = -1;

		for(String p : pb.getEffectivePermissions().keySet())
		{
			if(p.matches("quickshopx\\.create\\.\\d+"))
			{
				countLimit = Integer.parseInt(p.split("\\.")[2]);
				Quickshopx.logger.info("- "+p);
				break;
			}
		}

		if(countLimit!=-1)
		{
			int currCount = 0;

			for (ShopsConfig sc : Quickshopx.ins.multiShopsConfig.getAllShops())
			for (ShopData shopData : sc.shopDataMapping.values())
			{
				if(shopData.owner.equals(player.getName()))
					currCount++;
			}

			if(currCount>=countLimit)
			{
				player.sendMessage(L.get(Lang.im_not_allow_have_more_shop, "{MAX_COUNT}", String.valueOf(countLimit)));
				return;
			}
		}

		if(allowCreateShop)
		{
			int interval = Quickshopx.ins.pluginConfig.interactionTime;
			
			creatingShopPlayers.put(player.getName(), new Pair<Long,Block>(Long.valueOf(System.currentTimeMillis()+interval), block));
			player.sendMessage(L.get(Lang.im_creating_shop_enter_price, "{TIMEOUT}", String.format("%.1f", interval/1000f)));

		}
	}

	// for Creative Mode
	@EventHandler
	public void onPlayerBrokeBlock(BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		
		if(player.getGamemode()==1
				&& e.getBlock().getId()==Block.CHEST 
				&&player.isSneaking()
				&& (Shop.findShopByChest(e.getBlock())==null))
		{
			createShop(player, e.getBlock());
			
			e.setCancelled();
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		Block block = e.getBlock();
		
		if(e.getAction()==Action.LEFT_CLICK_BLOCK
				&& block.getId()==Block.CHEST 
				&& (Shop.findShopByChest(block)==null))
		{
			createShop(player, block);
			
			e.setCancelled();
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChatEvent(PlayerChatEvent e)
	{
		Player player = e.getPlayer();
		String message = e.getMessage();
		String playerName = player.getName();

		if(creatingShopPlayers.containsKey(playerName))
		{
			if(Quickshopx.ins.interactionShopListenerInstance.interactingShopHashMap.containsKey(player.getName()))
			{
				Quickshopx.ins.interactionShopListenerInstance.interactingShopHashMap.remove(player.getName());
			}

			if(!Quickshopx.isPrice(message))
			{
				player.sendMessage(L.get(Lang.im_not_a_number));
			}else
			if(!isVaildInteraction(playerName).key)
			{
				//player.sendMessage(L.get(Lang.im_interaction_timeout));
			}else {
				BlockChest creatingShopChest = (BlockChest) creatingShopPlayers.get(playerName).value;

				Shop createdShop = Shop.placeShop(creatingShopChest, Float.parseFloat(message), player);

				if (createdShop != null)
				{
					PlayerCreateShopEvent event = new PlayerCreateShopEvent(player, createdShop);
					Quickshopx.ins.getServer().getPluginManager().callEvent(event);

					if(!event.isCancelled())
					{
						Quickshopx.ins.hologramListener.addShopItemEntity(Server.getInstance().getOnlinePlayers().values(), createdShop.shopData);
					}

				}
			}
			
			creatingShopPlayers.remove(playerName);
			
			e.setCancelled();
		}
		
	}

	@Override
	public Pair<Boolean, Block> isVaildInteraction(String player) 
	{
		if(creatingShopPlayers.containsKey(player))
		{
			Pair<Long, Block> cs = creatingShopPlayers.get(player);
			boolean noTimeout = System.currentTimeMillis() < cs.key.longValue();
			return new Pair<>(noTimeout, cs.value);
		}
		
		return null;
	}
}
