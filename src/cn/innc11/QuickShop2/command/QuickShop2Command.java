package cn.innc11.QuickShop2.command;

import java.util.HashMap;

import cn.innc11.QuickShop2.QuickShop2Plugin;
import cn.innc11.QuickShop2.Pair;
import cn.innc11.QuickShop2.config.LangConfig.Lang;
import cn.innc11.QuickShop2.form.PluginControlPanel;
import cn.innc11.QuickShop2.shop.Shop;
import cn.innc11.QuickShop2.shop.ShopType;
import cn.innc11.QuickShop2.utils.L;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class QuickShop2Command extends Command
{

	public QuickShop2Command() 
	{
		super("qs");
		
		setDescription("QuickShop2 Command");
		setAliases(new String[]{"shop","qshop","quickshop", "qs2"});
        setUsage("/qs <subcommand> [args]");
        
        HashMap<String, CommandParameter[]> parameter = new HashMap<>();
        
        parameter.put("1arg", new CommandParameter[] {new CommandParameter("help(h)", false, new String[]{"help", "h"})});
        parameter.put("2arg", new CommandParameter[] {new CommandParameter("buy(b)", false, new String[]{"buy", "b"})});
        parameter.put("3arg", new CommandParameter[] {new CommandParameter("sell(s)", false, new String[]{"sell", "s"})});
        parameter.put("4arg", new CommandParameter[] {new CommandParameter("price(p)", false, new String[]{"price", "p"}), new CommandParameter("price", CommandParamType.INT, false)});
        parameter.put("5arg", new CommandParameter[] {new CommandParameter("unlimited(u)", false, new String[]{"unlimited", "u"})});
        parameter.put("6arg", new CommandParameter[] {new CommandParameter("version(v)", false, new String[]{"version", "v"})});
        parameter.put("7arg", new CommandParameter[] {new CommandParameter("controlpanel(cp)", false, new String[]{"controlpanel", "cp", "c"})});
        parameter.put("8arg", new CommandParameter[] {new CommandParameter("reload(r)", false, new String[]{"reload", "r"})});
        
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
			case "s":{
				if (sender instanceof Player)
				{
					Pair<Boolean, Shop> vi = QuickShop2Plugin.instance.interactionShopListenerInstance.isVaildInteraction(sender.getName());
					if(vi!=null)
					{
						if(vi.key.booleanValue())
						{
							Shop shop = vi.value;
							
							if(shop.data.owner.equals(sender.getName()))
							{
								if(args[0].equals("b") || args[0].equals("buy"))
								{
									if(shop.data.type!=ShopType.BUY)
									{
										shop.data.type = ShopType.BUY;
										
										QuickShop2Plugin.instance.shopConfig.save();
										
										shop.updateSignText();
										
										sender.sendMessage(L.get(Lang.IM_SHOP_TYPE_UPDATED, "{NEW_SHOP_TYPE}", ShopType.BUY.toString()));
									} else {
										sender.sendMessage(L.get(Lang.IM_SHOP_TYPE_NO_UPDATE));
									}
								} else if(args[0].equals("s") || args[0].equals("sell"))
								{
									if(shop.data.type!=ShopType.SELL)
									{
										shop.data.type = ShopType.SELL;
										
										QuickShop2Plugin.instance.shopConfig.save();
										
										shop.updateSignText();
										
										sender.sendMessage(L.get(Lang.IM_SHOP_TYPE_UPDATED, "{NEW_SHOP_TYPE}", ShopType.SELL.toString()));
									} else {
										sender.sendMessage(L.get(Lang.IM_SHOP_TYPE_NO_UPDATE));
									}
								}
								
										
							} else {
								sender.sendMessage(L.get(Lang.IM_PRICE_NO_UPDATE_NO_ONWER));
							}
						} else {
							sender.sendMessage(L.get(Lang.IM_INTERACTOIN_TIMEOUT));
						}
						
					}else{
						sender.sendMessage(L.get(Lang.IM_NO_SELECTED_SHOP));
					}
				} else {
					sender.sendMessage(L.get(Lang.IM_INTERCEPT_CONSOLE));
				}
				break;
			}
			case "price":
			case "p":{
				if (sender instanceof Player)
				{
					if (args.length == 2) {
						if (QuickShop2Plugin.isInteger(args[1]))
						{
							Pair<Boolean, Shop> vi = QuickShop2Plugin.instance.interactionShopListenerInstance.isVaildInteraction(sender.getName());
							
							if(vi!=null)
							{
								Shop shop = vi.value;
								
								if(shop.data.owner.equals(sender.getName()))
								{
									shop.data.price = Float.valueOf(args[1]);
									
									QuickShop2Plugin.instance.shopConfig.save();
									
									shop.updateSignText();
									
									sender.sendMessage(L.get(Lang.IM_SHOP_PRICE_UPDATED, String.format("%.2f", shop.data.price)));
								} else {
									sender.sendMessage(L.get(Lang.IM_PRICE_NO_UPDATE_NO_ONWER));
								}
							} else {
								sender.sendMessage(L.get(Lang.IM_NO_SELECTED_SHOP));
							}
						} else {
							sender.sendMessage(L.get(Lang.IM_PRICE_WRONG_FORMAT));
						}
					} else {
						sender.sendMessage(L.get(Lang.IM_PRICE_NO_UPDATE_WRONG_ARGS));
					}
				} else {
					sender.sendMessage(L.get(Lang.IM_INTERCEPT_CONSOLE));
				}
				
				break;
			}
			case "unlimited":
			case "ul":
			case "u":{
				if (sender instanceof Player) 
				{
					if(((Player)sender).isOp())
					{
						Pair<Boolean, Shop> vi = QuickShop2Plugin.instance.interactionShopListenerInstance.isVaildInteraction(sender.getName());
						
						if(vi!=null)
						{
							Shop shop = vi.value;
							
							shop.data.serverShop = !shop.data.serverShop;
							
							QuickShop2Plugin.instance.shopConfig.save();
							
							shop.updateSignText();
							
							sender.sendMessage(shop.data.serverShop ? L.get(Lang.IM_SHOP_UPDATED_SERVER_SHOP) : L.get(Lang.IM_SHOP_UPDATED_LIMITED));
						} else {
							sender.sendMessage(L.get(Lang.IM_NO_SELECTED_SHOP));
						}
					}
				} else {
					sender.sendMessage(L.get(Lang.IM_INTERCEPT_CONSOLE));
				}
				break;
			}
			case "version":
			case "v":{
				if (!(sender instanceof Player && !((Player)sender).isOp()))
				{
					PluginBase plugin = QuickShop2Plugin.instance;
					sender.sendMessage(TextFormat.colorize(String.format("&l&e%s&r, &dMade by %s, original author WetABQ", plugin.getFullName(), plugin.getDescription().getAuthors().get(0))));
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
					sender.sendMessage(L.get(Lang.IM_INTERCEPT_CONSOLE));
				}
				
				break;
			}
			
			case "reload":
			case "r":
			{
				if (!(sender instanceof Player && !((Player)sender).isOp()))
				{
					QuickShop2Plugin.instance.itemNameConfig.reload();
					QuickShop2Plugin.instance.signTextConfig.reload();
					QuickShop2Plugin.instance.langConfig.reload();
					QuickShop2Plugin.instance.pluginConfig.reload();
					
//					sender.sendMessage("Reload done");
					sender.sendMessage(L.get(Lang.PLUGIN_MESSAGE_RELOAD_DONE));
				}
				
				break;
			}
		}
		
		return true;
	}
	
	void sendHelp(CommandSender sender)
	{
		sender.sendMessage(L.get(Lang.PLUGIN_MESSAGE_HELP_NORMAL));
		
        if(!(sender instanceof Player && !((Player)sender).isOp()))
        {
        	sender.sendMessage(L.get(Lang.PLUGIN_MESSAGE_HELP_OPERATOR));
        }
	}

}
