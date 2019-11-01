package cn.innc11.QuickShop2.form;

import java.util.Arrays;

import cn.innc11.QuickShop2.QuickShopXPlugin;
import cn.innc11.QuickShop2.config.LangConfig.Lang;
import cn.innc11.QuickShop2.config.PluginConfig;
import cn.innc11.QuickShop2.config.PluginConfig.FormOperate;
import cn.innc11.QuickShop2.utils.L;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.element.ElementStepSlider;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;

public class PluginControlPanel extends FormWindowCustom implements FormRespone 
{
	public PluginControlPanel() 
	{
		super(L.get(Lang.FORM_CONTROL_PANEL__TITLE, "{PLUGIN_NAME}",  QuickShopXPlugin.instance.getDescription().getName(), "{CONFIG_FILE_VERSION}", QuickShopXPlugin.instance.pluginConfig.configVersion+"", "{PLUGIN_VERSION}", QuickShopXPlugin.instance.getDescription().getVersion()));
		
		PluginConfig pc = QuickShopXPlugin.instance.pluginConfig;
		
		//商店交互时间
		//shop interaction timeout
		addElement(new ElementSlider(L.get(Lang.FORM_CONTROL_PANEL__SHOP_INTERACTION_TIME), 1F, 60F, 1, pc.interactionInterval/1000>60 ? 60 : pc.interactionInterval/1000));
		
		//商店全息显示
		//hologram item
		addElement(new ElementToggle(L.get(Lang.FORM_CONTROL_PANEL__SHOP_HOLOGRAM_ITEM), pc.hologramItemShow));
		
		//FormWindow
		addElement(new ElementStepSlider(L.get(Lang.FORM_CONTROL_PANEL__FORM_OPERATE), Arrays.asList("Nerver", "DoubleClick", "Always"), pc.formOperate.ordinal()));
		
		//全息显示的每秒最大发包量
		//packet send per second max
		addElement(new ElementSlider(L.get(Lang.FORM_CONTROL_PANEL__PACKET_SEND_PS), 1F, 800F, 1, pc.packetSendPerSecondMax>500 ? 500 : pc.packetSendPerSecondMax));
		
		//和Residence插件交互
		//interaction with residence plugin
		addElement(new ElementToggle(L.get(Lang.FORM_CONTROL_PANEL__INTERACTION_WITH_RESIDENCE_PLUGIN), pc.interactionWithResidencePlugin));
		
		//只能在领地内创建商店
		//create shop in residence only
		addElement(new ElementToggle(L.get(Lang.FORM_CONTROL_PANEL__CREATE_SHOP_IN_RESIDENCE_NOLY), pc.createShopInResidenceOnly));
		
		//op可以无视领地权限进行创建和破坏商店(无视build权限)
		//op can ignore residence build permission
		addElement(new ElementToggle(L.get(Lang.FORM_CONTROL_PANEL__OP_IGNORE_RESIDENCE_BUILD_PERMISSION), pc.opIgnoreResidenceBuildPermission));

		//只能在潜行模式下破坏商店
		//only destroy shop in snake mode
		addElement(new ElementToggle(L.get(Lang.FORM_CONTROL_PANEL__SNAKE_MODE_DESTROY_SHOP), pc.snakeModeDestroyShop));
	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		if(!e.getPlayer().isOp())
			return;

		int interactionInterval = (int) getResponse().getSliderResponse(0);
		boolean hologramItemShow = getResponse().getToggleResponse(1);
		int formOperate = getResponse().getStepSliderResponse(2).getElementID();
		int packetSendPerSecondMax = (int) getResponse().getSliderResponse(3);
		boolean  interactionWithResidencePlugin = getResponse().getToggleResponse(4);
		boolean  createShopInResidenceOnly = getResponse().getToggleResponse(5);
		boolean  opIgnoreResidenceBuildPermission = getResponse().getToggleResponse(6);
		boolean  snakeDestroyShop = getResponse().getToggleResponse(7);

		e.getPlayer().sendMessage(L.get(Lang.PLUGIN_MESSAGE_PLUGIN_CONFIGURE_UPDATED));
		
		if(QuickShopXPlugin.instance.pluginConfig.hologramItemShow && !hologramItemShow)
		{
			QuickShopXPlugin.instance.hologramListener.removeAllItemEntityForAllPlayer();
		}
		
		if(!QuickShopXPlugin.instance.pluginConfig.hologramItemShow && hologramItemShow)
		{
			QuickShopXPlugin.instance.pluginConfig.hologramItemShow = hologramItemShow;
			
			QuickShopXPlugin.instance.hologramListener.addAllItemEntityForAllPlayer();
		}
		
		QuickShopXPlugin.instance.pluginConfig.interactionInterval = interactionInterval*1000;
		QuickShopXPlugin.instance.pluginConfig.hologramItemShow = hologramItemShow;
		QuickShopXPlugin.instance.pluginConfig.formOperate = FormOperate.values()[formOperate];
		QuickShopXPlugin.instance.pluginConfig.packetSendPerSecondMax = packetSendPerSecondMax;
		QuickShopXPlugin.instance.pluginConfig.interactionWithResidencePlugin = interactionWithResidencePlugin;
		QuickShopXPlugin.instance.pluginConfig.createShopInResidenceOnly = createShopInResidenceOnly;
		QuickShopXPlugin.instance.pluginConfig.opIgnoreResidenceBuildPermission = opIgnoreResidenceBuildPermission;
		QuickShopXPlugin.instance.pluginConfig.snakeModeDestroyShop = snakeDestroyShop;

		QuickShopXPlugin.instance.pluginConfig.save();
		
	}

}
