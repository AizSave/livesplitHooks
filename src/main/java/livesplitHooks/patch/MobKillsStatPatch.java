package livesplitHooks.patch;

import livesplitHooks.LivesplitHooksEntry;
import livesplitHooks.event.SplitTriggerEvent;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.playerStats.stats.MobKillsStat;
import net.bytebuddy.asm.Advice;

public class MobKillsStatPatch {
    @ModMethodPatch(target = MobKillsStat.class, name = "addKill", arguments = {String.class})
    public static class addKillPatch {
        @Advice.OnMethodExit
        public static void onExit(@Advice.Argument(0) String mobID) {
            LivesplitHooksEntry.config.splits.processEvent(SplitTriggerEvent.mobKill(mobID));
        }
    }
}
