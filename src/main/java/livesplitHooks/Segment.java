package livesplitHooks;

import livesplitHooks.event.SplitTriggerEvent;

public class Segment {
    private final SplitTrigger trigger;

    public Segment(SplitTrigger trigger) {
        this.trigger = trigger;
    }

    public boolean processEvent(SplitTriggerEvent event) {
        return trigger.processEvent(event);
    }

    public void reset() {
        trigger.reset();
    }
}
