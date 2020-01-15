package cn.innc11.QuickShopX.config;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.pluginEvent.PlayerRemoveShopEvent;
import cn.innc11.QuickShopX.shop.Shop;
import cn.innc11.QuickShopX.shop.ShopData;
import cn.innc11.QuickShopX.shop.ShopType;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.scheduler.PluginTask;

public class ShopConfig extends MyConfig
{
	public HashMap<String, ShopData> shopDataHashMap = new HashMap<String, ShopData>();
	
	private boolean modified = false;
	private boolean saving = false;
	
	private PluginTask<QuickShopXPlugin> saveTask;
	
	
	public Shop addShop(ShopData shop)
	{
		String key = String.format("%d:%d:%d:%s", shop.chestX, shop.chestY, shop.chestZ, shop.world);
		shopDataHashMap.put(key, shop);
		
		save();
		
		return Shop.getShopInstance(key);
	}
	
	public boolean destoryShop(ShopData shopData, Player player)
	{
		PlayerRemoveShopEvent event = new PlayerRemoveShopEvent(player, shopData.getShop());
		QuickShopXPlugin.instance.getServer().getPluginManager().callEvent(event);

		if(!event.isCancelled())
		{
			String key = String.format("%d:%d:%d:%s", shopData.chestX, shopData.chestY, shopData.chestZ, shopData.world);

//		    Shop.getShopInstance(key).destoryShop();

			shopDataHashMap.remove(key);

			save();

			return true;
		}

		return false;
	}

	public void removeShop(ShopData shopData)
	{
		String key = String.format("%d:%d:%d:%s", shopData.chestX, shopData.chestY, shopData.chestZ, shopData.world);

//		    Shop.getShopInstance(key).destoryShop();

		shopDataHashMap.remove(key);
		save();
	}

	@Override
	public void save()
	{
		if(!saving)
		{
			modified = true;
			saving = true;
			QuickShopXPlugin.instance.getServer().getScheduler().scheduleTask(QuickShopXPlugin.instance, saveTask, true);
		} else {
			modified = true;
		}
		
		
	}
	
	@Override
	public void reload()
	{
		config.reload();
		
		shopDataHashMap.clear();
		for(String key : config.getKeys(false))
		{
			if(key.equals("version")) continue;

			ShopData shopData = new ShopData();
			shopData.owner = config.getString(key+".owner");
			shopData.type = ShopType.valueOf(config.getString(key+".shopType"));
			shopData.price = (float) config.getDouble(key+".price");
			shopData.chestX = config.getInt(key+".chestX");
			shopData.chestY = config.getInt(key+".chestY");
			shopData.chestZ = config.getInt(key+".chestZ");
			shopData.signX = config.getInt(key+".signX");
			shopData.signZ = config.getInt(key+".signZ");
			shopData.world = config.getString(key+".world");

			String prefix = key+".item";

			String nbtTagText = config.getString(prefix + ".nbtTag");
			List<String> loreText = config.getStringList(prefix + ".lore");

			int id = config.getInt(prefix + ".itemId");
			int metadata = config.getInt(prefix + ".metadata");
			byte[] nbtTag = (nbtTagText != null) && (!nbtTagText.isEmpty()) ? Base64.getDecoder().decode(nbtTagText) : null;
			//String customName = config.getString(prefix + ".customName");
			//String[] lore = (loreText != null) && (!loreText.isEmpty()) ? (String[])loreText.toArray(new String[0]) : null;

			shopData.item =  Item.get(id, metadata);
			Item item = shopData.item;

			if ((nbtTag != null) && (nbtTag.length > 0))
			{
				item.setCompoundTag(nbtTag);
			}

			/*   included in NBT
			if ((customName != null) && (!customName.isEmpty()))
			{
				item.setCustomName(customName);
			}
			if ((lore != null) && (lore.length > 0))
			{
				item.setLore(lore);
			}

			for (String enchantKey : config.getSection(prefix + ".enchantment").getKeys(false))
			{
				int enchantmentId = Integer.parseInt(enchantKey.substring(1));
				int enchantmentLevel = config.getInt(prefix + ".enchantment." + enchantKey);

				Enchantment enchantment = Enchantment.getEnchantment(enchantmentId);
				enchantment.setLevel(enchantmentLevel);
				item.addEnchantment(new Enchantment[] { enchantment });
			}
			 */

			//QuickShopXPlugin.instance.getLogger().error(shopData.item.toString());

			//shopData.itemID = config.getInt(key+".itemID");
			//shopData.itemMetadata = config.getInt(key+".itemMeta");
			shopData.serverShop = config.getBoolean(key+".serverShop");
			
			shopDataHashMap.put(key, shopData);
		}
		
		QuickShopXPlugin.instance.getLogger().info("Loaded "+config.getKeys(false).size()+" shops");
	}
	
	public void SAVE()
	{
		int version = config.get("version", -1);
		config.getRootSection().clear();
		config.set("version", version);
		
		for(String key : shopDataHashMap.keySet())
		{
			ShopData shopData = shopDataHashMap.get(key);
			
			config.set(key+".owner", shopData.owner);
			config.set(key+".shopType", shopData.type.name());
			config.set(key+".price", shopData.price);
			config.set(key+".chestX", shopData.chestX);
			config.set(key+".chestY", shopData.chestY);
			config.set(key+".chestZ", shopData.chestZ);
			config.set(key+".signX", shopData.signX);
			config.set(key+".signZ", shopData.signZ);
			config.set(key+".world", shopData.world);

			String prefix = key+".item";
			Item item = shopData.item;

			int id = item.getId();
			int metadata = item.getDamage();
			String[] lore = item.getLore();
			String customName = item.getCustomName();
			Enchantment[] enchantments = item.getEnchantments();
			String nbtTag = item.hasCompoundTag() ? Base64.getEncoder().encodeToString(item.getCompoundTag()) : null;

			config.set(prefix + ".itemId", id);
			config.set(prefix + ".metadata", metadata);

			if ((item.hasCustomName()) && (!customName.isEmpty()))
			{
				config.set(prefix + ".customName", customName);
			}


			if ((lore != null) && (lore.length > 0))
			{
				config.set(prefix + ".lore", lore);
			}

			if ((nbtTag != null) && (!nbtTag.isEmpty()))
			{
				config.set(prefix + ".nbtTag", nbtTag);
			}

			for (Enchantment enchant : enchantments) {
				config.set(prefix + ".enchantment.E" + enchant.getId(), enchant.getLevel());
			}

			//config.set(key+".itemID", shopData.itemID);
			//config.set(key+".itemMeta", shopData.itemMetadata);
			config.set(key+".serverShop", shopData.serverShop);
			config.set(key+".common", QuickShopXPlugin.instance.itemNameConfig.getItemName(Item.get(shopData.item.getId(), shopData.item.getDamage())));
		}
		
		config.save();
	}
	
	public ShopConfig() 
	{
		super("shops.yml");
		
		reload();
		
		saveTask = new PluginTask<QuickShopXPlugin>(QuickShopXPlugin.instance)
		{
			@Override
			public void onRun(int currentTicks) 
			{
				while(modified)
				{
					modified = false;
					SAVE();
				}
				
				saving = false;
			}
		};
	}
	
	
}
