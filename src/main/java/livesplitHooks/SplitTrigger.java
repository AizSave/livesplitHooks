package livesplitHooks;

import livesplitHooks.event.SplitTriggerEvent;

public class SplitTrigger {
    private static enum Operation {
        AND, OR
    }

    private final Operation op;
    private final SplitTriggerEvent[] events;

    private SplitTrigger(Operation op, SplitTriggerEvent[] events) {
        this.op = op;
        this.events = events;
    }

    public static SplitTrigger and(SplitTriggerEvent... events) {
        return new SplitTrigger(Operation.AND, events);
    }

    public static SplitTrigger or(SplitTriggerEvent... events) {
        return new SplitTrigger(Operation.OR, events);
    }

    public boolean processEvent(SplitTriggerEvent inEvent) {
        boolean result = op == Operation.AND;

        boolean used = false;
        for (SplitTriggerEvent event : events) {
            if (!used && event.match(inEvent)) {
                used = true;
            }
            if (op == Operation.AND) {
                result = result && event.isMatched();
            } else {
                result = result || event.isMatched();
            }
        }

        return result;
    }

    public void reset() {
        for (SplitTriggerEvent event : events) {
            event.reset();
        }
    }
}
