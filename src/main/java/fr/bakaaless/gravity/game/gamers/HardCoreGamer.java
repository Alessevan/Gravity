package fr.bakaaless.gravity.game.gamers;

import fr.bakaaless.gravity.game.GravityGame;

import java.util.UUID;

public class HardCoreGamer extends Gamer {

    public HardCoreGamer(final UUID uniqueId, final GravityGame game) {
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
        if (this.getFails() > this.getGame().getConfig().getMaxFails()) {
            this.getGame().getPlayers().remove(this);
            final SpectatorPlayer player = SpectatorPlayer.from(this);
            player.join();
            this.getGame().getPlayers().add(player);
        }
    }

    public static HardCoreGamer from(final Gamer former) {
        final HardCoreGamer gamer = new HardCoreGamer(former.getUniqueId(), former.getGame());
        gamer.setMap(former.getMap());
        gamer.setFails(former.getFails());
        gamer.setStarted(former.getStarted());
        gamer.setFinished(former.getFinished());
        return gamer;
    }
}
