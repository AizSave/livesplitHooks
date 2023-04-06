package livesplitHooks;

import necesse.engine.Settings;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.gfx.ui.ButtonIcon;

@ModEntry
public class LivesplitHooksEntry {
    public static Config config;
    public static LivesplitServer livesplitServer;

    public static ButtonIcon livesplitGreen_icon;
    public static ButtonIcon livesplitRed_icon;

    public void init() {
        livesplitServer = new LivesplitServer();
    }

    public ModSettings initSettings() {
        config = new Config();
        return config;
    }

    public void initResources() {
        livesplitServer.connect();

        livesplitGreen_icon = new ButtonIcon(Settings.UI, "livesplit_green.png", false);
        livesplitRed_icon = new ButtonIcon(Settings.UI, "livesplit_red.png", false);
    }

    public void dispose() {
        livesplitServer.disconnect();
    }

    public static void setLoading(boolean isLoading) {
        if (isLoading) {
            livesplitServer.sendCommand(LivesplitServer.PAUSE_GAMETIME);
        } else {
            livesplitServer.sendCommand(LivesplitServer.UNPAUSE_GAMETIME);
        }
    }

    public static ButtonIcon getLivesplitIcon() {
        return livesplitServer.isConnected() ? livesplitGreen_icon : livesplitRed_icon;
    }

    public static GameMessage getLivesplitTooltips() {
        String messageId =
                livesplitServer.isConnected() ? "livesplitconnected" : "livesplitnotconnected";
        return new LocalMessage("ui", messageId);
    }
}
