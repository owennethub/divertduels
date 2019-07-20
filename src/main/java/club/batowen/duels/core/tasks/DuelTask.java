package club.batowen.duels.core.tasks;


import org.bukkit.Bukkit;

import club.batowen.DuelsMain;
import club.batowen.duels.core.DuelMatch;

public class DuelTask implements Runnable{

    private final DuelMatch match;

    public DuelTask(DuelMatch match){
        this.match=match;
    }

	@Override
    public void run() {
        
        int time = match.getTime();

        if(!match.finish()) {
            if(time >=60 && time % 60 == 0) {
                Bukkit.getPlayer(match.getPlayer1()).sendMessage("§c§lDUELO §ftermina em "+(time/60) + "minutos");
                Bukkit.getPlayer(match.getPlayer2()).sendMessage("§c§lDUELO §ftermina em "+(time/60) + "minutos");
            } else if (time == 30 || time == 15 || time == 10 || time < 6) {
                Bukkit.getPlayer(match.getPlayer1()).sendMessage("§c§lDUELO §ftermina em "+ time + "segundos");
                Bukkit.getPlayer(match.getPlayer2()).sendMessage("§c§lDUELO §ftermina em "+ time + "segundos");
            } else if(time > 297) {
                Bukkit.getPlayer(match.getPlayer1()).teleport(DuelsMain.getInstance().duelsconfig.getLocation1());
                Bukkit.getPlayer(match.getPlayer2()).teleport(DuelsMain.getInstance().duelsconfig.getLocation2());
            }
            match.decreaseTime();
        } else
            match.stop(DuelsMain.getInstance().duelsconfig.matchcancelled);
    }
}