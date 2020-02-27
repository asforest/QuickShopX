package cn.innc11.quickshopx.shop;

import cn.innc11.quickshopx.config.ShopsConfig;
import cn.innc11.quickshopx.utils.Lang;
import cn.nukkit.utils.Faceable;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.utils.InvItem;
import cn.innc11.quickshopx.utils.L;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.block.BlockWallSign;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.scheduler.PluginTask;

import java.util.Random;

public abstract class Shop 
{
	public final ShopData shopData;

	/*
	 chest x, y, z, world
	    "233:666:233:world"
	 */
	protected Shop(String shopKey)
	{
		shopData = Quickshopx.ins.multiShopsConfig.getShopsConfig(shopKeyToWorld(shopKey), false).getShopData(shopKey);
		
		if(shopData ==null) throw new NullPointerException("Key of Shop is Null("+shopKey+")");
	}

	public ShopData getShopData()
	{
		return shopData;
	}

	public Level getLevel()
	{
		return Quickshopx.ins.getServer().getLevelByName(shopData.world);
	}

	public Position getChestPosition()
	{
		return new Position(shopData.chestX, shopData.chestY, shopData.chestZ, getLevel());
	}

	public Position getSignPosition()
	{
		return new Position(shopData.signX, shopData.chestY, shopData.signZ, getLevel());
	}

	public BlockEntityChest getShopEntityChest()
	{
		BlockEntity entityBlock = getLevel().getBlockEntity(getChestPosition());
		
		return (entityBlock instanceof BlockEntityChest) ? (BlockEntityChest)entityBlock : null;
	}
	
	public BlockEntitySign getShopEntitySign()
	{
		BlockEntity signEntity = getLevel().getBlockEntity(getSignPosition());
		
		return (signEntity instanceof BlockEntitySign) ? (BlockEntitySign)signEntity : null;
	}
	
	public void updateSignText()
	{
		updateSignText(5);
	}
	
	public void updateSignText(int delayTicks)
	{
		BlockChest blockChest = (BlockChest) getShopEntityChest().getBlock();
		
		BlockFace chestFace = BlockFace.SOUTH;
		
		switch (blockChest.getBlockFace()) 
		{
			case SOUTH: chestFace = BlockFace.WEST; break;
			case NORTH: chestFace = BlockFace.NORTH; break;
			case WEST: chestFace = BlockFace.EAST; break;
			case EAST: chestFace = BlockFace.SOUTH; break;
			default: break;
		}
		
		Position signPos = blockChest.getSide(chestFace);

		Block signBlock = blockChest.level.getBlock(signPos);
		
		if(signBlock.getId()==Block.AIR)
		{
			BlockWallSign signInstance = new BlockWallSign();
			
			signInstance.level = blockChest.level;
			signInstance.place(Block.get(Block.WALL_SIGN).toItem(), signBlock, null, BlockFace.fromIndex(blockChest.getDamage()), 0d, 0d, 0d, null);
		} 

		Quickshopx.ins.getServer().getScheduler().scheduleDelayedTask(new PluginTask<Quickshopx>(Quickshopx.ins)
		{
			@Override
			public void onRun(int currentTick) 
			{
				updateSignTextNow();
			}
		}, delayTicks);
	}
	
	public void updateSignTextNow()
	{
		BlockEntitySign entitySign = getShopEntitySign();
		
		String[] bt = Quickshopx.ins.signTextConfig.getLang(this);
		
		entitySign.setText(bt[0], bt[1], bt[2], bt[3]);
	}
	
	
	@Override
	public boolean equals(Object obj) 
	{
		boolean ret = false;
		
		if(obj instanceof Shop)
		{
			ret = ((Shop) obj).shopData.equals(shopData);
		}

		return ret;
	}
	
	
	public Item getItem()
	{
		return shopData.getItem();
	}

	public String getStringPrice()
	{
		return String.valueOf(shopData.price); /*String.format("%.2f", data.price)*/
	}

	public int getStock()
	{
		Item item = getItem();

		if(shopData.type==ShopType.BUY)
		{
			return InvItem.getItemInInventoryCount(getShopEntityChest().getInventory(), item);
		} else {
			return getShopEntityChest().getInventory().getFreeSpace(item);
		}
	}

	public void destroyShopSign()
	{
		Block signBlock = getShopEntitySign().getBlock();

		if(signBlock.getId()==Block.WALL_SIGN)
		{
			signBlock.level.setBlock(signBlock, Block.get(Block.AIR));
		}
	}

	@Deprecated
	public void destroyShop(Player player)
	{
		if (Quickshopx.ins.multiShopsConfig.getShopsConfig(shopData, false).destroyShop(shopData, player))
		{
			destroyShopSign();
		}
	}

	@Deprecated
	public void removeShop()
	{
		destroyShopSign();
		Quickshopx.ins.multiShopsConfig.getShopsConfig(shopData, false).removeShop(shopData);
	}

	public String parseShopKey()
	{
		return String.format("%d:%d:%d:%s", shopData.chestX, shopData.chestY, shopData.chestZ, shopData.world);
	}
	
	public abstract int getMaxTransactionVolume(float playerMoney, int playerItemCount);


	// static mothed
	
	public static Shop placeShop(BlockChest chest, float price, Player player)
	{
		Shop SHOP = null;
		
		BlockChest chestBlock = chest;
		
		BlockFace chestFace = BlockFace.SOUTH;
		
		switch (chestBlock.getBlockFace()) 
		{
			case SOUTH: chestFace = BlockFace.WEST; break;
			case NORTH: chestFace = BlockFace.NORTH; break;
			case WEST: chestFace = BlockFace.EAST; break;
			case EAST: chestFace = BlockFace.SOUTH; break;
			default: break;
		}
		
		Position signPos = chestBlock.getSide(chestFace);
		Block signBlock = chestBlock.level.getBlock(signPos);
		
		if(signBlock.getId()==Block.AIR)
		{
			boolean allow = true;
			
			if(Quickshopx.ins.residencePluginLoaded)
			{
				ClaimedResidence chestRes = Residence.getResidenceManager().getByLoc(chest);
				ClaimedResidence signRes = Residence.getResidenceManager().getByLoc(signPos);
				
				if(chestRes!=null)
				{
					if(signRes!=null)
					{
						if(!signRes.getName().equals(chestRes.getName()))
						{
							// the chest and the sign must be in a same residence
							player.sendMessage(L.get(Lang.im_not_allow_cross_residence));
							allow = false;
						}
					} else {
						// the sign not in a residence
						player.sendMessage(L.get(Lang.im_sign_not_in_residence));
						allow = false;
					}
				} else {
					if(signRes!=null)
					{
						// the sign is not allowed in a residence
						player.sendMessage(L.get(Lang.im_not_allow_sign_in_another_residence));
						allow = false;
					}
				}
			}
			
			if(allow)
			{
				Item itemInHand = player.getInventory().getItemInHand();

				if(itemInHand.getId()!=Item.AIR)
				{
					BlockWallSign signInstance = new BlockWallSign();

					signInstance.level = chestBlock.level;
					signInstance.place(Block.get(Block.WALL_SIGN).toItem(), signBlock, null, BlockFace.fromIndex(chestBlock.getDamage()), 0d, 0d, 0d, player);

					ShopData sd = new ShopData();

					sd.owner = player.getName();
					sd.type = ShopType.BUY;
					sd.price = price;
					sd.chestX = (int) chestBlock.x;
					sd.chestY = (int) chestBlock.y;
					sd.chestZ = (int) chestBlock.z;
					sd.signX = (int) signBlock.x;
					sd.signZ = (int) signBlock.z;
					sd.world = chestBlock.level.getName();
					sd.item = itemInHand.clone();
					sd.item.setCount(1);
					sd.serverShop = false;
					sd.shopRandomId = generateRandomShopId();

					ShopsConfig shopsConfig = Quickshopx.ins.multiShopsConfig.getShopsConfig(sd, true);
					shopsConfig.addOrUpdateShop(sd);

					SHOP = sd.getShop();
					SHOP.updateSignText();

					player.sendMessage(L.get(Lang.im_successfully_created_shop));
				} else {
					player.sendMessage(L.get(Lang.im_no_item_in_hand));
				}

			}
			
		} else {
			// the sign of shop is blocked
			player.sendMessage(L.get(Lang.im_shop_sign_blocked, "{BLOCK}", Quickshopx.ins.itemNamesConfig.getItemName(signBlock.toItem())));
		}
		
		return SHOP;
	}

	protected static String shopKeyToWorld(String shopKey)
	{
		return shopKey.substring(shopKey.lastIndexOf(":")+1);
	}
	
	private static String parseShopKey(Position pos)
	{
		return String.format("%.0f:%.0f:%.0f:%s", pos.x, pos.y, pos.z, pos.level.getFolderName());
	}
	
	public static Shop getShopByRandomId(long randomId)
	{
		for (ShopsConfig sc : Quickshopx.ins.multiShopsConfig.getAllShops())
		{
			for(ShopData shopData : sc.shopDataMapping.values())
			{
				if(shopData.shopRandomId == randomId)
				{
					return shopData.getShop();
				}
			}
		}

		return null;
	}

	public static Shop getShopByKey(String shopKey)
	{
		ShopsConfig sc = Quickshopx.ins.multiShopsConfig.getShopsConfig(shopKeyToWorld(shopKey), false);
		ShopData shopData = null;

		if(sc!=null)
		{
			shopData = sc.getShopData(shopKey);

			if(shopData==null)
			{
				return null;
			}
		}else {
			return null;
		}

		if(shopData.type==ShopType.BUY)
		{
			return new BuyShop(shopKey);
		} else if(shopData.type==ShopType.SELL)
		{
			return new SellShop(shopKey);
		}

		return null;
	}

	public static Shop findShopByChestPos(Position chestPos)
	{
		return Shop.getShopByKey(Shop.parseShopKey(chestPos));
	}

	public static Shop findShopBySignPos(Position signPos)
	{
		if(!(signPos instanceof Faceable)) return null;

		Faceable sign = (Faceable) signPos.getLevelBlock();
		BlockFace chestFace = BlockFace.SOUTH;

		switch (sign.getBlockFace())
		{
			case SOUTH: chestFace = BlockFace.NORTH; break;
			case NORTH: chestFace = BlockFace.SOUTH; break;
			case WEST: chestFace = BlockFace.EAST; break;
			case EAST: chestFace = BlockFace.WEST; break;
			default: break;
		}

		Position chestPos = signPos.getSide(chestFace);
		return Shop.getShopByKey(Shop.parseShopKey(chestPos));
	}

	public static Shop findShopByChest(Block chestBlock)
	{
		if(!(chestBlock instanceof BlockChest)) return null;

		return findShopByChestPos(chestBlock);
	}
	
	public static Shop findShopBySign(Block signBlock)
	{
		if(!(signBlock instanceof BlockWallSign)) return null;

		return findShopBySignPos(signBlock);
	}


	public static long generateRandomShopId()
	{
		return generateRandomShopId(false);
	}

	public static long generateRandomShopId(boolean noCheck)
	{
		Random random = new Random();
		random.setSeed(System.currentTimeMillis() + random.nextLong());
		long randomValue;

		if(noCheck)
		{
			randomValue = random.nextLong();
		}else{
			do {
				randomValue = random.nextLong();
			} while (Shop.getShopByRandomId(randomValue) != null);
		}

		return randomValue;
	}
	
}
