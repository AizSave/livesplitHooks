package livesplitHooks.form;

import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.presets.SettingsForm;

public class LivesplitConfigButton extends FormLocalTextButton {

    public LivesplitConfigButton(SettingsForm settings, LivesplitSettingsForm livesplitSettings,
            int x, int y, int width) {
        super("ui", "livesplitconfigbutton", x, y, width);

        onClicked(e -> {
            livesplitSettings.resetOptions();
            settings.makeCurrent(livesplitSettings);
        });
    }
}
