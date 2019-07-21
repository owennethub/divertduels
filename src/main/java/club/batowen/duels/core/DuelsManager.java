package club.batowen.duels.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Predicate;

public class DuelsManager {

    private static final DuelsManager instance = new DuelsManager();

    private final HashMap<UUID, DuelMatch> matchholder = new HashMap<>();
    private final HashSet<UUID> induel = new HashSet<>();

    /**
     * @return the instance
     */
    public static DuelsManager getInstance() {
        return instance;
    }
    

    public boolean inDuel(UUID uuid) {
        return induel.contains(uuid);
    }

    public void stopAllDuels(){
        DuelsManager.getInstance().matchholder.values().forEach((match) -> match.stop("plugin disabling.."));
    }
    public void addDuel(UUID player1, UUID player2, double money) {
        DuelMatch duelmatch = new DuelMatch(generateIDMatch(player1, player2), player1, player2, money);
        duelmatch.startInviteTask();
        matchholder.put(duelmatch.getIdmatch(), duelmatch);
    }

    public void addInduelPlayer(UUID uuid) {
        induel.add(uuid);
    }
    public void removeMatch(DuelMatch match) {

        if(induel.contains(match.getPlayer1())) {
            induel.remove(match.getPlayer1());
            induel.remove(match.getPlayer2());
        }
        matchholder.remove(match.getIdmatch());
    }

    public void removeMatch(UUID idmatch) {
        matchholder.remove(idmatch);
    }

    public UUID generateIDMatch(UUID uuid1, UUID uuid2) {
        return null;
    }

    public DuelMatch lookupMatchByPlayerUuid(UUID uuid, Predicate<? super DuelMatch> condition) {
        return matchholder.values().stream().filter((match) -> match.getPlayer1() == uuid || match.getPlayer2() == uuid).filter(condition).findFirst().get();
    }
    
    public DuelMatch lookupMatchBySpectatorUuid(UUID uuid) {
        return matchholder.values().stream().filter((match) -> match.getAudience().contains(uuid)).findFirst().get();
    }


    private Predicate<? super DuelMatch> verifyuuid(UUID uuid) {
        return (match) -> match.getPlayer1() == uuid || match.getPlayer2() == uuid;
    }
}