package cn.innc11.QuickShopX.shop;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.innc11.QuickShopX.config.LangConfig.Lang;
import cn.innc11.QuickShopX.utils.InvItem;
import cn.innc11.QuickShopX.utils.L;
import cn.nukkit.Player;
import cn.nukkit.Server;
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
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.PluginTask;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;

public abstract class Shop 
{
	public final Position chestPos;
	public final Position signPos;
	public final Level world;
	public final ShopData data;
	
	
	/*
	 chest x, y, z, world
	    "233:666:233:world"
	 */
	protected Shop(String shop)
	{
		data = QuickShopXPlugin.instance.shopConfig.shopDataHashMap.get(shop);
		
		if(data==null) throw new NullPointerException("Key of Shop is Null("+shop+")");
		
		world = Server.getInstance().getLevelByName(data.world);
		
		if(world==null) throw new NullPointerException("the world is not loaded or no exists("+data.world+")");
		
		chestPos = new Position(data.chestX, data.chestY, data.chestZ, world);
		
		signPos = new Position(data.signX, data.chestY, data.signZ, world);
	}

	public ShopData getShopData()
	{
		return data;
	}

	public Level getWorld() {
		return world;
	}

	public BlockEntityChest getShopChest()
	{
		BlockEntity blockEntity = world.getBlockEntity(chestPos);
		
		return (blockEntity instanceof BlockEntityChest) ? (BlockEntityChest)blockEntity : null;
	}
	
	public BlockEntitySign getShopSign()
	{
		Vector3 vector3 = new Vector3(signPos.x, chestPos.y, signPos.z);
		
		BlockEntity signEntity = world.getBlockEntity(vector3);
		
		return (signEntity instanceof BlockEntitySign) ? (BlockEntitySign)signEntity : null;
	}
	
	public void updateSignText()
	{
		updateSignText(5);
	}
	
	public void updateSignText(int delayTicks)
	{
		BlockChest blockChest = (BlockChest) getShopChest().getBlock();
		
		BlockFace chestFace = BlockFace.SOUTH;
		
		switch (blockChest.getBlockFace()) 
		{
		case SOUTH:
			chestFace = BlockFace.WEST;
			break;
		case NORTH:
			chestFace = BlockFace.NORTH;
			break;
		case WEST:
			chestFace = BlockFace.EAST;
			break;
		case EAST:
			chestFace = BlockFace.SOUTH;
			break;
		default:
			break;
		}
		
		Position signPos = blockChest.getSide(chestFace);
		
		Block signBlock = blockChest.level.getBlock(signPos);
		
		if(signBlock.getId()==Block.AIR)
		{
			BlockWallSign signInstance = new BlockWallSign();
			
			signInstance.level = blockChest.level;
			signInstance.place(Block.get(Block.WALL_SIGN).toItem(), signBlock, null, BlockFace.fromIndex(blockChest.getDamage()), 0d, 0d, 0d, null);
		} 
		
		
		QuickShopXPlugin.instance.getServer().getScheduler().scheduleDelayedTask(new PluginTask<QuickShopXPlugin>(QuickShopXPlugin.instance)
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
		BlockEntitySign sign = getShopSign();
		
		String[] bt = QuickShopXPlugin.instance.signTextConfig.getLang(this);
		
		sign.setText(bt[0], bt[1], bt[2], bt[3]);
	}
	
	
	@Override
	public boolean equals(Object obj) 
	{
		boolean ret = false;
		
		if(obj instanceof Shop)
		{
			ret = ((Shop) obj).data.equals(data);
		}

		return ret;
	}
	
	
	public Item getItem()
	{
		Item temp = data.item.clone();
		temp.setCount(1);
		return temp;
	}

	public String getStringPrice()
	{
		return String.valueOf(data.price)/*String.format("%.2f", data.price)*/;
	}

	public int getStock()
	{
		Item item = getItem();
		return data.type==ShopType.BUY ? InvItem.getItemInInventoryCount(getShopChest().getInventory(), item) : getShopChest().getInventory().getFreeSpace(item);
	}
	
	public void destoryShopSign()
	{
		Block sign = world.getBlock(signPos);
		
		if(sign.getId()==Block.WALL_SIGN)
		{
			getShopSign().level.setBlock(signPos, Block.get(Block.AIR));
		}
		
	}
	
	public void destoryShop(Player player)
	{
		if (QuickShopXPlugin.instance.shopConfig.destoryShop(data, player))
		{
			destoryShopSign();
		}
	}

	public void removeShop()
	{
		destoryShopSign();
		QuickShopXPlugin.instance.shopConfig.removeShop(data);
	}

	public String getShopKey()
	{
		return String.format("%.0f:%.0f:%.0f:%s", chestPos.x, chestPos.y, chestPos.z, world.getFolderName());
	}
	
	public abstract int getMaxTranscationVolume(float playerMoney, int playerItemCount);
	
	//// static mothod
	
	public static Shop placeShop(BlockChest chest, float price, Player player)
	{
		Shop rt = null;
		
		BlockChest chestBlock = chest;
		
		BlockFace chestFace = BlockFace.SOUTH;
		
		switch (chestBlock.getBlockFace()) 
		{
		case SOUTH:
			chestFace = BlockFace.WEST;
			break;
		case NORTH:
			chestFace = BlockFace.NORTH;
			break;
		case WEST:
			chestFace = BlockFace.EAST;
			break;
		case EAST:
			chestFace = BlockFace.SOUTH;
			break;
		default:
			break;
		}
		
		Position signPos = chestBlock.getSide(chestFace);
		
		Block signBlock = chestBlock.level.getBlock(signPos);
		
		if(signBlock.getId()==Block.AIR)
		{
			boolean allow = true;
			
			if(QuickShopXPlugin.instance.residencePluginLoaded)
			{
				ClaimedResidence chestRes = Residence.getResidenceManager().getByLoc(chest);
				ClaimedResidence signRes = Residence.getResidenceManager().getByLoc(signPos);
				
				if(chestRes!=null)
				{
					if(signRes!=null)
					{
						if(!signRes.getName().equals(chestRes.getName()))
						{
							// chest and sign must be in a same residence
							player.sendMessage(L.get(Lang.IM_CHEST_SIGN_DIFFERENT_RESIDENCE));
							allow = false;
						}
					} else {
						//sign not in a residence range
						player.sendMessage(L.get(Lang.IM_SIGN_NOT_IN_A_RESIDENCE_RANGE));
						allow = false;
					}
				} else {
					if(signRes!=null)
					{
						//sign is not allowed in a residence
						player.sendMessage(L.get(Lang.IM_SIGN_NOT_ALLOWED_IN_A_RESIDENCE));
						allow = false;
					}
				}
			}
			
			if(allow)
			{
			
				BlockWallSign signInstance = new BlockWallSign();
				
				signInstance.level = chestBlock.level;
				signInstance.place(Block.get(Block.WALL_SIGN).toItem(), signBlock, null, BlockFace.fromIndex(chestBlock.getDamage()), 0d, 0d, 0d, player);
				
				Item itemInHand = player.getInventory().getItemInHand();
				
				if(itemInHand.getId()!=Item.AIR)
				{
					ShopData sd = new ShopData();
					
					sd.owner = player.getName();
					sd.type = ShopType.BUY;
					sd.price = price;
					sd.chestX = (int) chestBlock.x;
					sd.chestY = (int) chestBlock.y;
					sd.chestZ = (int) chestBlock.z;
					sd.signX = (int) signBlock.x;
					sd.signZ = (int) signBlock.z;
					sd.world = chestBlock.level.getFolderName();
					//sd.item = new Item(itemInHand.getId(), itemInHand.getDamage());
					sd.item = itemInHand.clone();
					sd.item.setCount(1);
					sd.serverShop = false;
					
					rt = QuickShopXPlugin.instance.shopConfig.addShop(sd);
					
					rt.updateSignText();
					
					// succeessfully created shop
					player.sendMessage(L.get(Lang.IM_SUCCEESSFULLY_CREATED_SHOP));
				} else {
					// need to held an item in hand
					player.sendMessage(L.get(Lang.IM_NO_ITEM_IN_HAND));
				}
			}
			
		} else {
			// shop sign blocked
			player.sendMessage(L.get(Lang.IM_SHOP_SIGN_BLOCKED, "{BLOCK_NAME}", QuickShopXPlugin.instance.itemNameConfig.getItemName(signBlock.toItem())));
		}
		
		return rt;
	}
	
	public static String getShopKey(Position pos)
	{
		return String.format("%.0f:%.0f:%.0f:%s", pos.x, pos.y, pos.z, pos.level.getFolderName());
	}
	
	public static Shop findShop(Position chest)
	{
		return getShopInstance(Shop.getShopKey(chest));
	}
	
	public static Shop getShopInstance(String shopKey)
	{
		ShopData shopData = QuickShopXPlugin.instance.shopConfig.shopDataHashMap.get(shopKey);
		
		if(shopData==null)
			return null;
		
		Shop retShop = null;
		
		if(shopData.type==ShopType.BUY)
		{
			retShop = new BuyShop(shopKey);
		} else if(shopData.type==ShopType.SELL)
		{
			retShop = new SellShop(shopKey);
		}
		
		return retShop;
	}
	
	public static Shop findShopBySignPos(Block sign)
	{
		if(!(sign instanceof BlockWallSign)) return null;

		BlockWallSign blockSign = (BlockWallSign) sign;
		Shop shop = null;

		BlockFace chestFace = BlockFace.SOUTH;

		/*
		QuickShopXPlugin.instance.getLogger().info("BBBBBBface: "+blockSign.getBlockFace().getName());
		QuickShopXPlugin.instance.getLogger().info("EAST: "+blockSign.getSide(BlockFace.EAST).getName());
		QuickShopXPlugin.instance.getLogger().info("WEST: "+blockSign.getSide(BlockFace.WEST).getName());
		QuickShopXPlugin.instance.getLogger().info("SOUTH: "+blockSign.getSide(BlockFace.SOUTH).getName());
		QuickShopXPlugin.instance.getLogger().info("NORTH: "+blockSign.getSide(BlockFace.NORTH).getName());
*/

		switch (blockSign.getBlockFace())
		{
			case SOUTH:
				chestFace = BlockFace.NORTH;
				break;
			case NORTH:
				chestFace = BlockFace.SOUTH;
				break;
			case WEST:
				chestFace = BlockFace.EAST;
				break;
			case EAST:
				chestFace = BlockFace.WEST;
				break;
			default:
				break;
		}

		Position chestPos = blockSign.getSide(chestFace);
		shop = Shop.getShopInstance(Shop.getShopKey(chestPos));
/*
		Block N = blockSign.getSide(BlockFace.NORTH);
		Block E = blockSign.getSide(BlockFace.EAST);
		Block S = blockSign.getSide(BlockFace.SOUTH);
		Block W = blockSign.getSide(BlockFace.WEST);
		
		if(shop==null && N instanceof BlockChest){
			Shop shopN = Shop.getShopInstance(Shop.getShopKey(N));
			if(shopN!=null)
				shop = shopN;
		}
		
		if(shop==null && E instanceof BlockChest){
			Shop shopE = Shop.getShopInstance(Shop.getShopKey(E));
			if(shopE!=null)
				shop = shopE;
		}
		
		if(shop==null && S instanceof BlockChest){
			Shop shopS = Shop.getShopInstance(Shop.getShopKey(S));
			if(shopS!=null)
				shop = shopS;
		}
		
		if(shop==null && W instanceof BlockChest){
			Shop shopW = Shop.getShopInstance(Shop.getShopKey(W));
			if(shopW!=null)
				shop = shopW;
		}

 */
		
		return shop;
	}
	
}
