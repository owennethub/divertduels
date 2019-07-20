package club.batowen;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import club.batowen.duels.DuelsConfig;
import club.batowen.duels.commands.DuelCommand;
import net.milkbowl.vault.economy.Economy;

public class DuelsMain extends JavaPlugin{

    
    private static DuelsMain instance;
    
    public final DuelsConfig duelsconfig = new DuelsConfig(this);
    public final Logger log = Logger.getLogger("Minecraft");
    
    public Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;

        /**
         * 
         * Safely loads the configuration file and Economy
         * If returns false disables plugin to avoid misfunction
         * 
         */
        if(!getDataFolder().exists()){
            getDataFolder().mkdirs();
        }
        if(!duelsconfig.loadFile()){
            log.info(("Could not load duels file"));
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if(!setupEconomy()){
            log.info(("Could not load Vault dependency"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
        
        getCommand("duelo").setExecutor(new DuelCommand());
    
    }

    public static DuelsMain getInstance() {
        return instance;
    }

    /**
     * Seting up Economy dependency
     * @return
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }


}
