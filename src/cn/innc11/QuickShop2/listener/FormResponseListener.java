package cn.innc11.QuickShop2.listener;

import cn.innc11.QuickShop2.form.FormRespone;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;

public class FormResponseListener implements Listener 
{
	@EventHandler()
	public void onFormResponse(PlayerFormRespondedEvent event) 
	{
		if (event.getResponse() == null)
			return;
		
		if (!(event.getWindow() instanceof FormRespone))
			return;
		
		((FormRespone)event.getWindow()).onFormResponse(event);
	}
	
}
