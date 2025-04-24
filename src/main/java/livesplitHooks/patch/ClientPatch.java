package livesplitHooks.patch;

import livesplitHooks.LivesplitHooksEntry;
import livesplitHooks.LivesplitServer;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.Client;
import net.bytebuddy.asm.Advice;

public class ClientPatch {
    public static boolean started = false;

    @ModMethodPatch(target = Client.class, name = "start", arguments = {})
    public static class StartPatch {
        @Advice.OnMethodExit
        public static void onExit(@Advice.This Client thiz) {
            started = false;
        }
    }

    @ModMethodPatch(target = Client.class, name = "tick", arguments = {})
    public static class TickPatch {
        @Advice.OnMethodExit
        public static void onExit(@Advice.This Client thiz) {
            System.out.println(started);
            if (!started && thiz.getPlayer() != null) {
                started = true;
                LivesplitHooksEntry.livesplitServer.sendCommand(LivesplitServer.START_TIMER);
            }
        }
    }

}
