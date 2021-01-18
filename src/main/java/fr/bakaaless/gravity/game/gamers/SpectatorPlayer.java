package fr.bakaaless.gravity.game.gamers;

import fr.bakaaless.gravity.game.GravityGame;
import fr.bakaaless.gravity.plugin.GravityPlugin;

import java.util.UUID;

public class SpectatorPlayer extends GravityPlayer {

    public SpectatorPlayer(final UUID uniqueId, final GravityGame game) {
        super(uniqueId, game);
    }

    @Override
    public void join() {
        super.join();
        this.toPlayer().setAllowFlight(true);
        for (final GravityPlayer player : this.getGame().getPlayers()) {
            if (player instanceof Gamer)
                player.toPlayer().hidePlayer(GravityPlugin.get(), this.toPlayer());
            else if (player instanceof SpectatorPlayer) {
                player.toPlayer().showPlayer(GravityPlugin.get(), this.toPlayer());
                this.toPlayer().showPlayer(GravityPlugin.get(), player.toPlayer());
            }
        }
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
    }

    @Override
    public void remove() {
        super.remove();
        this.toPlayer().setAllowFlight(false);
    }

    public static SpectatorPlayer from(final Gamer former) {
        return new SpectatorPlayer(former.getUniqueId(), former.getGame());
    }
}
