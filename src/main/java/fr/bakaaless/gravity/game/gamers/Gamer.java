package fr.bakaaless.gravity.game.gamers;

import fr.bakaaless.gravity.game.GravityGame;
import org.bukkit.Location;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class Gamer extends GravityPlayer {

    private int fails;
    private long started;
    private long finished;

    public Gamer(final UUID uniqueId, final GravityGame game) {
        super(uniqueId, game);
        this.fails = 0;
        this.started = -1L;
        this.finished = -1L;
    }

    public void onFail() {
        this.fails++;
        final List<Location> futureSpawns = this.getGame().getLoadedMaps().get(this.getMap()).getSpawns();
        this.toPlayer().teleport(
                futureSpawns.get(new Random().nextInt(futureSpawns.size()))
        );
    }

    @Override
    public void remove() {
        super.remove();
    }

    public void init() {
        this.started = System.currentTimeMillis();
    }

    public int getFails() {
        return this.fails;
    }

    public void setFails(final int fails) {
        this.fails = fails;
    }

    public long getStarted() {
        return this.started;
    }

    public void setStarted(final long started) {
        this.started = started;
    }

    public long getFinished() {
        return this.finished;
    }

    public void setFinished(final long finished) {
        this.finished = finished;
    }

}
