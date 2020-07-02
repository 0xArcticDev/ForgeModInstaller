package mekanism.client.gui.element;

import javax.annotation.Nonnull;
import com.mojang.blaze3d.matrix.MatrixStack;
import mekanism.client.gui.IGuiWrapper;
import mekanism.common.Mekanism;
import mekanism.common.network.PacketGuiInteract;
import mekanism.common.network.PacketGuiInteract.GuiInteraction;
import mekanism.common.tile.interfaces.IHasDumpButton;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.tileentity.TileEntity;

//TODO: Potentially make this extend MekanismImageButton
public class GuiDumpButton<TILE extends TileEntity & IHasDumpButton> extends GuiTexturedElement {

    protected final TILE tile;

    public GuiDumpButton(IGuiWrapper gui, TILE tile, int x, int y) {
        super(MekanismUtils.getResource(ResourceType.GUI, "dump.png"), gui, x, y, 21, 10);
        this.tile = tile;
        playClickSound = true;
    }

    @Override
    public void drawBackground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(matrix, mouseX, mouseY, partialTicks);
        minecraft.textureManager.bindTexture(getResource());
        func_238463_a_(matrix, field_230690_l_, field_230691_m_, 0, 0, field_230688_j_, field_230689_k_, field_230688_j_, field_230689_k_);
    }

    @Override
    public void func_230982_a_(double mouseX, double mouseY) {
        Mekanism.packetHandler.sendToServer(new PacketGuiInteract(GuiInteraction.DUMP_BUTTON, tile));
    }
}