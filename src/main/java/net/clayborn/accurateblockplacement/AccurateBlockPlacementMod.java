package net.clayborn.accurateblockplacement;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.ChatFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;

public class AccurateBlockPlacementMod implements ModInitializer {

	// global state
	public static Boolean  disableNormalItemUse = false;
	public static boolean  isAccurateBlockPlacementEnabled = true;

	private static FabricKeyBinding keyBinding;

	private static boolean wasAccurateBlockPlacementToggleKeyPressed = false;
	
	final static String KEY_CATEGORY_NAME = "Accurate Block Placement";
	
	@Override
	public void onInitialize() {

		keyBinding = FabricKeyBinding.Builder.create(
			    new Identifier("accurateblockplacement", "togglevanillaplacement"),
			    InputUtil.Type.KEYSYM,
			    GLFW.GLFW_KEY_UNKNOWN,
			    KEY_CATEGORY_NAME
			).build();

		KeyBindingRegistry.INSTANCE.addCategory(KEY_CATEGORY_NAME);
		KeyBindingRegistry.INSTANCE.register(keyBinding);
		
		ClientTickCallback.EVENT.register(e ->
		{
			MinecraftClient client = MinecraftClient.getInstance();
			if (client == null || client.inGameHud == null) return;
			
		    if(keyBinding.isPressed())
	    	{
	    		if (!wasAccurateBlockPlacementToggleKeyPressed)
	    		{
	    			isAccurateBlockPlacementEnabled = !isAccurateBlockPlacementEnabled;
	    			
	    			TranslatableComponent message = null;
	    			
	    			if (isAccurateBlockPlacementEnabled) {
	    				message = new TranslatableComponent("net.clayborn.accurateblockplacement.modplacementmodemessage");
	    			} else {
	    				message = new TranslatableComponent("net.clayborn.accurateblockplacement.vanillaplacementmodemessage");
	    			}
	    			
	    			message.setStyle((new Style()).setColor(ChatFormat.DARK_AQUA));
	    			
    				client.inGameHud.addChatMessage(ChatMessageType.SYSTEM, message);
	    		}
	    		wasAccurateBlockPlacementToggleKeyPressed = true;
	    	} else {
	    		wasAccurateBlockPlacementToggleKeyPressed = false;
	    	}
		});
	}
}