package net.clayborn.accurateblockplacement.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.clayborn.accurateblockplacement.AccurateBlockPlacementMod;
import net.clayborn.accurateblockplacement.IMinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	Hand lastBlockHoldingHand;
	
	private Boolean isItemToUseBlock(MinecraftClient client) {
		// have to check each hand
		Hand[] hands = Hand.values();
		int numHands = hands.length;

		for (int i = 0; i < numHands; ++i) {
			Hand thisHand = hands[i];
			ItemStack itemInHand = client.player.getStackInHand(thisHand);

			if (itemInHand.isEmpty()) {
				// hand is empty try the next one
				continue;
			} else if (itemInHand.getItem() instanceof BlockItem) {
				lastBlockHoldingHand = thisHand;
				return true;

			} else {
				// item in use is not a block, abort!
				return false;
			}
		}

		return false;
	}

	@Inject(method = "net/minecraft/client/render/GameRenderer.updateTargetedEntity(F)V", at = @At("RETURN"))
	private void onUpdateTargetedEntityComplete(CallbackInfo info) {

		MinecraftClient client = MinecraftClient.getInstance();

		// safety check
		if (client == null || client.options == null || client.options.keyUse == null)
		{
			return;
		}

		// will be set to true only if needed
		AccurateBlockPlacementMod.disableNormalItemUse = false;
		Boolean freshKeyPress = client.options.keyUse.wasPressed();
		
		// reset state if the key was actually pressed
		// note: at very low frame rates they might have let go and hit it again before we get back here
		if (freshKeyPress)
		{		
			// clear history since they let go of the button
			AccurateBlockPlacementMod.lastSeenBlockPos = null;
			AccurateBlockPlacementMod.lastPlacedBlockPos = null;
		}			

		// safety check
		if (client.hitResult == null || client.player == null)
		{
			return;
		}

		// did we find a block?
		if (client.hitResult.getType() == HitResult.Type.BLOCK) {

			BlockHitResult blockHitResult = (BlockHitResult) client.hitResult;
			BlockPos blockHitPos = blockHitResult.getBlockPos();
			
			// are they holding the use key and is the item to use a block?
			// note: check both freshKey and current state in cause of shitty frame rates
			if ((freshKeyPress || client.options.keyUse.isPressed()) && isItemToUseBlock(client)) {

				// it's a block!! it's go time!				
				IMinecraftClientAccessor clientAccessor = (IMinecraftClientAccessor) client;
				AccurateBlockPlacementMod.disableNormalItemUse = true;

				// if [ we have a fresh key press ] OR
				// [ we should apply the vanilla auto-repeat ] OR
				// [ [ we have no 'seen' history or the 'seen' history isn't a match ] AND
				// [ we have no 'place' history or the 'place' history isn't a match ] ]
				// we can try to place a block
				if (freshKeyPress || clientAccessor.accurateblockplacement_GetItemUseCooldown() <= 0 ||
						(
							(AccurateBlockPlacementMod.lastSeenBlockPos == null
								|| !AccurateBlockPlacementMod.lastSeenBlockPos.equals(blockHitPos))
							&& (AccurateBlockPlacementMod.lastPlacedBlockPos == null
								|| !AccurateBlockPlacementMod.lastPlacedBlockPos.equals(blockHitPos))
						)) {					

					Boolean runOnceFlag = true;
					
					// in case they manage to push the button multiple times per frame
					// note: we already subtracted one from the press count earlier so the total should be the same
					while(runOnceFlag || client.options.keyUse.wasPressed()) {
					
						// use item
						clientAccessor.accurateblockplacement_DoItemUseBypassDisable();
	
						// update last placed
						// TODO: don't update if placement failed
						AccurateBlockPlacementMod.lastPlacedBlockPos = new ItemPlacementContext(
								new ItemUsageContext(client.player, lastBlockHoldingHand, blockHitResult)).getBlockPos();
						
						runOnceFlag = false;
					}

				}

			}
			
			// update the last block we looked at
			AccurateBlockPlacementMod.lastSeenBlockPos = blockHitResult.getBlockPos();

		}

	}
}
