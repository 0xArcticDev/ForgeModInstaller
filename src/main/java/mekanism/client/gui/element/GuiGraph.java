package mekanism.client.gui.element;

import javax.annotation.Nonnull;
import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.util.text.ITextComponent;

public class GuiGraph extends GuiTexturedElement {

    private static final int TEXTURE_WIDTH = 3;
    private static final int TEXTURE_HEIGHT = 2;

    private final GuiInnerScreen innerScreen;
    private final LongList graphData = new LongArrayList();
    private final GraphDataHandler dataHandler;

    private long currentScale = 10;
    private boolean fixedScale = false;

    public GuiGraph(IGuiWrapper gui, int x, int y, int width, int height, GraphDataHandler handler) {
        super(MekanismUtils.getResource(ResourceType.GUI, "graph.png"), gui, x, y, width, height);
        innerScreen = new GuiInnerScreen(gui, x - 1, y - 1, width + 2, height + 2);
        dataHandler = handler;
    }

    public void enableFixedScale(long scale) {
        fixedScale = true;
        currentScale = scale;
    }

    public void setMinScale(long minScale) {
        currentScale = minScale;
    }

    public void addData(long data) {
        if (graphData.size() == field_230688_j_) {
            graphData.removeLong(0);
        }

        graphData.add(data);
        if (!fixedScale) {
            for (long i : graphData) {
                if (i > currentScale) {
                    currentScale = i;
                }
            }
        }
    }

    @Override
    public void drawBackground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(matrix, mouseX, mouseY, partialTicks);
        //Draw Black and border
        innerScreen.drawBackground(matrix, mouseX, mouseY, partialTicks);
        minecraft.textureManager.bindTexture(getResource());
        //Draw the graph
        int size = graphData.size();
        for (int i = 0; i < size; i++) {
            long data = Math.min(currentScale, graphData.getLong(i));
            int relativeHeight = (int) (data * field_230689_k_ / (double) currentScale);
            func_238463_a_(matrix, field_230690_l_ + i, field_230691_m_ + field_230689_k_ - relativeHeight, 0, 0, 1, 1, TEXTURE_WIDTH, TEXTURE_HEIGHT);

            RenderSystem.shadeModel(GL11.GL_SMOOTH);
            RenderSystem.disableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

            RenderSystem.color4f(1, 1, 1, 0.2F + 0.8F * i / size);
            func_238463_a_(matrix, field_230690_l_ + i, field_230691_m_ + field_230689_k_ - relativeHeight, 1, 0, 1, relativeHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);

            int hoverIndex = mouseX - getButtonX();
            if (hoverIndex == i && mouseY >= getButtonY() && mouseY < getButtonY() + field_230689_k_) {
                RenderSystem.color4f(1, 1, 1, 0.5F);
                func_238463_a_(matrix, field_230690_l_ + i, field_230691_m_, 2, 0, 1, field_230689_k_, TEXTURE_WIDTH, TEXTURE_HEIGHT);
                MekanismRenderer.resetColor();
                func_238463_a_(matrix, field_230690_l_ + i, field_230691_m_ + field_230689_k_ - relativeHeight, 0, 1, 1, 1, TEXTURE_WIDTH, TEXTURE_HEIGHT);
            }

            MekanismRenderer.resetColor();
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
        }
    }

    @Override
    public void func_230443_a_(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        int hoverIndex = mouseX - relativeX;
        if (hoverIndex >= 0 && hoverIndex < graphData.size()) {
            displayTooltip(matrix, dataHandler.getDataDisplay(graphData.getLong(hoverIndex)), mouseX, mouseY);
        }
    }

    public interface GraphDataHandler {

        ITextComponent getDataDisplay(long data);
    }
}