package mekanism.common.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.common.Mekanism;
import mekanism.common.MekanismBlock;
import mekanism.common.block.interfaces.IHasModel;
import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.block.states.IStateStorage;
import mekanism.common.item.block.ItemBlockCardboardBox;
import mekanism.common.tile.TileEntityCardboardBox;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockCardboardBox extends BlockMekanismContainer implements IHasModel, IStateStorage, IHasTileEntity<TileEntityCardboardBox> {

    private static boolean testingPlace = false;

    public BlockCardboardBox() {
        super(Material.CLOTH);
        setHardness(0.5F);
        setResistance(1F);
        MinecraftForge.EVENT_BUS.register(this);
        setRegistryName(new ResourceLocation(Mekanism.MODID, "cardboard_box"));
    }

    @Nonnull
    @Override
    public BlockStateContainer createBlockState() {
        return BlockStateHelper.getBlockState(this);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        //TODO
        return 0;
    }

    @Nonnull
    @Override
    @Deprecated
    public BlockState getActualState(@Nonnull BlockState state, IBlockReader world, BlockPos pos) {
        return BlockStateHelper.getActualState(this, state, MekanismUtils.getTileEntitySafe(world, pos));
    }

    @Override
    public boolean isReplaceable(IBlockReader world, @Nonnull BlockPos pos) {
        return testingPlace;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote && player.isSneaking()) {
            TileEntityCardboardBox tileEntity = (TileEntityCardboardBox) world.getTileEntity(pos);

            if (tileEntity != null && tileEntity.storedData != null) {
                BlockData data = tileEntity.storedData;
                testingPlace = true;
                if (!data.block.canPlaceBlockAt(world, pos)) {
                    testingPlace = false;
                    return true;
                }
                testingPlace = false;
                if (data.block != null) {
                    BlockState newstate = data.block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, data.meta, player, hand);
                    data.meta = newstate.getBlock().getMetaFromState(newstate);
                }
                world.setBlockState(pos, data.block.getStateFromMeta(data.meta), 3);
                if (data.tileTag != null && world.getTileEntity(pos) != null) {
                    data.updateLocation(pos);
                    world.getTileEntity(pos).read(data.tileTag);
                }
                if (data.block != null) {
                    data.block.onBlockPlacedBy(world, pos, data.block.getStateFromMeta(data.meta), player, new ItemStack(data.block, 1, data.meta));
                }
                spawnAsEntity(world, pos, MekanismBlock.CARDBOARD_BOX.getItemStack());
            }
        }
        return player.isSneaking();
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull BlockState state) {
        return new TileEntityCardboardBox();
    }

    @Nonnull
    @Override
    protected ItemStack getDropItem(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
        TileEntityCardboardBox tile = (TileEntityCardboardBox) world.getTileEntity(pos);
        ItemStack itemStack = new ItemStack(this);
        if (tile == null) {
            return itemStack;
        }
        if (tile.storedData != null) {
            ((ItemBlockCardboardBox) itemStack.getItem()).setBlockData(itemStack, tile.storedData);
        }
        return itemStack;
    }

    /**
     * If the player is sneaking and the dest block is a cardboard box, ensure onBlockActivated is called, and that the item use is not.
     *
     * @param blockEvent event
     */
    @SubscribeEvent
    public void rightClickEvent(RightClickBlock blockEvent) {
        if (blockEvent.getPlayerEntity().isSneaking() && blockEvent.getWorld().getBlockState(blockEvent.getPos()).getBlock() == this) {
            blockEvent.setUseBlock(Event.Result.ALLOW);
            blockEvent.setUseItem(Event.Result.DENY);
        }
    }

    @Nullable
    @Override
    public Class<? extends TileEntityCardboardBox> getTileClass() {
        return TileEntityCardboardBox.class;
    }

    public static class BlockData {

        public Block block;
        public int meta;
        public CompoundNBT tileTag;

        public BlockData(Block b, int j, CompoundNBT nbtTags) {
            block = b;
            meta = j;
            tileTag = nbtTags;
        }

        public BlockData() {
        }

        public static BlockData read(CompoundNBT nbtTags) {
            BlockData data = new BlockData();
            data.block = Block.getBlockById(nbtTags.getInt("id"));
            data.meta = nbtTags.getInt("meta");
            if (nbtTags.contains("tileTag")) {
                data.tileTag = nbtTags.getCompound("tileTag");
            }
            return data;
        }

        public void updateLocation(BlockPos pos) {
            if (tileTag != null) {
                tileTag.putInt("x", pos.getX());
                tileTag.putInt("y", pos.getY());
                tileTag.putInt("z", pos.getZ());
            }
        }

        public CompoundNBT write(CompoundNBT nbtTags) {
            nbtTags.putInt("id", Block.getIdFromBlock(block));
            nbtTags.putInt("meta", meta);
            if (tileTag != null) {
                nbtTags.put("tileTag", tileTag);
            }
            return nbtTags;
        }
    }
}