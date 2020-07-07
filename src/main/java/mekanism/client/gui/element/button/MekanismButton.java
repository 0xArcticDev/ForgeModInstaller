package mekanism.client.gui.element.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

/**
 * Extends our "Widget" class (GuiElement) instead of Button so that we can easier utilize common code
 */
public class MekanismButton extends GuiElement {

    private final IHoverable onHover;
    private final Runnable onLeftClick;
    private final Runnable onRightClick;

    public MekanismButton(IGuiWrapper gui, int x, int y, int width, int height, ITextComponent text, Runnable onLeftClick, IHoverable onHover) {
        this(gui, x, y, width, height, text, onLeftClick, onLeftClick, onHover);
        //TODO: Decide if default implementation for right clicking should be do nothing, or act as left click
    }

    public MekanismButton(IGuiWrapper gui, int x, int y, int width, int height, ITextComponent text, Runnable onLeftClick, Runnable onRightClick, IHoverable onHover) {
        super(gui, x, y, width, height, text);
        this.onHover = onHover;
        this.onLeftClick = onLeftClick;
        this.onRightClick = onRightClick;
        playClickSound = true;
        setButtonBackground(ButtonBackground.DEFAULT);
    }

    private void onLeftClick() {
        if (onLeftClick != null) {
            onLeftClick.run();
        }
    }

    @Override
    public void func_230982_a_(double mouseX, double mouseY) {
        onLeftClick();
    }

    @Override
    public boolean func_231046_a_(int keyCode, int scanCode, int modifiers) {
        //From AbstractButton
        if (this.field_230693_o_ && this.field_230694_p_ && this.func_230999_j_()) {
            if (keyCode == 257 || keyCode == 32 || keyCode == 335) {
                func_230988_a_(Minecraft.getInstance().getSoundHandler());
                onLeftClick();
                return true;
            }
        }
        return false;
    }

    @Override
    public void func_230443_a_(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        if (onHover != null) {
            onHover.onHover(this, matrix, mouseX, mouseY);
        }
    }

    @Override
    public boolean func_231044_a_(double mouseX, double mouseY, int button) {
        if (super.func_231044_a_(mouseX, mouseY, button)) {
            return true;
        }
        if (this.field_230693_o_ && this.field_230694_p_ && func_230449_g_()) {
            if (button == 1) {
                //Right clicked
                func_230988_a_(Minecraft.getInstance().getSoundHandler());
                onRightClick();
                return true;
            }
        }
        return false;
    }

    //TODO: Add right click support to GuiElement
    protected void onRightClick() {
        if (onRightClick != null) {
            onRightClick.run();
        }
    }

}