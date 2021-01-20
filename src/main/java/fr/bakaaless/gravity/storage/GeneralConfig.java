package fr.bakaaless.gravity.storage;

public class GeneralConfig {

    private static GeneralConfig instance;

    public static GeneralConfig get() {
        if (instance == null)
            instance = new GeneralConfig();
        return instance;
    }

    private boolean bungee = false;

    private GeneralConfig() {
    }

    public void init() {

    }

    public boolean isBungee() {
        return this.bungee;
    }
}
