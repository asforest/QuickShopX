package cn.innc11.QuickShopX.listener;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.utils.Pair;
import cn.innc11.QuickShopX.shop.ShopData;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.network.protocol.AddItemEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.scheduler.PluginTask;

public class HologramItemListener implements Listener
{
	ArrayBlockingQueue<Pair<Collection<Player>, DataPacket>> queue = new ArrayBlockingQueue<Pair<Collection<Player>,DataPacket>>(10000);	
	
	PluginTask<QuickShopXPlugin> sendDataPacketTask = new PluginTask<QuickShopXPlugin>(QuickShopXPlugin.instance)
	{
		@Override
		public void onRun(int currentTicks) 
		{
			int packetSendDelay = 50;
			
			while (true) 
			{
				try {
					Pair<Collection<Player>, DataPacket> x = queue.take();
					
					Server.broadcastPacket(x.key, x.value);

					packetSendDelay = (int)(1*1000 / QuickShopXPlugin.instance.pluginConfig.packetSendPerSecondMax);
					
					Thread.sleep(packetSendDelay);
					
//					Main.instance.getLogger().warning("packetSendDelay: "+packetSendDelay);
				}
				catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	};
	
	public HologramItemListener(QuickShopXPlugin main)
	{
		main.getServer().getScheduler().scheduleDelayedTask(sendDataPacketTask, 0, true);
	}
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) 
	{
		for (ShopData shopData : QuickShopXPlugin.instance.shopConfig.shopDataHashMap.values())
        {
        	if(shopData.world.equals(e.getPlayer().level.getFolderName()))
        		addShopItemEntity(Arrays.asList(e.getPlayer()), shopData);
        }
    }
	
	
	
	@EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) 
    {
		Player player = event.getPlayer();
		
		if(!event.getTo().level.getName().equals(event.getFrom().level.getName()))
		{
//			Server.getInstance().getLogger().error(event.getPlayer().getName()+"传送到了不同的世界: 从"+event.getFrom().level.getName()+" 到"+event.getTo().level.getName());
			
			for (ShopData shopData : QuickShopXPlugin.instance.shopConfig.shopDataHashMap.values())
			{
				if(shopData.world.equals(event.getTo().level.getFolderName()))
				{
//					Server.getInstance().getLogger().error("添加实体包: "+getEid(shopData));
					addShopItemEntity(Arrays.asList(player), shopData);
				}
				
				if(shopData.world.equals(event.getFrom().level.getFolderName()))
				{
//					Server.getInstance().getLogger().error("移除实体包: "+getEid(shopData));
					removeItemEntity(Arrays.asList(player), shopData);
				}
			}
		}
    }
	
	
	public void removeAllItemEntityForAllPlayer()
	{
		for (ShopData shopData : QuickShopXPlugin.instance.shopConfig.shopDataHashMap.values())
        {
			for(Player player : Server.getInstance().getOnlinePlayers().values())
			{
	        	if(shopData.world.equals(player.level.getFolderName()))
	        		removeItemEntity(Arrays.asList(player), shopData);
			}
        }
	}
	
	public void addAllItemEntityForAllPlayer()
	{
		for (ShopData shopData : QuickShopXPlugin.instance.shopConfig.shopDataHashMap.values())
		{
			for(Player player : Server.getInstance().getOnlinePlayers().values())
			{
				if(shopData.world.equals(player.level.getFolderName()))
					addShopItemEntity(Arrays.asList(player), shopData);
			}
		}
	}
	
	
	
	
	public long getEid(ShopData shopData)
	{
		long l = (long) Math.abs((shopData.signX * shopData.chestY * shopData.signZ)+Math.sqrt(shopData.signX * shopData.chestY * shopData.signZ));
//		Server.getInstance().getLogger().error("添加实体包: "+l);
		return l;
	}
	
	public long addShopItemEntity(Collection<Player> players, ShopData shopData) 
	{
		if(!QuickShopXPlugin.instance.pluginConfig.hologramItemShow) return 0L;
		
		long entityId = getEid(shopData);
		
		AddItemEntityPacket addItemEntityPacket = new AddItemEntityPacket();
		addItemEntityPacket.entityUniqueId = entityId;
		addItemEntityPacket.entityRuntimeId = addItemEntityPacket.entityUniqueId;
		addItemEntityPacket.item = shopData.getShop().getItem();
		addItemEntityPacket.x = (float) (shopData.chestX + 0.5F);
		addItemEntityPacket.y = (float) (shopData.chestY + 1);
		addItemEntityPacket.z = (float) (shopData.chestZ + 0.5F);
		addItemEntityPacket.speedX = 0f;
		addItemEntityPacket.speedY = 0f;
		addItemEntityPacket.speedZ = 0f;
		int flags = Entity.DATA_FLAG_IMMOBILE;
		addItemEntityPacket.metadata = new EntityMetadata()
				.putLong(Entity.DATA_FLAGS, flags)
				.putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
				.putFloat(Entity.DATA_SCALE, 4f);
//		Server.broadcastPacket(players, addItemEntityPacket);
		
		try {
			queue.offer(new Pair<Collection<Player>, DataPacket>(players, addItemEntityPacket), 2L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		return entityId;
	}
	
	
	public void removeItemEntity(Collection<Player> players, ShopData shopData) 
	{
		if(!QuickShopXPlugin.instance.pluginConfig.hologramItemShow) return;
		
		RemoveEntityPacket removeItemEntityPacket = new RemoveEntityPacket();
		removeItemEntityPacket.eid = getEid(shopData);
//		Server.broadcastPacket(players, removeItemEntityPacket);
		
		try {
			queue.offer(new Pair<Collection<Player>, DataPacket>(players, removeItemEntityPacket), 2L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
}
