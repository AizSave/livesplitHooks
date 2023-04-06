package livesplitHooks.patch;

import livesplitHooks.LivesplitHooksEntry;
import livesplitHooks.LivesplitServer;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.Client;
import net.bytebuddy.asm.Advice;

public class ClientPatch {
    @ModMethodPatch(target = Client.class, name = "start", arguments = {})
    public static class StartPatch {
        @Advice.OnMethodExit
        public static void onExit() {
            LivesplitHooksEntry.livesplitServer.sendCommand(LivesplitServer.START_TIMER);
        }
    }
}
