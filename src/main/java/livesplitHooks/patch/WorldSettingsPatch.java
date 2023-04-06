package livesplitHooks.patch;

import livesplitHooks.LivesplitHooksEntry;
import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.engine.world.World;
import necesse.engine.world.WorldSettings;
import net.bytebuddy.asm.Advice;

public class WorldSettingsPatch {
    @ModConstructorPatch(target = WorldSettings.class, arguments = {World.class})
    public static class ConstructorPatch {
        @Advice.OnMethodExit
        public static void onExit(@Advice.This WorldSettings thiz) {
            thiz.difficulty = LivesplitHooksEntry.config.difficulty;
            thiz.deathPenalty = LivesplitHooksEntry.config.deathPenalty;
            thiz.playerHunger = LivesplitHooksEntry.config.playerHunger;
            thiz.raidFrequency = LivesplitHooksEntry.config.raidFrequency;
        }
    }
}
