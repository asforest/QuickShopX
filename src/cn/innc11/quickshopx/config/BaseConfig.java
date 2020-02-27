package cn.innc11.quickshopx.config;

import java.io.File;
import java.io.FileNotFoundException;

import cn.innc11.quickshopx.Quickshopx;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.Config;

public abstract class BaseConfig
{
	public Config config;
	protected File file;

	private boolean modified = false;
	private boolean saving = false;
	private PluginTask<Quickshopx> saveTask;
	private boolean synchronousSave;

	public BaseConfig(File file, boolean autoLoad, boolean saveable)
	{
		this(file, autoLoad, saveable, false);
	}

	public BaseConfig(File file, boolean autoLoad, boolean saveable, boolean synchronousSave)
	{
		this.file = file;
		this.synchronousSave = synchronousSave;

		if(autoLoad)
		{
			initConfig();

			if(saveable)
			{
				saveTask = new PluginTask<Quickshopx>(Quickshopx.ins)
				{
					@Override
					public void onRun(int currentTicks)
					{
						while(modified)
						{
							modified = false;
							Save();
						}

						saving = false;
					}
				};

			}
		}
	}

	protected boolean initConfig()
	{
		if(config==null)
		{
			config = new Config(file, Config.YAML);
			return true;
		}

		return false;
	}

	protected static File checkFile(File file) throws FileNotFoundException
	{
		if(!file.exists()) throw new FileNotFoundException(String.format("File %s not found!", file.getAbsolutePath()));

		return file;
	}

	public String getWholeFileName()
	{
		return file.getName();
	}

	public String getFileNameWithoutSuffix()
	{
		return getWholeFileName().replaceAll("\\.\\w+$", "");
	}

	public final void save()
	{
		if(synchronousSave)
		{
			modified = true;

			saveTask.onRun(0);
		}else{
			if(saveTask!=null)
			{
				modified = true;

				if(!saving)
				{
					saving = true;
					Quickshopx.server.getScheduler().scheduleTask(Quickshopx.ins, saveTask, true);
				}
			}
		}

	}

	public abstract void reload();

	protected abstract void Save();
}
