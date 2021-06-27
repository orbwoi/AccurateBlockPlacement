package net.clayborn.accurateblockplacement.mixin;

import net.clayborn.accurateblockplacement.IMinecraftClientAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.clayborn.accurateblockplacement.AccurateBlockPlacementMod;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements IMinecraftClientAccessor
{

    @Shadow
    protected abstract void doItemUse();

    @Override
    public void accurateblockplacement_DoItemUseBypassDisable()
    {
        Boolean oldValue = AccurateBlockPlacementMod.disableNormalItemUse;
        AccurateBlockPlacementMod.disableNormalItemUse = false;
        doItemUse();
        AccurateBlockPlacementMod.disableNormalItemUse = oldValue;
    }

    @Inject(method = "doItemUse()V", at = @At("HEAD"), cancellable = true)
    void OnDoItemUse(CallbackInfo info)
    {
        if (AccurateBlockPlacementMod.disableNormalItemUse) {
            info.cancel();
        }
    }

    @Shadow
    private int itemUseCooldown;

    @Override
    public void accurateblockplacement_SetItemUseCooldown(int cooldown)
    {
        itemUseCooldown = cooldown;
    }

    @Override
    public int accurateblockplacement_GetItemUseCooldown()
    {
        return itemUseCooldown;
    }
}
