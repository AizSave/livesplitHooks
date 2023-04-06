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

    private FormTextInput portInput;
    private FormDropdownSelectionButton<GameDifficulty> difficulty;
    private FormDropdownSelectionButton<GameDeathPenalty> deathPenalty;
    private FormDropdownSelectionButton<GameRaidFrequency> raidFrequency;
    private FormCheckBox playerHunger;
    private FormLocalLabel hungerLabel;
    private FormCheckBox useCustomSpawn;
    private FormTextInput spawnX;
    private FormTextInput spawnY;

    private FormLocalTextButton saveButton;

    public LivesplitSettingsForm(SettingsForm settingsForm) {
        super(400, 40);
        this.settingsForm = settingsForm;

        addComponent(new FormLocalLabel("ui", "livesplit", new FontOptions(20),
                FormLocalLabel.ALIGN_MID, getWidth() / 2, 5));

        content = addComponent(new FormContentBox(0, 30, getWidth(), getHeight() - 40));
        FormFlow flow = new FormFlow(5);
        int labelX = 24;
        int inputX = getWidth() / 2 - 40;
        int inputWidth = 200;
        FontOptions labelOptions = new FontOptions(16);

        // LiveSplit server port input
        content.addComponent(new FormLocalLabel("ui", "livesplitport", labelOptions,
                FormLocalLabel.ALIGN_LEFT, labelX, flow.next()));
        portInput = content.addComponent(
                new FormTextInput(inputX, flow.next(45) - 2, FormInputSize.SIZE_20, inputWidth, 5));
        portInput.placeHolder = new StaticMessage(portInput.getText());
        portInput.setRegexMatchFull("\\d*");

        // Difficulty selector
        content.addComponent(new FormLocalLabel("ui", "difficulty", labelOptions,
                FormLocalLabel.ALIGN_LEFT, labelX, flow.next()));
        difficulty = content.addComponent(new FormDropdownSelectionButton<>(inputX,
                flow.next(25) - 2, FormInputSize.SIZE_20, ButtonColor.BASE, inputWidth));
        for (GameDifficulty value : GameDifficulty.values()) {
            difficulty.options.add(value, value.displayName);
        }

        // Death penalty selector
        content.addComponent(new FormLocalLabel("ui", "deathpenalty", labelOptions,
                FormLocalLabel.ALIGN_LEFT, labelX, flow.next()));
        deathPenalty = content.addComponent(new FormDropdownSelectionButton<>(inputX,
                flow.next(25) - 3, FormInputSize.SIZE_20, ButtonColor.BASE, inputWidth));
        for (GameDeathPenalty value : GameDeathPenalty.values()) {
            deathPenalty.options.add(value, value.displayName, () -> value.description);
        }

        // Raid frequency selector
        content.addComponent(new FormLocalLabel("ui", "raidfrequency", labelOptions,
                FormLocalLabel.ALIGN_LEFT, labelX, flow.next()));
        raidFrequency = content.addComponent(new FormDropdownSelectionButton<>(inputX,
                flow.next(25) - 3, FormInputSize.SIZE_20, ButtonColor.BASE, inputWidth));
        for (GameRaidFrequency value : GameRaidFrequency.values()) {
            raidFrequency.options.add(value, value.displayName, () -> value.description);
        }

        // Hunger checkbox
        content.addComponent(new FormLocalLabel("ui", "playerhunger", labelOptions,
                FormLocalLabel.ALIGN_LEFT, labelX, flow.next()));
        playerHunger = content.addComponent(new FormCheckBox("", inputX, flow.next() + 2));
        hungerLabel = content.addComponent(new FormLocalLabel(null, labelOptions,
                FormLocalLabel.ALIGN_LEFT, inputX + 20, flow.next(45)));
        playerHunger.onClicked(e -> {
            hungerLabel.setLocalization("ui", playerHunger.checked ? "enabled" : "disabled");
        });

        // Custom spawn settings
        content.addComponent(new FormLocalLabel("ui", "customspawnlabel", labelOptions,
                FormLocalLabel.ALIGN_LEFT, labelX, flow.next()));
        useCustomSpawn = content.addComponent(new FormCheckBox("", inputX, flow.next() + 2));

        int coordWidth = (inputWidth - 24) / 2;
        spawnX = content.addComponent(new FormTextInput(inputX + 20, flow.next() - 2,
                FormInputSize.SIZE_20, coordWidth, 10));
        spawnX.placeHolder = new LocalMessage("ui", "xcoord");
        spawnX.setRegexMatchFull("-?\\d*");

        spawnY = content.addComponent(new FormTextInput(inputX + 24 + coordWidth, flow.next(30) - 2,
                FormInputSize.SIZE_20, coordWidth, 10));
        spawnY.placeHolder = new LocalMessage("ui", "ycoord");
        spawnY.setRegexMatchFull("-?\\d*");

        useCustomSpawn.onClicked(e -> {
            spawnX.setActive(useCustomSpawn.checked);
            spawnY.setActive(useCustomSpawn.checked);
        });

        // General menu buttons
        saveButton =
                addComponent(new FormLocalTextButton("ui", "savebutton", 4, 0, getWidth() / 2 - 6));
        saveButton.onClicked(e -> saveOptions());
        FormLocalTextButton backButton = addComponent(new FormLocalTextButton("ui", "backbutton",
                getWidth() / 2 + 2, 0, getWidth() / 2 - 6));
        backButton.onClicked(e -> settingsForm.subMenuBackPressed());

        // Sizing and position tweaks
        int height = GameMath.limit(60 + flow.next(), 100, Screen.getHudHeight() - 100);
        setHeight(height);
        content.setHeight(flow.next());
        content.setContentBox(new Rectangle(getWidth(), getHeight() - 80));
        saveButton.setY(getHeight() - 40);
        backButton.setY(getHeight() - 40);
        setPosMiddle(Screen.getHudWidth() / 2, Screen.getHudHeight() / 2);

        resetOptions();
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
                || config.spawnY != stoi(spawnY.getText());
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
        setPosMiddle(Screen.getHudWidth() / 2, Screen.getHudHeight() / 2);
    }
}
