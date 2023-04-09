package livesplitHooks.form;

import java.awt.Rectangle;
import livesplitHooks.Config;
import livesplitHooks.LivesplitHooksEntry;
import necesse.engine.GameDeathPenalty;
import necesse.engine.GameDifficulty;
import necesse.engine.GameRaidFrequency;
import necesse.engine.Screen;
import necesse.engine.Settings;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.tickManager.TickManager;
import necesse.engine.util.GameMath;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormCheckBox;
import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.FormDropdownSelectionButton;
import necesse.gfx.forms.components.FormFlow;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.FormTextInput;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.presets.SettingsForm;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;

public class LivesplitSettingsForm extends Form {
    private SettingsForm settingsForm;
    private FormContentBox content;
    private int preferredHeight;

    private FormTextInput portInput;
    private FormDropdownSelectionButton<GameDifficulty> difficulty;
    private FormDropdownSelectionButton<GameDeathPenalty> deathPenalty;
    private FormDropdownSelectionButton<GameRaidFrequency> raidFrequency;
    private FormCheckBox playerHunger;
    private FormLocalLabel hungerLabel;
    private FormCheckBox useCustomSpawn;
    private FormTextInput spawnX;
    private FormTextInput spawnY;
    private SplitsComponent splits;

    private FormLocalTextButton saveButton;
    private FormLocalTextButton backButton;

    public LivesplitSettingsForm(SettingsForm settingsForm) {
        super(400, 40);
        this.settingsForm = settingsForm;

        addComponent(new FormLocalLabel("ui", "livesplit", new FontOptions(20),
                FormLocalLabel.ALIGN_MID, getWidth() / 2, 5));

        int margin = 20;
        int padding = 4;
        content = addComponent(
                new FormContentBox(margin, 30, getWidth() - 2 * margin, getHeight() - 40));
        content.drawScrollBarOutsideBox = true;
        FormFlow flow = new FormFlow(padding);
        int inputWidth = 200;
        int inputX = content.getWidth() - inputWidth - padding;
        FontOptions labelOptions = new FontOptions(16);

        // LiveSplit server port input
        content.addComponent(new FormLocalLabel("ui", "livesplitport", labelOptions,
                FormLocalLabel.ALIGN_LEFT, padding, flow.next()));
        portInput = content.addComponent(
                new FormTextInput(inputX, flow.next(45) - 2, FormInputSize.SIZE_20, inputWidth, 5));
        portInput.placeHolder = new StaticMessage(portInput.getText());
        portInput.setRegexMatchFull("\\d*");

        // World overrides label
        content.addComponent(new FormLocalLabel("ui", "worldoverrides", labelOptions,
                FormLocalLabel.ALIGN_MID, content.getWidth() / 2, flow.next(24)));


        // Difficulty selector
        content.addComponent(new FormLocalLabel("ui", "difficulty", labelOptions,
                FormLocalLabel.ALIGN_LEFT, padding, flow.next()));
        difficulty = content.addComponent(new FormDropdownSelectionButton<>(inputX,
                flow.next(25) - 2, FormInputSize.SIZE_20, ButtonColor.BASE, inputWidth));
        for (GameDifficulty value : GameDifficulty.values()) {
            difficulty.options.add(value, value.displayName);
        }

        // Death penalty selector
        content.addComponent(new FormLocalLabel("ui", "deathpenalty", labelOptions,
                FormLocalLabel.ALIGN_LEFT, padding, flow.next()));
        deathPenalty = content.addComponent(new FormDropdownSelectionButton<>(inputX,
                flow.next(25) - 3, FormInputSize.SIZE_20, ButtonColor.BASE, inputWidth));
        for (GameDeathPenalty value : GameDeathPenalty.values()) {
            deathPenalty.options.add(value, value.displayName, () -> value.description);
        }

        // Raid frequency selector
        content.addComponent(new FormLocalLabel("ui", "raidfrequency", labelOptions,
                FormLocalLabel.ALIGN_LEFT, padding, flow.next()));
        raidFrequency = content.addComponent(new FormDropdownSelectionButton<>(inputX,
                flow.next(25) - 3, FormInputSize.SIZE_20, ButtonColor.BASE, inputWidth));
        for (GameRaidFrequency value : GameRaidFrequency.values()) {
            raidFrequency.options.add(value, value.displayName, () -> value.description);
        }

        // Hunger checkbox
        content.addComponent(new FormLocalLabel("ui", "playerhunger", labelOptions,
                FormLocalLabel.ALIGN_LEFT, padding, flow.next()));
        playerHunger = content.addComponent(new FormCheckBox("", inputX, flow.next() + 2));
        hungerLabel = content.addComponent(new FormLocalLabel(null, labelOptions,
                FormLocalLabel.ALIGN_LEFT, inputX + 20, flow.next(25)));
        playerHunger.onClicked(e -> {
            hungerLabel.setLocalization("ui", playerHunger.checked ? "enabled" : "disabled");
        });

        // Custom spawn settings
        content.addComponent(new FormLocalLabel("ui", "customspawnlabel", labelOptions,
                FormLocalLabel.ALIGN_LEFT, padding, flow.next()));
        useCustomSpawn = content.addComponent(new FormCheckBox("", inputX, flow.next() + 2));

        int coordWidth = (inputWidth - 24) / 2;
        spawnX = content.addComponent(new FormTextInput(inputX + 20, flow.next() - 2,
                FormInputSize.SIZE_20, coordWidth, 10));
        spawnX.placeHolder = new LocalMessage("ui", "xcoord");
        spawnX.setRegexMatchFull("-?\\d*");

        spawnY = content.addComponent(new FormTextInput(inputX + 24 + coordWidth, flow.next(45) - 2,
                FormInputSize.SIZE_20, coordWidth, 10));
        spawnY.placeHolder = new LocalMessage("ui", "ycoord");
        spawnY.setRegexMatchFull("-?\\d*");

        useCustomSpawn.onClicked(e -> {
            spawnX.setActive(useCustomSpawn.checked);
            spawnY.setActive(useCustomSpawn.checked);
        });

        // Split segments
        content.addComponent(new FormLocalLabel("ui", "segments", labelOptions,
                FormLocalLabel.ALIGN_MID, content.getWidth() / 2, flow.next(24)));
        splits = content.addComponent(new SplitsComponent(LivesplitHooksEntry.config.splits,
                2 * padding, flow.next(308), content.getWidth() - 4 * padding));
        splits.onResize(e -> {
            int prevHeight = content.getContentBox().height;
            Rectangle rect = new Rectangle(e.width, e.y + e.height + 8);
            content.setContentBox(rect);
            if (prevHeight < rect.height) {
                content.scrollY(rect.height - prevHeight);
            }
        });

        // General menu buttons
        saveButton =
                addComponent(new FormLocalTextButton("ui", "savebutton", 4, 0, getWidth() / 2 - 6));
        saveButton.onClicked(e -> saveOptions());
        backButton = addComponent(new FormLocalTextButton("ui", "backbutton", getWidth() / 2 + 2, 0,
                getWidth() / 2 - 6));
        backButton.onClicked(e -> settingsForm.subMenuBackPressed());

        preferredHeight = flow.next() + 60;
        updateHeight();
        resetOptions();
    }

    private void updateHeight() {
        int height = GameMath.limit(preferredHeight, 100, Screen.getHudHeight() - 100);
        setHeight(height);
        Rectangle rect = content.getContentBoxToFitComponents();
        rect.width = content.getContentWidth();
        rect.height += 8;
        content.setContentBox(rect);
        content.setHeight(getHeight() - 80);
        saveButton.setY(getHeight() - 40);
        backButton.setY(getHeight() - 40);
        setPosMiddle(Screen.getHudWidth() / 2, Screen.getHudHeight() / 2);
    }

    private int stoi(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void resetOptions() {
        Config config = LivesplitHooksEntry.config;
        portInput.setText(String.valueOf(config.livesplitPort));
        difficulty.setSelected(config.difficulty, config.difficulty.displayName);
        deathPenalty.setSelected(config.deathPenalty, config.deathPenalty.displayName);
        raidFrequency.setSelected(config.raidFrequency, config.raidFrequency.displayName);
        playerHunger.checked = config.playerHunger;
        hungerLabel.setLocalization("ui", playerHunger.checked ? "enabled" : "disabled");
        useCustomSpawn.checked = config.useCustomSpawn;
        spawnX.setText(String.valueOf(config.spawnX));
        spawnX.setActive(config.useCustomSpawn);
        spawnY.setText(String.valueOf(config.spawnY));
        spawnY.setActive(config.useCustomSpawn);
        splits.setSplits(config.splits);
        content.setScrollY(0);
    }

    public void saveOptions() {
        Config config = LivesplitHooksEntry.config;
        config.livesplitPort = stoi(portInput.getText());
        config.difficulty = difficulty.getSelected();
        config.deathPenalty = deathPenalty.getSelected();
        config.raidFrequency = raidFrequency.getSelected();
        config.playerHunger = playerHunger.checked;
        config.useCustomSpawn = useCustomSpawn.checked;
        config.spawnX = stoi(spawnX.getText());
        config.spawnY = stoi(spawnY.getText());
        config.splits = splits.getSplits();
        Settings.saveClientSettings();
    }

    private void updateSaveActive() {
        Config config = LivesplitHooksEntry.config;
        boolean modified = config.livesplitPort != stoi(portInput.getText())
                || config.difficulty != difficulty.getSelected()
                || config.deathPenalty != deathPenalty.getSelected()
                || config.raidFrequency != raidFrequency.getSelected()
                || config.playerHunger != playerHunger.checked
                || config.useCustomSpawn != useCustomSpawn.checked
                || config.spawnX != stoi(spawnX.getText())
                || config.spawnY != stoi(spawnY.getText())
                || !config.splits.equals(splits.getSplits());
        saveButton.setActive(modified);
        settingsForm.setSaveActive(modified);
    }

    @Override
    public void draw(TickManager tickManager, PlayerMob perspective, Rectangle renderBox) {
        updateSaveActive();
        super.draw(tickManager, perspective, renderBox);
    }

    @Override
    public void onWindowResized() {
        updateHeight();
        setPosMiddle(Screen.getHudWidth() / 2, Screen.getHudHeight() / 2);
    }
}
