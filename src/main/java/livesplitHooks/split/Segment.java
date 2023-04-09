package livesplitHooks.split;

import livesplitHooks.event.SplitTriggerEvent;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

public class Segment {
    public final SplitTrigger trigger;

    public Segment(SplitTrigger trigger) {
        this.trigger = trigger;
    }

    public Segment() {
        this(SplitTrigger.and(SplitTriggerEvent.mobKill("evilsprotector")));
    }

    public boolean processEvent(SplitTriggerEvent event) {
        return trigger.processEvent(event);
    }

    public void reset() {
        trigger.reset();
    }

    public void addSaveData(SaveData save) {
        SaveData triggerData = new SaveData("TRIGGER");
        trigger.addSaveData(triggerData);
        save.addSaveData(triggerData);
    }

    public static Segment fromLoadData(LoadData save) {
        LoadData triggerData = save.getFirstLoadDataByName("TRIGGER");
        SplitTrigger trigger = SplitTrigger.fromLoadData(triggerData);
        return new Segment(trigger);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Segment)) {
            return false;
        }
        return trigger.equals(((Segment) obj).trigger);
    }
}
