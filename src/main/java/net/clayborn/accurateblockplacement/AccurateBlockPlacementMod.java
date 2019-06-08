package net.clayborn.accurateblockplacement;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AccurateBlockPlacementMod implements ModInitializer {
	@Override
	public void onInitialize() {
		// nothing to do
	}
	
	public static BlockPos lastSeenBlockPos = null;
	public static BlockPos lastPlacedBlockPos = null;
	public static Vec3d    lastPlayerPlacedBlockPos = null;
	public static Boolean  disableNormalItemUse = false;
}