package fr.bakaaless.gravity.game.storage;

public class GravityConfig {

    private int playerMin;
    private int playerMax;
    private int maps;
    private int maxFails;
    private long timeMax;
    private long timeAfterWin;

    public int getPlayerMin() {
        return this.playerMin;
    }

    public void setPlayerMin(final int playerMin) {
        this.playerMin = playerMin;
    }

    public int getPlayerMax() {
        return this.playerMax;
    }

    public void setPlayerMax(final int playerMax) {
        this.playerMax = playerMax;
    }

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
