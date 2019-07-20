package club.batowen.duels.commands;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import club.batowen.DuelsMain;
import club.batowen.duels.core.DuelMatch;
import club.batowen.duels.core.DuelsManager;

public class Commands {

    public static final Consumer<SimpleCommand> acceptduel = (cmd) -> acceptDuel(cmd);
    public static final Consumer<SimpleCommand> assistduel = (cmd) -> assistDuel(cmd);

    private static void assistDuel(SimpleCommand command) {
        final Player player = command.getPlayer();
        
        Player target = Bukkit.getPlayer(command.getArguments()[0]);

        if(target == null){
            player.sendMessage("§cEsse jogador esta offline.");
            return;
        }
        DuelsManager manager = DuelsManager.getInstance();

        if(!manager.inDuel(target.getUniqueId())) {
            player.sendMessage("§cEsse jogador nao esta duelando.");
            return;
        }
        DuelMatch match = manager.lookupMatchByPlayerUuid(target.getUniqueId());
        if(match.getState() !=1) {
            player.sendMessage("§cEsse jogador nao esta duelando.");
            return;
        }
        match.addSpectator(player);
        player.teleport(DuelsMain.getInstance().duelsconfig.getAssistLocation());
    
    }
    private static void acceptDuel(SimpleCommand command) {
        final Player player = command.getPlayer();
        final DuelsManager manager = DuelsManager.getInstance();

        DuelMatch match = manager.lookupMatchByPlayerUuid(player.getUniqueId());

        if(match == null) {
            
        }
    }

    public static final class SimpleCommand {

        private final Player player;
        private final String[] arguments;

        public SimpleCommand(Player player, String[] args) {
            this.player = player;
            this.arguments = args;
        }
        /**
         * @return the arguments
         */
        public String[] getArguments() {
            return arguments;
        }

        /**
         * @return the player
         */
        public Player getPlayer() {
            return player;
        }
    }
}