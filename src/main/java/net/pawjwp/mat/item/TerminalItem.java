package net.pawjwp.mat.item;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public class TerminalItem extends Item implements Vanishable {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String TAG_TARGET_POS = "TargetPos";
    public static final String TAG_TARGET_DIMENSION = "TargetDimension";
    public static final String TAG_TARGET_TRACKED = "TargetTracked";
    public static final String TAG_MODE = "Mode";
    public static final float
            MODE_DEFAULT = 0f,
            MODE_TRACKING = 1f,
            MODE_QUESTING = 2f,
            MODE_ATLAS = 3f;

    public TerminalItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public static float getMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return (tag != null && tag.contains(TAG_MODE)) ? tag.getFloat(TAG_MODE) : MODE_DEFAULT;
    }
    public static void setMode(ItemStack stack, float mode) {
        stack.getOrCreateTag().putFloat(TAG_MODE, mode);
    }

    public static boolean hasTarget(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTag();
        return compoundtag != null && (compoundtag.contains(TAG_TARGET_DIMENSION) || compoundtag.contains(TAG_TARGET_POS));
    }

    private static Optional<ResourceKey<Level>> getTargetDimension(CompoundTag pCompoundTag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get(TAG_TARGET_DIMENSION)).result();
    }

    @Nullable
    public static GlobalPos getTargetPosition(CompoundTag pTag) {
        boolean flag = pTag.contains(TAG_TARGET_POS);
        boolean flag1 = pTag.contains(TAG_TARGET_DIMENSION);
        if (flag && flag1) {
            Optional<ResourceKey<Level>> optional = getTargetDimension(pTag);
            if (optional.isPresent()) {
                BlockPos blockpos = NbtUtils.readBlockPos(pTag.getCompound(TAG_TARGET_POS));
                return GlobalPos.of(optional.get(), blockpos);
            }
        }

        return null;
    }

    /**
     * Called each tick as long the item is in a player's inventory. Used by maps to check if it's in a player's hand and
     * update its contents.
     */
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected) {
        if (!pLevel.isClientSide) {
            if (hasTarget(pStack)) {
                CompoundTag compoundtag = pStack.getOrCreateTag();
                if (compoundtag.contains(TAG_TARGET_TRACKED) && !compoundtag.getBoolean(TAG_TARGET_TRACKED)) {
                    return;
                }

                Optional<ResourceKey<Level>> optional = getTargetDimension(compoundtag);
                if (optional.isPresent() && optional.get() == pLevel.dimension() && compoundtag.contains(TAG_TARGET_POS)) {
                    BlockPos blockpos = NbtUtils.readBlockPos(compoundtag.getCompound(TAG_TARGET_POS));
                    if (!pLevel.isInWorldBounds(blockpos)) {
                        compoundtag.remove(TAG_TARGET_POS);
                    }
                }
            }

        }
    }

    /**
     * Called when this item is used when targeting a Block
     */
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        if (!level.getBlockState(blockpos).is(Blocks.LODESTONE)) {
            return super.useOn(pContext);
        } else {
            level.playSound((Player)null, blockpos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
            Player player = pContext.getPlayer();
            ItemStack itemstack = pContext.getItemInHand();
            boolean flag = !player.getAbilities().instabuild && itemstack.getCount() == 1;
            if (flag) {
                this.setTarget(level.dimension(), blockpos, itemstack.getOrCreateTag());
                this.setMode(itemstack, MODE_TRACKING);
            } else {
                ItemStack itemstack1 = new ItemStack(MatItems.MAT.get(), 1);
                CompoundTag compoundtag = itemstack.hasTag() ? itemstack.getTag().copy() : new CompoundTag();
                itemstack1.setTag(compoundtag);
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.setTarget(level.dimension(), blockpos, compoundtag);
                this.setMode(itemstack1, MODE_TRACKING);
                if (!player.getInventory().add(itemstack1)) {
                    player.drop(itemstack1, false);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private void setTarget(ResourceKey<Level> pLodestoneDimension, BlockPos pLodestonePos, CompoundTag pCompoundTag) {
        pCompoundTag.put(TAG_TARGET_POS, NbtUtils.writeBlockPos(pLodestonePos));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, pLodestoneDimension).resultOrPartial(LOGGER::error).ifPresent((p_40731_) -> {
            pCompoundTag.put(TAG_TARGET_DIMENSION, p_40731_);
        });
        pCompoundTag.putBoolean(TAG_TARGET_TRACKED, true);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getDescriptionId(ItemStack pStack) {
        float mode = getMode(pStack);
        return switch ((int) (mode)) {
            case 1 -> "item.mat.mat_tracking";
            case 2 -> "item.mat.mat_questing";
            case 3 -> "item.mat.mat_atlas";
            default -> "item.mat.mat";
        };
    }
}