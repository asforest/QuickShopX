package cn.innc11.QuickShop2.form;

import java.util.Arrays;

import cn.innc11.QuickShop2.Main;
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
		super(L.get(Lang.FORM_CONTROL_PANEL__TITLE, "{PLUGIN_NAME}",  Main.instance.getDescription().getName(), "{CONFIG_FILE_VERSION}", Main.instance.pluginConfig.configVersion+""));
		
		PluginConfig pc = Main.instance.pluginConfig;
		
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
		addElement(new ElementSlider(L.get(Lang.FORM_CONTROL_PANEL__PACKET_SEND_PS), 1F, 100F, 1, pc.packetSendPerSecondMax>1000 ? 1000 : pc.packetSendPerSecondMax));
		
		//和Residence插件交互
		//interaction with residence plugin
		addElement(new ElementToggle(L.get(Lang.FORM_CONTROL_PANEL__INTERACTION_WITH_RESIDENCE_PLUGIN), pc.interactionWithResidencePlugin));
		
		//只能在领地内创建商店
		//create shop in residence only
		addElement(new ElementToggle(L.get(Lang.FORM_CONTROL_PANEL__CREATE_SHOP_IN_RESIDENCE_NOLY), pc.createShopInResidenceOnly));
		
		//op可以无视领地权限进行创建和破坏商店
		//op can ignore residence permission
		addElement(new ElementToggle(L.get(Lang.FORM_CONTROL_PANEL__OP_IGNORE_RESIDENCE), pc.opIgnoreResidence));
	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		int interactionInterval = (int) getResponse().getSliderResponse(0);
		boolean hologramItemShow = getResponse().getToggleResponse(1);
		int formOperate = getResponse().getStepSliderResponse(2).getElementID();
		int packetSendPerSecondMax = (int) getResponse().getSliderResponse(3);
		boolean  interactionWithResidencePlugin = getResponse().getToggleResponse(4);
		boolean  createShopInResidenceOnly = getResponse().getToggleResponse(5);
		boolean  opIgnoreResidence = getResponse().getToggleResponse(6);

		e.getPlayer().sendMessage(L.get(Lang.PLUGIN_MESSAGE_PLUGIN_CONFIGURE_UPDATED));
		
		if(Main.instance.pluginConfig.hologramItemShow && !hologramItemShow)
		{
			Main.instance.hologramListener.removeAllItemEntityForAllPlayer();
		}
		
		if(!Main.instance.pluginConfig.hologramItemShow && hologramItemShow)
		{
			Main.instance.pluginConfig.hologramItemShow = hologramItemShow;
			
			Main.instance.hologramListener.addAllItemEntityForAllPlayer();
		}
		
		Main.instance.pluginConfig.interactionInterval = interactionInterval*1000;
		Main.instance.pluginConfig.hologramItemShow = hologramItemShow;
		Main.instance.pluginConfig.formOperate = FormOperate.values()[formOperate];
		Main.instance.pluginConfig.packetSendPerSecondMax = packetSendPerSecondMax;
		Main.instance.pluginConfig.interactionWithResidencePlugin = interactionWithResidencePlugin;
		Main.instance.pluginConfig.createShopInResidenceOnly = createShopInResidenceOnly;
		Main.instance.pluginConfig.opIgnoreResidence = opIgnoreResidence;
		
		Main.instance.pluginConfig.save();
		
	}

}
