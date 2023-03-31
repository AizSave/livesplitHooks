package livesplitHooks;

import livesplitHooks.event.SplitTriggerEvent;

public class Splits {
    private final Segment[] segments;
    private int currSegmentIndex;

    public Splits(Segment... segments) {
        this.segments = segments;
        currSegmentIndex = 0;
    }

    public void processEvent(SplitTriggerEvent event) {
        if (currSegmentIndex >= segments.length) {
            return;
        }
        if (segments[currSegmentIndex].processEvent(event)) {
            split();
        }
    }

    public void reset() {
        System.out.println("Resetting things");
        for (Segment segment : segments) {
            segment.reset();
        }
        currSegmentIndex = 0;
        LivesplitHooksEntry.livesplitServer.sendCommand("reset");
    }

    private void split() {
        LivesplitHooksEntry.livesplitServer.sendCommand("split");
        currSegmentIndex++;
    }
}
