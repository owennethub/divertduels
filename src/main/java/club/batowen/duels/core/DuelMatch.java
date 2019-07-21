package club.batowen.duels.core;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import club.batowen.DuelsMain;
import club.batowen.duels.DuelsConfig;
import club.batowen.duels.core.tasks.DuelTask;
import club.batowen.duels.core.tasks.InviteDuelTask;


/**
 * DuelMatch object
 * 1 vs 1
 * State 
 * 0 = Invite 
 * 1 = INDUEL
 */

public class DuelMatch {


    private final UUID player1,player2,idmatch;
    private double reward;
    private int time = 300, state = 0;
    private BukkitTask dueltask, invitetask;
    private HashSet<UUID> audience;

    public DuelMatch(UUID idmatch, UUID player1, UUID player2, double reward){
        this.idmatch = idmatch;
        this.player1 = player1;
        this.player2 = player2;
        this.reward = reward;
        this.audience = new HashSet<>();
    }

    /**
     * @return the player1
     */
    public UUID getPlayer1() {
        return player1;
    }

    /**
     * @return the player2
     */
    public UUID getPlayer2() {
        return player2;
    }

    /**
     * @return the reward
     */
    public double getreward() {
        return reward;
    }

    /**
     * @return the idmatch
     */
    public UUID getIdmatch() {
        return idmatch;
    }

    public boolean finish(){
        return time == 0;
    }

    /**
     * @return the time
     */
    public int getTime() {
        return time;
    }

    public void decreaseTime(){
        time--;
    }

    /**
     * @return the audience
     */
    public HashSet<UUID> getAudience() {
        return audience;
    }
    /**
     * @return the state
     */
    public int getState() {
        return state;
    }
    
    public boolean compareUUIDS(UUID uuid1, UUID uuid2) {
        if(uuid1 == this.player1 || uuid1 == this.player2){
            return uuid2 == this.player2 || uuid2 == this.player2;
        }
        return false;
    }
    /**
     * @return the invitetask
     */
    public void startInviteTask() {
        invitetask = Bukkit.getScheduler().runTaskLaterAsynchronously(DuelsMain.getInstance(), new InviteDuelTask(getIdmatch()), 180000L);
    }
    public void start(Player player1, Player player2) {
        DuelsConfig config = DuelsMain.getInstance().duelsconfig;

        player1.teleport(config.getLocation1());
        player2.teleport(config.getLocation2()); 
        player1.sendMessage(ChatColor.YELLOW + "Duelo termina em 5 minutos");
        player2.sendMessage(ChatColor.YELLOW + "Duelo termina em 5 minutos");

        for(Player all : Bukkit.getOnlinePlayers()) {
            if(all.getUniqueId() != getPlayer1() && all.getUniqueId() != getPlayer2()){
                player1.hidePlayer(all);
                player2.hidePlayer(all);
            }
        }

        state = 1;
        invitetask.cancel();
        dueltask =  Bukkit.getScheduler().runTaskTimerAsynchronously(DuelsMain.getInstance(), new DuelTask(this), 0L, 20L);
    }

    public void stop(String cause) {

        DuelsConfig config = DuelsMain.getInstance().duelsconfig;
        final Location spawn = config.getSpawn();

        Player player1 = Bukkit.getPlayer(getPlayer1());
        Player player2 = Bukkit.getPlayer(getPlayer2());

        if(player1 != null){
            player1.teleport(spawn);
            player1.sendMessage(cause);
        }
        if(player2 != null){
            player2.teleport(spawn);
            player2.sendMessage(cause);
            
        }
        for(Player all : Bukkit.getOnlinePlayers()) {
           if(player1 != null)
                player1.showPlayer(all);
            if(player2 != null)
                player2.showPlayer(all);
        }
        audience.forEach((uuid) -> resetEspectator(Bukkit.getPlayer(uuid)));
        audience.clear();
        dueltask.cancel();
        DuelsManager.getInstance().removeMatch(this);
    }

    public void win(UUID winner) {

        final Player player = Bukkit.getPlayer(winner);
        if(winner == player2){
            DuelsMain.getInstance().economy.bankDeposit(player.getName(), reward);
            player.sendMessage("§aParabens ganhaste o duelo e recebeste "+reward+"!");
        } else
            player.sendMessage("§aParabens ganhaste o duelo!");

    }

    public void addSpectator(Player player) {
        audience.add(player.getUniqueId());
        player.teleport(DuelsMain.getInstance().duelsconfig.getAssistLocation());

        for(Player all : Bukkit.getOnlinePlayers()) {
            if(all.getUniqueId() != getPlayer1() && all.getUniqueId() != getPlayer2()){
                player.hidePlayer(all);
            }
        }
    }

    public void resetEspectator(Player player) {

        if(player == null)
            return;

        for(Player all : Bukkit.getOnlinePlayers()) {
            player.showPlayer(all);
        }
        audience.remove(player.getUniqueId());
    }
}

