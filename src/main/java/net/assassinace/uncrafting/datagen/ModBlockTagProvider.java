package net.assassinace.uncrafting.datagen;

import net.assassinace.uncrafting.Uncrafting;
import net.assassinace.uncrafting.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Uncrafting.MOD_ID, existingFileHelper);
    }
@Override
    protected void addTags(HolderLookup.Provider pProvider) {

        tag(ModTags.Blocks.NEEDS_EMERALD_TOOL)
            .addTag(BlockTags.NEEDS_DIAMOND_TOOL);

        tag(ModTags.Blocks.INCORRECT_FOR_EMERALD_TOOL)
            .addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .remove(ModTags.Blocks.NEEDS_EMERALD_TOOL);


        tag(ModTags.Blocks.NEEDS_COPPER_TOOL)
            .addTag(BlockTags.NEEDS_STONE_TOOL);

        tag(ModTags.Blocks.INCORRECT_FOR_COPPER_TOOL)
            .addTag(BlockTags.INCORRECT_FOR_STONE_TOOL)
                .remove(ModTags.Blocks.NEEDS_COPPER_TOOL);
    }
}
