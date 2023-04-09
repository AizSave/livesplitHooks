package livesplitHooks.form;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import livesplitHooks.split.Segment;
import livesplitHooks.split.Splits;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.events.FormResizeEvent;

public class SplitsComponent extends Form {
    private List<SegmentComponent> segments;

    private FormContentBox segmentContent;
    private FormLocalTextButton addSegment;
    private FormLocalTextButton clearSegments;

    private int height;

    public SplitsComponent(Splits splits, int x, int y, int width) {
        super(width, 0);
        setPosition(x, y);

        segments = new ArrayList<>();

        segmentContent = addComponent(new FormContentBox(0, 0, width, 0));
        addSegment = addComponent(new FormLocalTextButton("ui", "addsegment", 4, 0, width / 2 - 6));
        addSegment.onClicked(e -> addSegment(new Segment()));
        clearSegments = addComponent(
                new FormLocalTextButton("ui", "clearsegments", width / 2 + 2, 0, width / 2 - 6));
        clearSegments.onClicked(e -> {
            clearSegments();
            updateHeight();
        });

        setSplits(splits);
    }

    private void addSegment(Segment segment) {
        SegmentComponent segmentComponent =
                segmentContent.addComponent(new SegmentComponent(segment, segments.size(), 0,
                        height, segmentContent.getWidth()));
        segmentComponent.onResized(e -> updateSegments());
        segmentComponent.onMoveUpClicked(this::moveSegmentUp);
        segmentComponent.onMoveDownClicked(this::moveSegmentDown);
        segmentComponent.onDeleted(this::removeSegment);
        segments.add(segmentComponent);
        height += segmentComponent.getHeight();
        updateHeight();
    }

    private void removeSegment(int i) {
        SegmentComponent segment = segments.remove(i);
        segmentContent.removeComponent(segment);
        updateSegments();
    }

    private void clearSegments() {
        segmentContent.clearComponents();
        segments.clear();
        height = 0;
    }

    private void updateSegments() {
        height = 0;
        int i = 0;
        for (SegmentComponent segment : segments) {
            segment.setY(height);
            segment.index = i++;
            height += segment.getHeight();
        }
        updateHeight();
    }

    private void moveSegmentUp(int i) {
        if (i <= 0) {
            return;
        }

        swapSegments(i, i - 1);
    }

    private void moveSegmentDown(int i) {
        if (i >= segments.size() - 1) {
            return;
        }

        swapSegments(i, i + 1);
    }

    private void swapSegments(int i1, int i2) {
        SegmentComponent first = segments.get(i1);
        SegmentComponent second = segments.set(i2, first);
        segments.set(i1, second);
        updateSegments();
    }

    private void updateHeight() {
        setHeight(height + 40);
        segmentContent.setHeight(height);
        Rectangle rect = new Rectangle(getWidth(), height - 4);
        segmentContent.setContentBox(rect);
        addSegment.setPosition(addSegment.getX(), height);
        clearSegments.setPosition(clearSegments.getX(), height);
        resizeEvents.onEvent(
                new FormResizeEvent<>(this, null, getX(), getY(), getWidth(), getHeight()));
    }

    public void setSplits(Splits splits) {
        clearSegments();
        for (Segment segment : splits.segments) {
            addSegment(segment);
        }
        updateHeight();
    }

    public Splits getSplits() {
        Segment[] segmentArray = segments.stream().map(SegmentComponent::getSegment)
                .filter(s -> s != null).toArray(Segment[]::new);
        return new Splits(segmentArray);
    }
}
