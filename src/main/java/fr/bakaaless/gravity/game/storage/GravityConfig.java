package fr.bakaaless.gravity.game.storage;

public class GravityConfig {

    private int maps;
    private int maxFails;
    private long timeMax;
    private long timeAfterWin;

    public int getMaps() {
        return this.maps;
    }

    public void setMaps(final int maps) {
        this.maps = maps;
    }

    public int getMaxFails() {
        return this.maxFails;
    }

    public void setMaxFails(final int maxFails) {
        this.maxFails = maxFails;
    }

    public long getTimeMax() {
        return this.timeMax;
    }

    public void setTimeMax(final long timeMax) {
        this.timeMax = timeMax;
    }

    public long getTimeAfterWin() {
        return this.timeAfterWin;
    }

    public void setTimeAfterWin(final long timeAfterWin) {
        this.timeAfterWin = timeAfterWin;
    }
}
