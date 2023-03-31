package livesplitHooks;

import livesplitHooks.event.MobKillEvent;
import necesse.engine.commands.CommandsManager;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.clientCommands.VoidClientCommand;
import necesse.engine.modLoader.annotations.ModEntry;

@ModEntry
public class LivesplitHooksEntry {
    public static LivesplitServer livesplitServer;
    public static Splits splits;

    public void init() {
        livesplitServer = new LivesplitServer();

        splits = new Splits(new Segment(SplitTrigger.and(new MobKillEvent("evilsprotector"))),
                new Segment(SplitTrigger.and(new MobKillEvent("evilsprotector"))),
                new Segment(SplitTrigger.and(new MobKillEvent("voidwizard"))),
                new Segment(SplitTrigger.and(new MobKillEvent("piratecaptain"))),
                new Segment(SplitTrigger.and(new MobKillEvent("sageandgrit"))),
                new Segment(SplitTrigger.and(new MobKillEvent("fallenwizard"))));

        CommandsManager.registerClientCommand(new VoidClientCommand("tstart", "Start the timer",
                PermissionLevel.USER, (client, log) -> livesplitServer.sendCommand("starttimer")));

        CommandsManager.registerClientCommand(
                new VoidClientCommand("tsplit", "Perform a split", PermissionLevel.USER,
                        (client, log) -> splits.processEvent(new MobKillEvent("test"))));

        CommandsManager.registerClientCommand(new VoidClientCommand("reset", "Reset the timer",
                PermissionLevel.USER, (client, log) -> splits.reset()));
    }

    public void initResources() {
        livesplitServer.connect();
    }

    public void dispose() {
        livesplitServer.disconnect();
    }

    public static void setLoading(boolean isLoading) {
        if (isLoading) {
            livesplitServer.sendCommand("pausegametime");
        } else {
            livesplitServer.sendCommand("unpausegametime");
        }
    }
}
