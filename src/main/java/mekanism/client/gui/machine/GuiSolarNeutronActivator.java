package mekanism.client.gui.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiRedstoneControlTab;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.machine.TileEntitySolarNeutronActivator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiSolarNeutronActivator extends GuiMekanismTile<TileEntitySolarNeutronActivator, MekanismTileContainer<TileEntitySolarNeutronActivator>> {

    public GuiSolarNeutronActivator(MekanismTileContainer<TileEntitySolarNeutronActivator> container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    public void func_231160_c_() {
        super.func_231160_c_();
        func_230480_a_(new GuiSecurityTab<>(this, tile));
        func_230480_a_(new GuiRedstoneControlTab(this, tile));
        func_230480_a_(new GuiGasGauge(() -> tile.inputTank, () -> tile.getGasTanks(null), GaugeType.STANDARD, this, 25, 13));
        func_230480_a_(new GuiGasGauge(() -> tile.outputTank, () -> tile.getGasTanks(null), GaugeType.STANDARD, this, 133, 13));
        func_230480_a_(new GuiProgress(tile::getProgress, ProgressType.LARGE_RIGHT, this, 64, 39).jeiCategory(tile));
    }

    @Override
    protected void func_230451_b_(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        renderTitleText(matrix, 4);
        drawString(matrix, MekanismLang.INVENTORY.translate(), 8, (getYSize() - 96) + 4, titleTextColor());
        super.func_230451_b_(matrix, mouseX, mouseY);
    }
}