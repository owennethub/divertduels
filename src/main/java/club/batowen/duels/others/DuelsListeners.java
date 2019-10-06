package club.batowen.duels.others;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginDisableEvent;

import club.batowen.DuelsMain;
import club.batowen.duels.core.DuelMatch;
import club.batowen.duels.core.DuelsManager;
import net.md_5.bungee.api.ChatColor;

public class DuelsListeners implements Listener{

    private final DuelsManager manager;

    private DuelsListeners(){
        manager = DuelsManager.getInstance();  
    }

    @EventHandler
    public void plugindis(PluginDisableEvent event) {

    }
    @EventHandler
    public void playerquitduel(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(manager.inDuel(player.getUniqueId())) {
            DuelMatch match = manager.lookupMatchByPlayerUuid(player.getUniqueId(), (as)-> as.getState()==1);
            match.stop("§fO jogador §b"+player.getName()+" §fsaiu do duelo.");
            UUID winner = player.getUniqueId() == match.getPlayer1() ? match.getPlayer2() : match.getPlayer1();
            match.win(winner);
        }
    }
    
    @EventHandler
    public void playerquitduel(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if(manager.inDuel(player.getUniqueId()) && event.getTo().distance(DuelsMain.getInstance().duelsconfig.getSpawn()) > 2)
            event.setCancelled(true);
        else if(!manager.inDuel(player.getUniqueId()) && event.getFrom().distance(DuelsMain.getInstance().duelsconfig.getAssistLocation()) < 10){
            DuelMatch match = manager. lookupMatchBySpectatorUuid(player.getUniqueId());
            if(match != null) {
                match.resetEspectator(player);
            }
        }
    }

    @EventHandler
    public void playerdeathduel(PlayerDeathEvent event) {
        
        Player player = event.getEntity();

        if(manager.inDuel(player.getUniqueId())) {
            DuelMatch match = manager.lookupMatchByPlayerUuid(player.getUniqueId(),(as)-> as.getState()==1);
            UUID winner = player.getUniqueId() == match.getPlayer1() ? match.getPlayer2() : match.getPlayer1();
            match.win(winner);
            match.stop(ChatColor.YELLOW + "Duelo terminou.");
        }
    }
}
