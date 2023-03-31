package livesplitHooks.patch;

import livesplitHooks.LivesplitHooksEntry;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.gfx.forms.FormSwitcherTyped;
import necesse.gfx.forms.components.FormComponent;
import necesse.gfx.forms.presets.NewSaveForm;
import net.bytebuddy.asm.Advice;

public class FormSwitcherTypedPatch {
    @ModMethodPatch(target = FormSwitcherTyped.class, name = "makeCurrent",
            arguments = {FormComponent.class})
    public static class MakeCurrentPatch {
        @Advice.OnMethodExit
        public static void onExit(@Advice.Argument(0) FormComponent comp) {
            if (comp instanceof NewSaveForm) {
                LivesplitHooksEntry.splits.reset();
            }
        }
    }
}
