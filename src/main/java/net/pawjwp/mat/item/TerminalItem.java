package net.pawjwp.mat.item;

import com.mojang.logging.LogUtils;
import hardcorequesting.common.forge.event.EventTrigger;
import hardcorequesting.common.forge.network.GeneralUsage;
import hardcorequesting.common.forge.quests.QuestingDataManager;
import hardcorequesting.common.forge.team.PlayerEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
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

    // Hue values of textures for reference:
    // DEFAULT:  blue    ( 185  175 - 195 )  right-left
    // GUIDE:    forest  ( 140  150 - 130 )  left-right
    // TRACKING: lime    ( 95   85  - 105 )  right-left
    // ATLAS:    yellow  ( 50   60  - 40  )  left-right
    // CRAFTING: red     ( 5    15  - 355 )  left-right
    // QUESTING: pink    ( 320  310 - 330 )  right-left
    // STORAGE:  purple  ( 275  285 - 265 )  left-right
    // STARMAP:  indigo  ( 230  220 - 240 )  right-left

    public enum Mode {
        DEFAULT(0, "item.mat.mat", ""),
        GUIDE(1, "item.mat.mat_guide", "_guide"),
        TRACKING(2, "item.mat.mat_tracking", "_tracking"),
        ATLAS(3, "item.mat.mat_atlas", "_atlas"),
        CRAFTING(4, "item.mat.mat_crafting", "_crafting"),
        QUESTING(5, "item.mat.mat_questing", "_questing"),
        STORAGE(6, "item.mat.mat_storage", "_storage"),
        STARMAP(7, "item.mat.mat_starmap", "_starmap");

        private final int id;
        private final String translationKey;
        private final String modelSuffix;

        Mode(int id, String translationKey, String modelSuffix) {
            this.id = id;
            this.translationKey = translationKey;
            this.modelSuffix = modelSuffix;
        }

        public int getId() { return id; }
        public float getPropertyValue() { return (float) id; }
        public String getTranslationKey() { return translationKey; }
        public String getModelSuffix() { return modelSuffix; }

        public Mode next() {
            Mode[] values = values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static Mode fromId(int id) {
            for (Mode mode : values()) {
                if (mode.id == id) return mode;
            }
            return DEFAULT;
        }
    }

    public TerminalItem(Item.Properties pProperties) {
        super(pProperties);
    }

    // Mode handling
    public static Mode getMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TAG_MODE)) {
            return Mode.fromId(tag.getInt(TAG_MODE));
        }
        return Mode.DEFAULT;
    }

    public static float getModePropertyValue(ItemStack stack) {
        return getMode(stack).getPropertyValue();
    }

    public static void setMode(ItemStack stack, Mode mode) {
        stack.getOrCreateTag().putInt(TAG_MODE, mode.getId());
    }

    public static void iterateMode(ItemStack stack) {
        setMode(stack, getMode(stack).next());
    }

    // Target handling
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
            level.playSound(null, blockpos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
            Player player = pContext.getPlayer();
            if (player == null) {
                return InteractionResult.PASS;
            }
            ItemStack itemstack = pContext.getItemInHand();
            boolean flag = !player.getAbilities().instabuild && itemstack.getCount() == 1;
            if (flag) {
                this.setTarget(level.dimension(), blockpos, itemstack.getOrCreateTag());
                setMode(itemstack, Mode.TRACKING);
            } else {
                ItemStack itemstack1 = new ItemStack(MatItems.MAT.get(), 1);
                CompoundTag compoundtag = itemstack.hasTag() ? itemstack.getTag().copy() : new CompoundTag();
                itemstack1.setTag(compoundtag);
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.setTarget(level.dimension(), blockpos, compoundtag);
                setMode(itemstack1, Mode.TRACKING);
                if (!player.getInventory().add(itemstack1)) {
                    player.drop(itemstack1, false);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // When shift+right-clicking, cycle mode
            if (player.isSecondaryUseActive()) {
                iterateMode(stack);
                level.playSound(null, player.blockPosition(), SoundEvents.LEVER_CLICK, SoundSource.PLAYERS, 0.4F, 0.5F + 0.1F * getMode(stack).getId());
            }
            // Otherwise, open corresponding menu
            else {
                openMenuForMode(player, stack);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private void openMenuForMode(Player player, ItemStack stack) {
        Mode mode = getMode(stack);
        switch (mode) {
            case CRAFTING -> player.openMenu(new SimpleMenuProvider(
                    (id, inventory, p) -> new net.minecraft.world.inventory.CraftingMenu(id, inventory),
                    Component.translatable("container.mat.crafting")
            ));
            case QUESTING -> {
                if (!(player instanceof ServerPlayer)) return;

                QuestingDataManager questingData = QuestingDataManager.getInstance();
                if (!questingData.isQuestActive()) {
                    player.sendSystemMessage(Component.translatable("hqm.message.noQuestYet"));
                    return;
                }

                EventTrigger.instance().onBookOpening(new EventTrigger.BookOpeningEvent(player.getUUID(), false, true));
                PlayerEntry entry = questingData.getQuestingData(player).getTeam().getEntry(player.getUUID());
                if (entry != null) {
                    GeneralUsage.sendOpenBook(player, false);
                } else {
                    player.sendSystemMessage(Component.translatable("hqm.message.bookNoPlayer"));
                }
            }
            default -> {}
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
        return getMode(pStack).getTranslationKey();
    }
}