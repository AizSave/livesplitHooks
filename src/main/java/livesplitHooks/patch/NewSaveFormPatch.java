package livesplitHooks.patch;

import livesplitHooks.LivesplitHooksEntry;
import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.gfx.forms.components.FormTextInput;
import necesse.gfx.forms.components.localComponents.FormLocalCheckBox;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.presets.NewSaveForm;
import net.bytebuddy.asm.Advice;

public class NewSaveFormPatch {
    @ModConstructorPatch(target = NewSaveForm.class, arguments = {String.class})
    public static class ConstructorPatch {
        @Advice.OnMethodExit
        public static void onExit(@Advice.FieldValue("customSpawn") FormLocalCheckBox customSpawn,
                @Advice.FieldValue("spawnX") FormTextInput spawnX,
                @Advice.FieldValue("spawnY") FormTextInput spawnY,
                @Advice.FieldValue("spawnSeed") FormTextInput spawnSeed,
                @Advice.FieldValue("spawnSeedNew") FormLocalTextButton spawnSeedNew) {
            if (LivesplitHooksEntry.config.useCustomSpawn) {
                spawnX.setActive(true);
                spawnY.setActive(true);
                spawnSeed.setActive(false);
                spawnSeedNew.setActive(false);

                customSpawn.checked = true;
                spawnX.setText(String.valueOf(LivesplitHooksEntry.config.spawnX));
                spawnY.setText(String.valueOf(LivesplitHooksEntry.config.spawnY));
            }
        }
    }
}
