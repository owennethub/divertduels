package club.batowen.duels.others;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

/**
 * 
 * A great and simple API Message includes JSON and others.
 * 
 */
public class PrettyMessage {

    private final TextComponent[] messages;

    public PrettyMessage(String message) {
        this.messages = new TextComponent[] {new TextComponent(message)};
    }
    public PrettyMessage(TextComponent message) {
        this.messages = new TextComponent[] {message};
    }
    public PrettyMessage(int size) {
        this.messages = new TextComponent[size];
    }

    public TextComponent addClickableText(String prefix, String text, String command) {
        TextComponent txtcprefix = new TextComponent(prefix);
        TextComponent clickablemsg = new TextComponent(text);
        clickablemsg.setUnderlined(true);
        clickablemsg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
        txtcprefix.addExtra(clickablemsg);
        return txtcprefix;
    }

    public void set(int pos, String msg) {
        this.messages[pos] = new TextComponent(msg);
    }
    public void set(int pos, TextComponent msg) {
        this.messages[pos] = msg;
    }

    public String centralize(String msg){
        int center = 37 - msg.length();
        for(int i = 0; i <=center; i++){
            msg = " " + msg;
        }
        return msg;
    }

    public void broadcast() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            for (TextComponent var : messages) {
                player.spigot().sendMessage(var);
            }
        }
    }

    public void sendMessage(Player player) {
        for (TextComponent var : messages) {
            player.spigot().sendMessage(var);
        }
    }
} 