package mekanism.client.gui.element;

import java.util.function.BooleanSupplier;
import javax.annotation.Nonnull;
import com.mojang.blaze3d.matrix.MatrixStack;
import mekanism.client.gui.IGuiWrapper;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.util.ResourceLocation;

public class GuiBigLight extends GuiTexturedElement {

    private static final ResourceLocation LIGHTS = MekanismUtils.getResource(ResourceType.GUI, "big_lights.png");
    private final GuiInnerScreen screen;
    private final BooleanSupplier lightSupplier;

    public GuiBigLight(IGuiWrapper gui, int x, int y, BooleanSupplier lightSupplier) {
        super(LIGHTS, gui, x, y, 14, 14);
        this.screen = new GuiInnerScreen(gui, x, y, field_230688_j_, field_230689_k_);
        this.lightSupplier = lightSupplier;
    }

    @Override
    public void drawBackground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(matrix, mouseX, mouseY, partialTicks);
        screen.drawBackground(matrix, mouseX, mouseY, partialTicks);
        minecraft.textureManager.bindTexture(getResource());
        func_238463_a_(matrix, field_230690_l_ + 1, field_230691_m_ + 1, lightSupplier.getAsBoolean() ? 0 : 12, 0, field_230688_j_ - 2, field_230689_k_ - 2, 24, 12);
    }
}