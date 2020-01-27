package cn.innc11.QuickShopX.config;

import cn.innc11.QuickShopX.QuickShopXPlugin;
import cn.nukkit.utils.TextFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class PluginConfig extends MyConfig
{
	@DefaultValue(defalutValue = 0)
	public int configVersion;

	@DefaultValue(defalutValue = 5000)
	public int interactionInterval;

	@DefaultValue(defalutValue = 1)
	public boolean hologramItemShow;

	@DefaultValue(defalutValue = 1)
	public FormOperate formOperate;

	@DefaultValue(defalutValue = 500)
	public int packetSendPerSecondMax;

	@DefaultValue(defalutValue = 1)
	public boolean interactionWithResidencePlugin;

	@DefaultValue(defalutValue = 1)
	public boolean createShopInResidenceOnly;

	@DefaultValue(defalutValue = 0)
	public boolean opIgnoreResidenceBuildPermission;

	@DefaultValue(defalutValue = 0)
	public boolean snakeModeDestroyShop;

	@DefaultValue(defalutValue = 1)
	public boolean hopperActiveInResidenceOnly;

	@DefaultValue(defalutValue = 0)
	public boolean useCustomItemName;

	@DefaultValue(defalutValue = 0)
	public boolean debug;
	
	public PluginConfig()
	{
		super("config.yml");

		reload();
	}

	@Override
	public void save()
	{
		config.getRootSection().clear();
		

		for(Field field : getClass().getDeclaredFields())
		{
			if(field.isAnnotationPresent(DefaultValue.class))
			{
				int annotationValue = field.getAnnotation(DefaultValue.class).defalutValue();
				if(annotationValue<0) continue;

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
				int annotationValue = field.getAnnotation(DefaultValue.class).defalutValue();
				if(annotationValue<0) continue;

				String fieldName = field.getName();

				try {

					if(field.getType()==int.class)
					{
						int defaultValue = annotationValue;

						field.setInt(this, config.getInt(fieldName, defaultValue));

					}

					if(field.getType()==boolean.class)
					{
						boolean defaultValue = annotationValue!=0;
						field.setBoolean(this, config.getBoolean(fieldName, defaultValue));
					}

					if(field.getType().isEnum())
					{
						int defaultIndex = Math.min(field.getType().getEnumConstants().length-1, annotationValue);

						String configText = config.getString(fieldName, field.getType().getEnumConstants()[defaultIndex].toString());

						boolean found = false;

						for (Object v : field.getType().getEnumConstants())
						{
							FormOperate object = (FormOperate) v;

							if(configText.equals(object.text))
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


				/*
				try {
					QuickShopXPlugin.instance.getLogger().error(field.getName()+" --> "+field.get(this));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				 */


			}
		}

		if(debug)
			QuickShopXPlugin.instance.getLogger().error(TextFormat.colorize("&cDEBUG is on"));

	}

	public static enum FormOperate
	{
		NEVER("never"),
		DOUBLE_CLICK("doubleclick"),
		ALWAYS("always");
		
		public String text;
		
		FormOperate(String text)
		{
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	@Target({ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	@interface DefaultValue{
		int defalutValue() default -1;
	}
}
