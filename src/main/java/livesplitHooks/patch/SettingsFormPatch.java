package livesplitHooks.patch;

import livesplitHooks.form.LivesplitConfigButton;
import livesplitHooks.form.LivesplitSettingsForm;
import necesse.engine.GameLog;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormComponent;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.presets.SettingsForm;
import net.bytebuddy.asm.Advice;

public class SettingsFormPatch {
    public static LivesplitSettingsForm livesplitSettings;

    @ModMethodPatch(target = SettingsForm.class, name = "setupMenuForm", arguments = {})
    public static class SetupMenuFormPatch {
        @Advice.OnMethodExit
        public static void onExit(@Advice.This SettingsForm thiz,
                @Advice.FieldValue("mainMenu") Form mainMenu) {
            String backText = new LocalMessage("ui", "backbutton").translate();

            mainMenu.setHeight(mainMenu.getHeight() + 40);
            FormLocalTextButton backButton = null;
            for (FormComponent comp : mainMenu.getComponents()) {
                if (!(comp instanceof FormLocalTextButton)) {
                    continue;
                }
                FormLocalTextButton btn = (FormLocalTextButton) comp;
                if (btn.getText().equals(backText)) {
                    backButton = btn;
                    break;
                }
            }

            if (backButton == null) {
                GameLog.warn.println("No back button found. Cannot inject Livesplit config");
                return;
            }

            livesplitSettings = thiz.addComponent(new LivesplitSettingsForm(thiz));
            mainMenu.addComponent(new LivesplitConfigButton(thiz, livesplitSettings, 4,
                    backButton.getY(), mainMenu.getWidth() - 8));
            backButton.setY(backButton.getY() + 40);
        }
    }

    @ModMethodPatch(target = SettingsForm.class, name = "savePressed", arguments = {})
    public static class SavePressedPatch {
        @Advice.OnMethodEnter
        public static void onEnter(@Advice.This SettingsForm thiz) {
            livesplitSettings.saveOptions();
        }
    }
}
