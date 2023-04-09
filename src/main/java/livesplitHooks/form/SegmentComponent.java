package livesplitHooks.form;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import livesplitHooks.LivesplitHooksEntry;
import livesplitHooks.event.SplitTriggerEvent;
import livesplitHooks.split.Segment;
import livesplitHooks.split.SplitTrigger;
import necesse.engine.Settings;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.FormContentIconButton;
import necesse.gfx.forms.components.FormDropdownSelectionButton;
import necesse.gfx.forms.components.FormIconButton;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.events.FormEventListener;
import necesse.gfx.forms.events.FormResizeEvent;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;

public class SegmentComponent extends FormContentBox {
    private Form form;
    private FormContentIconButton addEventButton;
    public int index;

    private List<TriggerEventComponent> triggerEvents;
    private FormDropdownSelectionButton<SplitTrigger.Operation> opSelect;

    private List<FormEventListener<FormResizeEvent<SegmentComponent>>> resizeListeners;
    private List<Consumer<Integer>> moveUpListeners;
    private List<Consumer<Integer>> moveDownListeners;
    private List<Consumer<Integer>> deleteListeners;

    public SegmentComponent(Segment segment, int index, int x, int y, int width) {
        super(x, y, width, 0);
        setPosition(x, y);
        this.index = index;
        resizeListeners = new ArrayList<>();
        moveUpListeners = new ArrayList<>();
        moveDownListeners = new ArrayList<>();
        deleteListeners = new ArrayList<>();

        form = addComponent(new Form(width - 56, 44));
        form.setPosition(28, 4);

        form.addComponent(new FormLocalLabel("ui", "triggercriteria", new FontOptions(14),
                FormLocalLabel.ALIGN_LEFT, 4, 4));
        opSelect = form.addComponent(new FormDropdownSelectionButton<>(form.getWidth() / 2 + 4, 4,
                FormInputSize.SIZE_16, ButtonColor.BASE, 80));
        for (SplitTrigger.Operation op : SplitTrigger.Operation.values()) {
            opSelect.options.add(op, op.label, () -> op.tooltip);
        }
        opSelect.setSelected(segment.trigger.op, segment.trigger.op.label);

        addEventButton = form.addComponent(new FormContentIconButton(4, 0, FormInputSize.SIZE_16,
                ButtonColor.BASE, LivesplitHooksEntry.button_add_20));
        addEventButton.onClicked(e -> addEvent(SplitTriggerEvent.mobKill("evilsprotector")));
        triggerEvents = new ArrayList<>();
        for (SplitTriggerEvent event : segment.trigger.events) {
            addEvent(event);
        }

        addComponent(new FormIconButton(4, 4, Settings.UI.button_moveup))
                .onClicked(e -> notifyUpListeners());
        addComponent(new FormIconButton(4, 20, Settings.UI.button_movedown))
                .onClicked(e -> notifyDownListeners());
        addComponent(new FormContentIconButton(getWidth() - 20, 4, FormInputSize.SIZE_16,
                ButtonColor.BASE, Settings.UI.button_escaped_20))
                        .onClicked(e -> notifyDeleteListeners());
    }

    private void addEvent(SplitTriggerEvent event) {
        int y = 24 + triggerEvents.size() * 20;
        TriggerEventComponent triggerEvent = form.addComponent(
                new TriggerEventComponent(event, triggerEvents.size(), 0, y, form.getWidth()));
        triggerEvent.onDeleted(() -> {
            removeEvent(triggerEvent.index);
        });
        triggerEvents.add(triggerEvent);
        updateHeight(form.getHeight() + triggerEvent.getHeight());
    }

    private void removeEvent(int index) {
        TriggerEventComponent triggerEvent = triggerEvents.get(index);
        int compHeight = triggerEvent.getHeight();
        form.removeComponent(triggerEvent);
        triggerEvents.remove(triggerEvent);
        for (TriggerEventComponent eventComp : triggerEvents) {
            if (eventComp.getY() < triggerEvent.getY()) {
                continue;
            }
            eventComp.setY(eventComp.getY() - compHeight);
            eventComp.index--;
        }
        updateHeight(form.getHeight() - compHeight);
    }

    private void updateHeight(int height) {
        form.setHeight(height);
        setHeight(form.getHeight() + 8);
        addEventButton.setPosition(addEventButton.getX(), form.getHeight() - 20);
        setContentBox(new Rectangle(getWidth(), getHeight()));
        notifyResizeListeners();
    }

    public void onResized(FormEventListener<FormResizeEvent<SegmentComponent>> listener) {
        resizeListeners.add(listener);
    }

    public void onMoveUpClicked(Consumer<Integer> listener) {
        moveUpListeners.add(listener);
    }

    public void onMoveDownClicked(Consumer<Integer> listener) {
        moveDownListeners.add(listener);
    }

    public void onDeleted(Consumer<Integer> listener) {
        deleteListeners.add(listener);
    }

    private void notifyResizeListeners() {
        FormResizeEvent<SegmentComponent> event =
                new FormResizeEvent<>(this, null, getX(), getY(), getWidth(), getHeight());
        resizeListeners.forEach(l -> l.onEvent(event));
    }

    private void notifyUpListeners() {
        moveUpListeners.forEach(l -> l.accept(index));
    }

    private void notifyDownListeners() {
        moveDownListeners.forEach(l -> l.accept(index));
    }

    private void notifyDeleteListeners() {
        deleteListeners.forEach(l -> l.accept(index));
    }

    public Segment getSegment() {
        SplitTriggerEvent[] events = triggerEvents.stream().map(TriggerEventComponent::getEvent)
                .filter(e -> e != null).toArray(SplitTriggerEvent[]::new);
        if (events.length == 0) {
            return null;
        }
        return new Segment(new SplitTrigger(opSelect.getSelected(), events));
    }
}
