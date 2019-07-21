package club.batowen.duels;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DuelsConfig{

    private Location spawn,pos1,pos2,assist;
    private final File configuration;
    private FileConfiguration yalm;
    public final String matchcancelled = "§fO seu §c§lDUELO §ffoi cancelado por durar mais 3 minutos";

    public DuelsConfig(JavaPlugin plugin){
        configuration = new File(plugin.getDataFolder(), "configuration.yml");
    }

    public boolean loadFile() {
        try {
            if(!configuration.exists()){
                configuration.createNewFile();
            } else {
                yalm = YamlConfiguration.loadConfiguration(configuration);
                if(yalm.getConfigurationSection("spawn") != null){
                    spawn = extractLocation("spawn");
                }   
                if(yalm.getConfigurationSection("pos1") != null){
                    pos1 = extractLocation("pos1");
                }  
                if(yalm.getConfigurationSection("pos2") != null){
                    pos2 = extractLocation("pos2");
                }  
                if(yalm.getConfigurationSection("assist") != null){
                    assist = extractLocation("assist");
                }  
            }
        } catch (IOException e) {
            return false;
		}
        return true;
    }

    private Location extractLocation(String section){
        Location location = new Location(Bukkit.getWorld(yalm.getString(section+".world")),yalm.getDouble(section+".x"),yalm.getDouble(section+".y"),yalm.getDouble(section+".z"));
        location.setPitch((float)yalm.getLong(section+".pitch"));
        location.setYaw((float)yalm.getLong(section+".yaw"));
        return location;
    }

    private void setLocation(String section, Location location) {
        yalm.set("section.world", location.getWorld().getName());
        yalm.set("section.x", location.getX());
        yalm.set("section.y", location.getY());
        yalm.set("section.z", location.getZ());
        yalm.set("section.pitch", location.getPitch());
        yalm.set("section.yaw", location.getYaw());
        try {
            yalm.save(configuration);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean readyLoc(){
        return spawn !=null && pos1 !=null && pos2 != null && assist !=null;
    }


    public Location getSpawn() {
        return spawn;
    }

    public Location getLocation1() {
        return pos1;
    }

    public Location getLocation2() {
        return pos2;
    }
    
    public Location getAssistLocation() {
        return assist;
    }

    /**
     * @param assist the assist to set
     */
    public void setAssist(Location assist) {
        this.assist = assist;
        setLocation("assist", assist);
    }

    /**
     * @param pos1 the pos1 to set
     */
    public void setPos1(Location pos1) {
        this.pos1 = pos1;
        setLocation("pos1", pos1);
    }
    
    /**
     * @param pos2 the pos2 to set
     */
    public void setPos2(Location pos2) {
        this.pos2 = pos2;
        setLocation("pos2", pos2);
    }
    
    /**
     * @param spawn the spawn to set
     */
    public void setSpawn(Location spawn) {
        this.spawn = spawn;
        setLocation("spawn", spawn);
    }
}
