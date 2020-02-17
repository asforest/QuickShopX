package cn.innc11.quickshopx.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.config.PluginConfig;
import cn.innc11.quickshopx.utils.L;
import cn.innc11.quickshopx.utils.Lang;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;

public class PluginControlPanel extends FormWindowCustom implements FormResponse
{
	public PluginControlPanel() 
	{
		super(L.get(Lang.cp_title,
				"{PLUGIN_NAME}",  Quickshopx.ins.getDescription().getName(),
				"{CONFIG_VERSION}", Quickshopx.ins.pluginConfig.version +"",
				"{PLUGIN_VERSION}", Quickshopx.ins.getDescription().getVersion()));
		
		PluginConfig pc = Quickshopx.ins.pluginConfig;

		for(Field field : PluginConfig.class.getDeclaredFields())
		{
			if(field.isAnnotationPresent(PluginConfig.DefaultValue.class))
			{
				String fieldName = field.getName();
				int annotationDefaultValue = field.getAnnotation(PluginConfig.DefaultValue.class).defaultValue();
				String annotationDefaultString = field.getAnnotation(PluginConfig.DefaultValue.class).defaultString();
				Object instanceValue_ = null;
				try {
					instanceValue_ = pc.getClass().getDeclaredField(fieldName).get(pc);
				} catch (Exception e) {e.printStackTrace();}
				boolean ignore = !field.isAnnotationPresent(PresentInForm.class);
				if((annotationDefaultValue<0 && annotationDefaultString.isEmpty()) || ignore) continue;
				String langText = L.get(field.getAnnotation(PresentInForm.class).LangKey());

				if(field.getType()==int.class)
				{
					int instanceValue = (int) instanceValue_;
					int min = field.getAnnotation(PluginConfig.DefaultValue.class).rangeMin();
					int max = field.getAnnotation(PluginConfig.DefaultValue.class).rangeMax();
					int step = field.getAnnotation(PluginConfig.DefaultValue.class).step();

					if(min<0 || max<0 || min>max) continue;

					addElement(new ElementSlider(langText, min, max, step, Math.min(Math.max(min, instanceValue), max)));
				}

				if(field.getType()==boolean.class)
				{
					boolean instanceValue = (boolean) instanceValue_;

					addElement(new ElementToggle(langText, instanceValue));
				}

				if(field.getType()==String.class)
				{
					String instanceValue = (String) instanceValue_;

					addElement(new ElementInput(langText, "", instanceValue));
				}

				if(field.getType().isEnum())
				{
					int instanceIndex = 0;
					for(Object p : field.getType().getEnumConstants())
					{
						if(instanceValue_==p) break;
						instanceIndex++;
					}

					ArrayList<String> list = new ArrayList<>();
					for(Object p : field.getType().getEnumConstants())
					{
						list.add(p.toString());
					}

					addElement(new ElementDropdown(langText, list, instanceIndex));
				}

			}
		}


	}

	@Override
	public void onFormResponse(PlayerFormRespondedEvent e) 
	{
		if(!e.getPlayer().isOp())
			return;

		try {
			e.getPlayer().sendMessage(L.get(Lang.plugin_message_configure_updated));

			PluginConfig pc = Quickshopx.ins.pluginConfig;
			int index = 0;
			for (Field field : PluginConfig.class.getDeclaredFields())
			{
				if (field.isAnnotationPresent(PluginConfig.DefaultValue.class))
				{
					String fieldName = field.getName();
					int annotationDefaultValue = field.getAnnotation(PluginConfig.DefaultValue.class).defaultValue();
					String annotationDefaultString = field.getAnnotation(PluginConfig.DefaultValue.class).defaultString();
					Object instanceValue_ = pc.getClass().getDeclaredField(fieldName).get(pc);
					boolean ignore = !field.isAnnotationPresent(PresentInForm.class);
					if((annotationDefaultValue<0 && annotationDefaultString.isEmpty()) || ignore) continue;

					if (field.getType() == int.class)
					{
						int instanceValue = (int) getResponse().getSliderResponse(index);
						index++;
						pc.getClass().getDeclaredField(fieldName).set(pc, instanceValue);
					}

					if (field.getType() == boolean.class)
					{
						boolean instanceValue = getResponse().getToggleResponse(index);
						index++;
						pc.getClass().getDeclaredField(fieldName).set(pc, instanceValue);
					}

					if (field.getType().isEnum())
					{
						int newInstanceIndex = getResponse().getDropdownResponse(index).getElementID();
						index++;
						pc.getClass().getDeclaredField(fieldName).set(pc, field.getType().getEnumConstants()[newInstanceIndex]);
					}

					if(field.getType()==String.class)
					{
						String instanceValue = getResponse().getInputResponse(index);
						index++;
						pc.getClass().getDeclaredField(fieldName).set(pc, instanceValue);
					}

					if (field.isAnnotationPresent(UpdateCallbackInForm.class))
					{
						String mothedName = field.getAnnotation(UpdateCallbackInForm.class).mothedName();
						field.getDeclaringClass().getDeclaredMethod(mothedName, instanceValue_.getClass()).invoke(pc, instanceValue_);
					}

				}
			}

		}catch (Exception ex){ex.printStackTrace();}

		Quickshopx.ins.pluginConfig.save();
	}

	@Override
	public void onFormClose(PlayerFormRespondedEvent e)
	{

	}


	@Target({ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface PresentInForm
	{
		Lang LangKey();
	}

	@Target({ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface UpdateCallbackInForm
	{
		String mothedName();
	}

}
