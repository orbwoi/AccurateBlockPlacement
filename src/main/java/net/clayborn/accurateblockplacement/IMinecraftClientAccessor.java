package net.clayborn.accurateblockplacement;

public interface IMinecraftClientAccessor {
	void accurateblockplacement_DoItemUseBypassDisable();
	void accurateblockplacement_SetItemUseCooldown(int cooldown);
	public int accurateblockplacement_GetItemUseCooldown();
}
