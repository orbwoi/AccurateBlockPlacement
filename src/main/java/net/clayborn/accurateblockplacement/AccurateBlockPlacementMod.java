package net.clayborn.accurateblockplacement;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.math.BlockPos;

public class AccurateBlockPlacementMod implements ModInitializer {
	@Override
	public void onInitialize() {
		// nothing to do
	}
	
	public static BlockPos lastSeenBlockPos = null;
	public static BlockPos lastPlacedBlockPos = null;
	public static Boolean  disableNormalItemUse = false;
}
