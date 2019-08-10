package cn.innc11.QuickShop2.listener;

import java.util.HashMap;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

import cn.innc11.QuickShop2.Main;
import cn.innc11.QuickShop2.Pair;
import cn.innc11.QuickShop2.config.LangConfig.Lang;
import cn.innc11.QuickShop2.shop.Shop;
import cn.innc11.QuickShop2.utils.L;
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
	
	//@EventHandler
//	public void o(PlayerMoveEvent e)
//	{
//		e.getPlayer().sendActionBar(String.format("%d : %d : %d", (int)e.getTo().x, (int)e.getTo().y, (int)e.getTo().z));
//	}
	
	private void createShop(Player player, Block block) 
	{
		
		boolean allowCreateShop = false;
		
		if(Main.instance.residencePluginLoaded && Main.instance.pluginConfig.interactionWithResidencePlugin)
		{
			ClaimedResidence res = Residence.getResidenceManager().getByLoc(block);
			
			if(res!=null && !Main.instance.pluginConfig.opIgnoreResidence)
			{
				boolean hasBuildPerm = res.getPermissions().playerHas(player.getName(), "build", false);
				
				if(hasBuildPerm)
				{
					allowCreateShop = true;
				} else {
					player.sendMessage(L.get(Lang.IM_NO_RESIDENCE_PERMISSION, "{PERMISSION}", "build"));
				}
				
			} else {
				allowCreateShop = true;
				
				if(Main.instance.pluginConfig.createShopInResidenceOnly)
				{
					allowCreateShop = false;
					
					// create shop in a residence only
					player.sendMessage(L.get(Lang.IM_CREATE_SHOP_IN_RESIDENCE_NOLY));
				}
			}
			
		} else {
			allowCreateShop = true;
		}
		
		if(allowCreateShop)
		{
			int interval = Main.instance.pluginConfig.interactionInterval;
			
			creatingShopPlayers.put(player.getName(), new Pair<Long,Block>(Long.valueOf(System.currentTimeMillis()+interval), block));
			
			player.sendMessage(L.get(Lang.IM_CREATING_SHOP_ENTER_PRICE, "{TIMEOUT}", String.format("%.1f", interval/1000f)));
			
		}
	}
	
	@EventHandler
	public void onPlayerBrokeBlock(BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		
		if(player.getGamemode()==1
				&& e.getBlock().getId()==Block.CHEST 
				&&player.isSneaking()
				&& (Shop.findShop(e.getBlock())==null))
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
				//&& player.isSneaking() 
				&& (Shop.findShop(block)==null))
		{
			createShop(player, block);
			
			e.setCancelled();
		}
	}
	
	@EventHandler
	public void onPlayerChatEvent(PlayerChatEvent e)
	{
		Player player = e.getPlayer();
		String message = e.getMessage();
		String playerName = player.getName();
		
		if(creatingShopPlayers.containsKey(playerName))
		{
			if(!Main.isInteger(message))
			{
				// not a number
				player.sendMessage(L.get(Lang.IM_NO_ENTER_NUMBER));
			}else
			if(!isVaildInteraction(playerName).key)
			{
				// timeout
				player.sendMessage(L.get(Lang.IM_INTERACTOIN_TIMEOUT));
			}else {
				BlockChest creatingShopChest = (BlockChest) creatingShopPlayers.get(playerName).value;
				
				Shop createdShop = Shop.placeShop(creatingShopChest, Float.parseFloat(message), player);
				
				if(createdShop!=null)
					Main.instance.hologramListener.addShopItemEntity(Server.getInstance().getOnlinePlayers().values(), createdShop.data);
				
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
