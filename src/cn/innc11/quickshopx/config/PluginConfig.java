package cn.innc11.quickshopx.config;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.listener.HologramItemListener;
import cn.nukkit.utils.TextFormat;
import static cn.innc11.quickshopx.utils.Lang.*;
import cn.innc11.quickshopx.form.PluginControlPanel.*;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class PluginConfig extends BaseConfig
{
	@DefaultValue(defaultValue = 5)
	public int version;

	@DefaultValue(defaultString = "cn")
	public String language;

	@DefaultValue(defaultValue = 1)
	@PresentInForm(LangKey = cp_interaction_way)
	public InteractionWay interactionWay;

	@DefaultValue(defaultValue = 5000, rangeMin = 2*1000, rangeMax = 10*1000, step = 500)
	@PresentInForm(LangKey = cp_interaction_time)
	public int interactionTime;

	@DefaultValue(defaultValue = 150, rangeMin = 10, rangeMax = 1000, step = 10)
	@PresentInForm(LangKey = cp_packet_send_ps)
	public int packetSendLimit;

	@DefaultValue(defaultValue = 1)
	@PresentInForm(LangKey = cp_link_with_residence)
	public boolean linkWithResidencePlugin;

	@DefaultValue(defaultValue = 1)
	@PresentInForm(LangKey = cp_create_in_residence_only)
	public boolean createShopInResidenceOnly;

	@DefaultValue(defaultValue = 0)
	@PresentInForm(LangKey = cp_op_ignore_build_permission)
	public boolean ignoreOpBuildPermission;

	@DefaultValue(defaultValue = 1)
	@PresentInForm(LangKey = cp_hopper_limit)
	public boolean hopperLimit;

	@DefaultValue(defaultValue = 1)
	@PresentInForm(LangKey = cp_hologram_item)
	@UpdateCallbackInForm(mothedName = "updateHologramItem")
	public boolean hologramItem;

	@DefaultValue(defaultValue = 0)
	@PresentInForm(LangKey = cp_snake_mode_destroy_shop)
	public boolean snakeModeDestroyShopOnly;

	@DefaultValue(defaultValue = 0)
	@PresentInForm(LangKey = cp_use_custom_item_name)
	public boolean useCustomItemNames;

	@PresentInForm(LangKey = cp_debug)
	@DefaultValue(defaultValue = 0)
	public boolean debug;
	
	public PluginConfig(File file)
	{
		super(file, true, true);

		reload();

		if(config.getKeys().size()==0)
		{
			save();
		}
	}

	@Override
	public void Save()
	{
		config.getRootSection().clear();

		for(Field field : getClass().getDeclaredFields())
		{
			if(field.isAnnotationPresent(DefaultValue.class))
			{
				int defaultValue = field.getAnnotation(PluginConfig.DefaultValue.class).defaultValue();
				String defaultString = field.getAnnotation(DefaultValue.class).defaultString();
				if(defaultValue<0 && defaultString.isEmpty()) continue;

				String fieldName = field.getName();

				try {

					if(field.getType().isEnum())
					{
						config.set(fieldName, field.get(this).toString());
					}else{
						config.set(fieldName, field.get(this));
					}

				} catch (IllegalAccessException e) {e.printStackTrace();}
			}
		}

		config.save();
	}

	@Override
	public void reload()
	{
		config.reload();

		for(Field field : getClass().getDeclaredFields())
		{
			if(field.isAnnotationPresent(DefaultValue.class))
			{
				int fieldDefaultValue = field.getAnnotation(DefaultValue.class).defaultValue();
				String defaultString = field.getAnnotation(DefaultValue.class).defaultString();

				if(fieldDefaultValue<0 && defaultString.isEmpty()) continue;

				String fieldName = field.getName();

				try {

					if(field.getType()==int.class)
					{
						int defaultValue = fieldDefaultValue;

						field.setInt(this, config.getInt(fieldName, defaultValue));
 					}

					if(field.getType()==boolean.class)
					{
						boolean defaultValue = fieldDefaultValue!=0;
						field.setBoolean(this, config.getBoolean(fieldName, defaultValue));
					}

					if(field.getType()==String.class)
					{
						field.set(this, config.getString(fieldName, defaultString));
					}

					if(field.getType().isEnum())
					{
						int defaultIndex = Math.min(field.getType().getEnumConstants().length-1, fieldDefaultValue);

						String configText = config.getString(fieldName, field.getType().getEnumConstants()[defaultIndex].toString());

						boolean found = false;

						for (Object v : field.getType().getEnumConstants())
						{
							InteractionWay object = (InteractionWay) v;

							if(configText.equals(object.toString()))
							{
								field.set(this, object);
								found = true;
							}
						}

						if(!found)
						{
							field.set(this, field.getType().getEnumConstants()[defaultIndex]);
						}

					}

				} catch (IllegalAccessException e) {e.printStackTrace();}

			}
		}

		if(debug)
		{
			Quickshopx.ins.getLogger().error(TextFormat.colorize("&cDEBUG is on"));
		}

	}

	public void updateHologramItem(Boolean lastHologramItemShowState)
	{
		HologramItemListener hl = Quickshopx.ins.hologramListener;

		if(hologramItem && !lastHologramItemShowState)
		{
			hl.addAllItemEntityForAllPlayer();
		}

		if(!hologramItem && lastHologramItemShowState)
		{
			hl.removeAllItemEntityForAllPlayer();
		}
	}

	public enum InteractionWay
	{
		NEVER("ChatBar"),
		DOUBLE_CLICK("Both"),
		ALWAYS("Interface");
		
		private String text;
		
		InteractionWay(String prettyText)
		{
			this.text = prettyText;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	@Target({ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface DefaultValue
	{
		String defaultString() default "";
		int defaultValue() default -1;
		int rangeMin() default -1;
		int rangeMax() default -1;
		int step() default 1;
	}

}
