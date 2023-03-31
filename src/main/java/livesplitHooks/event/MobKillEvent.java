package livesplitHooks.event;

public class MobKillEvent extends SplitTriggerEvent {
    private final String mobID;

    public MobKillEvent(String mobId) {
        this.mobID = mobId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MobKillEvent)) {
            return false;
        }
        return mobID.equals(((MobKillEvent) obj).mobID);
    }
}
