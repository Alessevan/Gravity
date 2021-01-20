package fr.bakaaless.gravity.plugin;

import fr.bakaaless.gravity.storage.GeneralConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class GravityPlugin extends JavaPlugin {

    public static GravityPlugin get() {
        return JavaPlugin.getPlugin(GravityPlugin.class);
    }

    @Override
    public void onEnable() {
        GeneralConfig.get().init();
    }

    @Override
    public void onDisable() {

    }

}
