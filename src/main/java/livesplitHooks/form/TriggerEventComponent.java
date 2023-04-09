package livesplitHooks.form;

import livesplitHooks.event.SplitTriggerEvent;
import necesse.engine.Settings;
import necesse.engine.localization.message.LocalMessage;
import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.FormContentIconButton;
import necesse.gfx.forms.components.FormDropdownSelectionButton;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.ui.ButtonColor;

public class TriggerEventComponent extends FormContentBox {
    public static final String[] allowedMobIDs = new String[] {"evilsprotector", "queenspider",
            "voidwizard", "ancientvulture", "swampguardian", "piratecaptain", "reaper", "cryoqueen",
            "pestwarden", "sageandgrit", "fallenwizard"};
    public int index;

    private FormDropdownSelectionButton<SplitTriggerEvent.Type> typeSelect;
    private FormDropdownSelectionButton<String> dataSelect;
    private Runnable deleteCallback;

    public TriggerEventComponent(SplitTriggerEvent event, int index, int x, int y, int width) {
        super(x, y, width, 20);
        this.index = index;

        typeSelect = addComponent(new FormDropdownSelectionButton<>(4, 0, FormInputSize.SIZE_16,
                ButtonColor.BASE, width / 3 - 4));
        for (SplitTriggerEvent.Type type : SplitTriggerEvent.Type.values()) {
            typeSelect.options.add(type, type.label, () -> type.tooltip);
        }
        typeSelect.setSelected(event.type, event.type.label);

        dataSelect = addComponent(new FormDropdownSelectionButton<>(width / 3 + 4, 0,
                FormInputSize.SIZE_16, ButtonColor.BASE, width * 2 / 3 - 28));
        for (String mobID : allowedMobIDs) {
            dataSelect.options.add(mobID, new LocalMessage("mob", mobID));
        }
        dataSelect.setSelected(event.data, new LocalMessage("mob", event.data));

        FormContentIconButton removeButton = addComponent(new FormContentIconButton(width - 20, 0,
                FormInputSize.SIZE_16, ButtonColor.BASE, Settings.UI.button_escaped_20));
        removeButton.onClicked(e -> {
            if (deleteCallback != null) {
                deleteCallback.run();
            }
        });
    }

    public void onDeleted(Runnable callback) {
        deleteCallback = callback;
    }

    public SplitTriggerEvent getEvent() {
        return new SplitTriggerEvent(typeSelect.getSelected(), dataSelect.getSelected());
    }
}
