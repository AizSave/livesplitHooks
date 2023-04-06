package livesplitHooks.patch;

import livesplitHooks.form.ConnectLivesplitComponent;
import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.engine.state.MainMenu;
import necesse.gfx.forms.components.FormComponent;
import necesse.gfx.forms.position.FormPositionContainer;
import necesse.gfx.forms.presets.MainMenuForm;
import net.bytebuddy.asm.Advice;

public class MainMenuFormPatch {
    @ModConstructorPatch(target = MainMenuForm.class, arguments = {MainMenu.class})
    public static class ConstructorPatch {
        @Advice.OnMethodExit
        public static void onExit(@Advice.This MainMenuForm thiz) {
            thiz.mainSideButtons.setHeight(thiz.mainSideButtons.getHeight() + 32);
            // thiz.mainSideButtons.setY(thiz.mainSideButtons.getY() - 32);
            for (FormComponent comp : thiz.mainSideButtons.getComponentList()) {
                if (comp instanceof FormPositionContainer) {
                    FormPositionContainer posComp = (FormPositionContainer) comp;
                    posComp.setY(posComp.getY() + 32);
                }
            }
            thiz.mainSideButtons.addComponent(new ConnectLivesplitComponent(0, 0));
        }
    }
}
