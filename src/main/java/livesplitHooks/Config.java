package livesplitHooks;

import livesplitHooks.split.Splits;
import necesse.engine.GameDeathPenalty;
import necesse.engine.GameDifficulty;
import necesse.engine.GameRaidFrequency;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

public class Config extends ModSettings {
    public int livesplitPort;
    public Splits splits;

    public boolean useCustomSpawn;
    public int spawnX;
    public int spawnY;

    public GameDifficulty difficulty;
    public GameDeathPenalty deathPenalty;
    public boolean playerHunger;
    public GameRaidFrequency raidFrequency;

    public Config() {
        livesplitPort = 16834;
        splits = Splits.getDefaultSplits();

        useCustomSpawn = false;
        spawnX = 0;
        spawnY = 0;

        difficulty = GameDifficulty.CASUAL;
        deathPenalty = GameDeathPenalty.NONE;
        playerHunger = false;
        raidFrequency = GameRaidFrequency.NEVER;
    }

    @Override
    public void applyLoadData(LoadData save) {
        livesplitPort = save.getInt("livesplitport");
        splits = Splits.fromLoadData(save);
        useCustomSpawn = save.getBoolean("usecustomspawn");
        spawnX = save.getInt("spawnx");
        spawnY = save.getInt("spawny");
        difficulty = save.getEnum(GameDifficulty.class, "difficulty");
        deathPenalty = save.getEnum(GameDeathPenalty.class, "deathpenalty");
        playerHunger = save.getBoolean("playerhunger");
        raidFrequency = save.getEnum(GameRaidFrequency.class, "raidfrequency");
    }

    @Override
    public void addSaveData(SaveData save) {
        save.addInt("livesplitport", livesplitPort);
        splits.addSaveData(save);
        save.addBoolean("usecustomspawn", useCustomSpawn);
        save.addInt("spawnx", spawnX);
        save.addInt("spawny", spawnY);
        save.addEnum("difficulty", difficulty);
        save.addEnum("deathpenalty", deathPenalty);
        save.addBoolean("playerhunger", playerHunger);
        save.addEnum("raidfrequency", raidFrequency);
    }
}
