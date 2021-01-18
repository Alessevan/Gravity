package fr.bakaaless.gravity.game.gamers;

import fr.bakaaless.gravity.game.GravityGame;

import java.util.UUID;

public class SimpleGamer extends Gamer {

    public SimpleGamer(final UUID uniqueId, final GravityGame game) {
        super(uniqueId, game);
    }

    @Override
    public void join() {
        super.join();
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
    }

    @Override
    public void onFail() {
        super.onFail();
    }

    public static SimpleGamer from(final Gamer former) {
        final SimpleGamer gamer = new SimpleGamer(former.getUniqueId(), former.getGame());
        gamer.setMap(former.getMap());
        gamer.setFails(former.getFails());
        gamer.setStarted(former.getStarted());
        gamer.setFinished(former.getFinished());
        return gamer;
    }

}
