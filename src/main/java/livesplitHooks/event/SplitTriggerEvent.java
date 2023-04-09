package livesplitHooks.event;

import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

public class SplitTriggerEvent {
    public static enum Type {
        MOB_KILL("mobkill");

        public final GameMessage label;
        public final GameMessage tooltip;

        private Type(String typeStr) {
            label = new LocalMessage("ui", "event" + typeStr);
            tooltip = new LocalMessage("ui", "event" + typeStr + "tip");
        }
    }

    public final Type type;
    public final String data;
    private boolean matched;

    public SplitTriggerEvent(Type type, String data) {
        this.type = type;
        this.data = data;
        matched = false;
    }

    public static SplitTriggerEvent mobKill(String mobID) {
        return new SplitTriggerEvent(Type.MOB_KILL, mobID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SplitTriggerEvent)) {
            return false;
        }
        SplitTriggerEvent other = (SplitTriggerEvent) obj;
        return type == other.type && data == null ? other.data == null : data.equals(other.data);
    }

    public boolean match(SplitTriggerEvent event) {
        if (matched) {
            return false;
        }
        matched = equals(event);
        return matched;
    }

    public boolean isMatched() {
        return matched;
    }

    public void reset() {
        matched = false;
    }

    public void addSaveData(SaveData save) {
        save.addEnum("type", type);
        save.addSafeString("data", data);
    }

    public static SplitTriggerEvent fromLoadData(LoadData save) {
        Type type = save.getEnum(Type.class, "type");
        String data = save.getSafeString("data");
        return new SplitTriggerEvent(type, data);
    }
}
