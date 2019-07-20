package club.batowen.duels.commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import club.batowen.DuelsMain;
import club.batowen.duels.core.DuelsManager;
import club.batowen.duels.others.PrettyMessage;

public class DuelCommand implements CommandExecutor {

    private final DuelsManager manager = DuelsManager.getInstance();
    private final DuelsMain main = DuelsMain.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arguments) {
        
        if(sender instanceof Player) {
            if(arguments.length < 2) {
                sender.sendMessage("§fPara começar um duelo use §b/duelo <jogador> <recompensa>");
                sender.sendMessage("§fPara aceitar um duelo use §b/duelo <jogador> aceitar");
                sender.sendMessage("§fPara assistir um duelo use §b/duelo <jogador> assistir");
            }
            else {
                Player player1 = (Player)sender;

                if(manager.inDuel(player1.getUniqueId())){
                    sender.sendMessage("§cTermine seu duelo para usar este comando");
                    return false;
                }
                if(!NumberUtils.isNumber(arguments[1])){
                    if(arguments[1].equalsIgnoreCase("aceitar"))
                        Commands.acceptduel.accept(new Commands.SimpleCommand(player1, arguments));
                    else if(arguments[1].equalsIgnoreCase("assistir")) 
                        Commands.assistduel.accept(new Commands.SimpleCommand(player1, arguments));
                    else {
                        sender.sendMessage("§fPara começar um duelo use §b/duelo <jogador> <recompensa>");
                        sender.sendMessage("§fPara aceitar um duelo use §b/duelo <jogador> aceitar");
                        sender.sendMessage("§fPara assistir um duelo use §b/duelo <jogador> assistir");
                    }
                        
                } else {
                    double money = Double.parseDouble(arguments[1]);
                    Player player2 = Bukkit.getPlayer(arguments[0]);
    
                    if(player2 == null){
                        sender.sendMessage("§c§nO jogador escolhido está ofline!");
                        return false;
                    }
                    if(manager.inDuel(player2.getUniqueId())){
                        sender.sendMessage("§c§nO jogador escolhido está em um duelo!");
                        return false;
                    }
                    if(main.economy.getBalance(player2) < money){
                        sender.sendMessage("§c§nA recompensa é maior que seu dinheiro atual.");
                        return false;
                    }

                    PrettyMessage invitemsg = new PrettyMessage(5);
                    invitemsg.set(0, "");
                    invitemsg.set(1, invitemsg.centralize("§6§DUELO DE PVP"));
                    invitemsg.set(2, "");
                    invitemsg.set(3, "§fO jogador §b"+player1.getName()+" §fconvidou §b"+player2.getName()+" §fpara um duelo.");
                    invitemsg.set(4, "§fValendo: §e"+DuelsMain.getInstance().economy.format(money)+"§f coins.");
                    invitemsg.set(5, "");
                    invitemsg.set(6, invitemsg.addClickableText("§FPara assistir o duelo §e§l","CLIQUE AQUI","/duelo "+player1.getName()+" assistir"));
                    invitemsg.broadcast();

                    new PrettyMessage(invitemsg.addClickableText("§fPara §b§lACEITAR§f o duelo §e§l","CLIQUE AQUI", "/duelo "+player1.getName()+" aceitar")).sendMessage(player2);
                    DuelsManager.getInstance().addDuel(player1.getUniqueId(), player2.getUniqueId(), money);
                }
            }
        } else
            sender.sendMessage("Console cannot execute this command");
        
        return false;
	}

}