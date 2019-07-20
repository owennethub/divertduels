package club.batowen.duels.core.tasks;

import java.util.UUID;

import club.batowen.duels.core.DuelsManager;

public class InviteDuelTask implements Runnable{

    private final UUID idmatch;

    public InviteDuelTask(UUID idmatch){
        this.idmatch=idmatch;
    }

    @Override
    public void run() {
        DuelsManager.getInstance().removeMatch(idmatch);
    }

}