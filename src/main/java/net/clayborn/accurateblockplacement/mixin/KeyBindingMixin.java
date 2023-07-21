package net.clayborn.accurateblockplacement.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.clayborn.accurateblockplacement.IKeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements IKeyBindingAccessor {
	@Shadow
	private int timesPressed;
	
	@Override
	public int accurateblockplacement_GetTimesPressed()
	{
		return timesPressed;		
	}
}
