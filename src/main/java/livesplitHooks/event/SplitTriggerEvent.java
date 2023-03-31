package livesplitHooks.event;

public abstract class SplitTriggerEvent {
    private boolean matched;

    public SplitTriggerEvent() {
        matched = false;
    }

    public boolean match(SplitTriggerEvent event) {
        if (matched) {
            return false;
        }
        if (equals(event)) {
            matched = true;
            return true;
        }
        return false;
    }

    public boolean isMatched() {
        return matched;
    }

    public void reset() {
        matched = false;
    }
}
