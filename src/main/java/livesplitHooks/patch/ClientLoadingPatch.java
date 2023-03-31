package livesplitHooks.patch;

import livesplitHooks.LivesplitHooksEntry;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.loading.ClientLoading;
import net.bytebuddy.asm.Advice;

public class ClientLoadingPatch {
    @ModMethodPatch(target = ClientLoading.class, name = "tick", arguments = {})
    public static class TickPatch {
        public static boolean isLoading;

        @Advice.OnMethodExit
        public static void onExit(@Advice.This ClientLoading thiz) {
            if (isLoading == thiz.isDone()) {
                isLoading = !isLoading;
                LivesplitHooksEntry.setLoading(isLoading);
            }
        }
    }
}
