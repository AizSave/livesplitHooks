package livesplitHooks.split;

import java.util.List;
import livesplitHooks.event.SplitTriggerEvent;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

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

    public void addSaveData(SaveData save) {
        save.addEnum("op", op);
        for (SplitTriggerEvent event : events) {
            SaveData eventData = new SaveData("EVENT");
            event.addSaveData(eventData);
            save.addSaveData(eventData);
        }
    }

    public static SplitTrigger fromLoadData(LoadData save) {
        Operation op = save.getEnum(Operation.class, "op");
        List<LoadData> eventsData = save.getLoadDataByName("EVENT");
        SplitTriggerEvent[] events = new SplitTriggerEvent[eventsData.size()];
        int i = 0;
        for (LoadData eventData : eventsData) {
            events[i++] = SplitTriggerEvent.fromLoadData(eventData);
        }
        return new SplitTrigger(op, events);
    }
}
