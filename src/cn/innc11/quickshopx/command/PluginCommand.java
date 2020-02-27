package cn.innc11.quickshopx.command;

import java.io.FileNotFoundException;
import java.util.HashMap;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.utils.Lang;
import cn.innc11.quickshopx.utils.Pair;
import cn.innc11.quickshopx.form.PluginControlPanel;
import cn.innc11.quickshopx.shop.Shop;
import cn.innc11.quickshopx.shop.ShopType;
import cn.innc11.quickshopx.utils.L;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class PluginCommand extends Command
{

	public PluginCommand()
	{
		super("quickshopx");
		
		setDescription("QuickShopX Command");
		setAliases(new String[]{"shop", "qs", "quickshop"});
        setUsage("/qs <subcommand> [args]");
        
        HashMap<String, CommandParameter[]> parameter = new HashMap<>();
        
        parameter.put("1arg", new CommandParameter[] {new CommandParameter("help", false, new String[]{"help", "h"})});
        parameter.put("2arg", new CommandParameter[] {new CommandParameter("buy", false, new String[]{"buy", "b"})});
        parameter.put("3arg", new CommandParameter[] {new CommandParameter("sell", false, new String[]{"sell", "s"})});
        parameter.put("4arg", new CommandParameter[] {new CommandParameter("price", false, new String[]{"price", "p"}), new CommandParameter("price", CommandParamType.FLOAT, false)});
        parameter.put("5arg", new CommandParameter[] {new CommandParameter("server", false, new String[]{"server", "se"})});
        parameter.put("6arg", new CommandParameter[] {new CommandParameter("version", false, new String[]{"version", "v"})});
        parameter.put("7arg", new CommandParameter[] {new CommandParameter("controlpanel", false, new String[]{"controlpanel", "cp", "c"})});
        parameter.put("8arg", new CommandParameter[] {new CommandParameter("reload", false, new String[]{"reload", "r"})});

        setCommandParameters(parameter);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) 
	{
		if(args.length==0)
		{
			sendHelp(sender);
			return true;
		}
		
		switch (args[0]) 
		{
			case "help":
			case "h":{
				sendHelp(sender);
				break;
			}
			case "buy":
			case "b":
			case "sell":
			case "s": {
				if (sender instanceof Player)
				{
					Pair<Boolean, Shop> vi = Quickshopx.ins.interactionShopListenerInstance.isVaildInteraction(sender.getName());
					if(vi!=null)
					{
						if(vi.key.booleanValue())
						{
							Shop shop = vi.value;
							
							if(shop.shopData.owner.equals(sender.getName()))
							{
								if(args[0].equals("b") || args[0].equals("buy"))
								{
									if(shop.shopData.type!=ShopType.BUY)
									{
										shop.shopData.type = ShopType.BUY;
										
										Quickshopx.ins.multiShopsConfig.getShopsConfig(shop, false).save();
										
										shop.updateSignText();
										
										sender.sendMessage(L.get(Lang.im_shop_type_updated, "{TYPE}", ShopType.BUY.toString()));
									} else {
										sender.sendMessage(L.get(Lang.im_shop_type_donot_need_update));
									}
								} else if(args[0].equals("s") || args[0].equals("sell"))
								{
									if(shop.shopData.type!=ShopType.SELL)
									{
										shop.shopData.type = ShopType.SELL;
										
										Quickshopx.ins.multiShopsConfig.getShopsConfig(shop, false).save();
										
										shop.updateSignText();
										
										sender.sendMessage(L.get(Lang.im_shop_type_updated, "{TYPE}", ShopType.SELL.toString()));
									} else {
										sender.sendMessage(L.get(Lang.im_shop_type_donot_need_update));
									}
								}
								
										
							} else {
								sender.sendMessage(L.get(Lang.im_not_allow_modify_price_not_owner));
							}
						} else {
							sender.sendMessage(L.get(Lang.im_interaction_timeout));
						}
						
					}else{
						sender.sendMessage(L.get(Lang.im_not_selected_shop));
					}
				} else {
					sender.sendMessage(L.get(Lang.im_intercept_console));
				}
				break;
			}
			case "price":
			case "p":{
				if (sender instanceof Player)
				{
					if (args.length == 2) {
						if (Quickshopx.isInteger(args[1]))
						{
							Pair<Boolean, Shop> vi = Quickshopx.ins.interactionShopListenerInstance.isVaildInteraction(sender.getName());
							
							if(vi!=null)
							{
								Shop shop = vi.value;
								
								if(shop.shopData.owner.equals(sender.getName()))
								{
									shop.shopData.price = Float.valueOf(args[1]);
									
									Quickshopx.ins.multiShopsConfig.getShopsConfig(shop, false).save();
									
									shop.updateSignText();
									
									sender.sendMessage(L.get(Lang.im_shop_price_updated, String.format("%.2f", shop.shopData.price)));
								} else {
									sender.sendMessage(L.get(Lang.im_not_allow_modify_price_not_owner));
								}
							} else {
								sender.sendMessage(L.get(Lang.im_not_selected_shop));
							}
						} else {
							sender.sendMessage(L.get(Lang.im_price_wrong_format));
						}
					} else {
						sender.sendMessage(L.get(Lang.im_price_wrong_args));
					}
				} else {
					sender.sendMessage(L.get(Lang.im_intercept_console));
				}
				
				break;
			}
			case "server":
			case "se":{
				if (sender instanceof Player) 
				{
					if(sender.isOp())
					{
						Pair<Boolean, Shop> vi = Quickshopx.ins.interactionShopListenerInstance.isVaildInteraction(sender.getName());
						
						if(vi!=null)
						{
							Shop shop = vi.value;
							
							shop.shopData.serverShop = !shop.shopData.serverShop;
							
							Quickshopx.ins.multiShopsConfig.getShopsConfig(shop, false).save();
							
							shop.updateSignText();
							
							sender.sendMessage(shop.shopData.serverShop ? L.get(Lang.im_shop_updated_server) : L.get(Lang.im_shop_updated_ordinary));
						} else {
							sender.sendMessage(L.get(Lang.im_not_selected_shop));
						}
					}
				} else {
					sender.sendMessage(L.get(Lang.im_intercept_console));
				}
				break;
			}
			case "version":
			case "v":{
				if (!(sender instanceof Player && !sender.isOp()))
				{
					sender.sendMessage(TextFormat.colorize(String.format(Quickshopx.ins.getDescription().getDescription())));
				}
				break;
			}
			
			case "controlpanel":
			case "cp":
			case "c":
			{
				if (sender instanceof Player) 
				{
					if(((Player)sender).isOp())
					{
						((Player)sender).showFormWindow(new PluginControlPanel());
					}
				} else {
					sender.sendMessage(L.get(Lang.im_intercept_console));
				}
				
				break;
			}
			
			case "reload":
			case "r":
			{
				if (!(sender instanceof Player && !sender.isOp()))
				{
					try
					{
						Quickshopx.ins.loadConfig();

						sender.sendMessage(L.get(Lang.plugin_message_reload_done));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						sender.sendMessage(L.get(Lang.plugin_message_reload_failed));
					}


				}
				
				break;
			}
		}
		
		return true;
	}
	
	void sendHelp(CommandSender sender)
	{
		sender.sendMessage(L.get(Lang.plugin_message_help_normal));
		
        if(!(sender instanceof Player && !sender.isOp()))
        {
        	sender.sendMessage(L.get(Lang.plugin_message_help_operator));
        }
	}

}
