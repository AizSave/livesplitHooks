package livesplitHooks.form;

import livesplitHooks.LivesplitHooksEntry;
import necesse.gfx.forms.components.FormContentIconButton;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.ui.ButtonColor;

public class ConnectLivesplitComponent extends FormContentIconButton {
    public ConnectLivesplitComponent(int x, int y) {
        super(x, y, FormInputSize.SIZE_32, ButtonColor.BASE, LivesplitHooksEntry.getLivesplitIcon(),
                LivesplitHooksEntry.getLivesplitTooltips());
        onClicked(event -> {
            System.out.println("Connecting to LiveSplit");
            LivesplitHooksEntry.livesplitServer.connect();
            setIcon(LivesplitHooksEntry.getLivesplitIcon());
            setTooltips(LivesplitHooksEntry.getLivesplitTooltips());
        });
    }
}
