package livesplitHooks.split;

import java.util.Arrays;
import java.util.List;
import livesplitHooks.LivesplitHooksEntry;
import livesplitHooks.LivesplitServer;
import livesplitHooks.event.SplitTriggerEvent;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

public class Splits {
    public final Segment[] segments;
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
        for (Segment segment : segments) {
            segment.reset();
        }
        currSegmentIndex = 0;
        LivesplitHooksEntry.livesplitServer.sendCommand(LivesplitServer.RESET);
    }

    private void split() {
        LivesplitHooksEntry.livesplitServer.sendCommand(LivesplitServer.SPLIT);
        currSegmentIndex++;
    }

    public static Splits getDefaultSplits() {
        return new Splits(
                new Segment(SplitTrigger.and(SplitTriggerEvent.mobKill("evilsprotector"),
                        SplitTriggerEvent.mobKill("evilsprotector"))),
                new Segment(SplitTrigger.and(SplitTriggerEvent.mobKill("voidwizard"))),
                new Segment(SplitTrigger.and(SplitTriggerEvent.mobKill("piratecaptain"))),
                new Segment(SplitTrigger.and(SplitTriggerEvent.mobKill("sageandgrit"))),
                new Segment(SplitTrigger.and(SplitTriggerEvent.mobKill("fallenwizard"))));
    }

    public void addSaveData(SaveData save) {
        for (Segment segment : segments) {
            SaveData segmentData = new SaveData("SEGMENT");
            segment.addSaveData(segmentData);
            save.addSaveData(segmentData);
        }
    }

    public static Splits fromLoadData(LoadData loadData) {
        List<LoadData> segmentsData = loadData.getLoadDataByName("SEGMENT");
        int i = 0;
        Segment[] segments = new Segment[segmentsData.size()];
        for (LoadData segmentData : segmentsData) {
            segments[i++] = Segment.fromLoadData(segmentData);
        }
        return new Splits(segments);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Splits)) {
            return false;
        }
        Splits other = (Splits) obj;
        return Arrays.equals(segments, other.segments);
    }
}
