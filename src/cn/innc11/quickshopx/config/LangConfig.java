package cn.innc11.quickshopx.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.innc11.quickshopx.Quickshopx;
import cn.innc11.quickshopx.utils.Lang;
import cn.nukkit.utils.TextFormat;

public class LangConfig extends BaseConfig
{
	HashMap<Lang, String> lang = new HashMap<Lang, String>();
	
	public LangConfig(File file) throws FileNotFoundException
	{
		super(checkFile(file), true, true);

		reload();
	}

	@Override
	public void Save()
	{
		config.save();
	}

	@Override
	public void reload() 
	{
		config.reload();
		lang.clear();

		boolean supplement = false;
		int ct = 0;
		
		for(Lang key : Lang.values())
		{
			Object v = config.get(key.name().replace("_", "-"));

			if(v==null)
			{
				config.set(key.name().replace("_", "-"), key.getDefaultLangText());
				lang.put(key, key.getDefaultLangText());
				supplement = true;
				Quickshopx.ins.getLogger().info(TextFormat.colorize("&cSet default language text for "+getFullFileName() +"("+key.name()+")"));
			}

			if(v instanceof String)
			{
				lang.put(key, (String) v);
				ct++;
			} else if(v instanceof ArrayList)
			{
				ArrayList<String> values = (ArrayList<String>) v;

				StringBuffer sb = new StringBuffer();

				for(int i=0;i<values.size();i++)
				{
					sb.append(values.get(i));
					if(i!=values.size()-1) {
						sb.append("\n");
					}
				}

				lang.put(key, sb.toString());
				ct++;
			}

			
		}

		if(supplement)
			save();
		
		Quickshopx.logger.info(TextFormat.colorize("Loaded &6"+ct+"&r languages"));
	}
	
	public String get(Lang l, String... argsPair)
	{
		String rawStr = lang.get(l);
		
		int argCount = argsPair.length / 2;
		
		for(int i=0;i<argCount;i++)
		{
			String reg = argsPair[i*2];
			String replacement = argsPair[i*2+1];
			
			if(reg.startsWith("{") && reg.endsWith("}"))
			{
				reg = reg.replaceAll("\\{", "\\\\{");
				reg = reg.replaceAll("\\}", "\\\\}");
				//rawStr = rawStr.replaceAll("\\$"+reg, replacement);
				rawStr = rawStr.replaceAll(reg, replacement);
			}
			
		}
        
		
		return TextFormat.colorize(rawStr);
	}


}
