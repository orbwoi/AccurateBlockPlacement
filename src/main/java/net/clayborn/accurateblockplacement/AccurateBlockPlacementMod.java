package net.clayborn.accurateblockplacement;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.MessageType;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class AccurateBlockPlacementMod implements ModInitializer
{

    // global state
    public static Boolean disableNormalItemUse = false;
    public static boolean isAccurateBlockPlacementEnabled = true;

    private static KeyBinding keyBinding;

    private static boolean wasAccurateBlockPlacementToggleKeyPressed = false;

    final static String KEY_CATEGORY_NAME = "Accurate Block Placement";

    @Override
    public void onInitialize()
    {

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(new Identifier("accurateblockplacement", "togglevanillaplacement").toString(),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY_NAME));
        ClientTickEvents.END_CLIENT_TICK.register(e -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.inGameHud == null) return;

            if (keyBinding.isPressed()) {
                if (!wasAccurateBlockPlacementToggleKeyPressed) {
                    isAccurateBlockPlacementEnabled = !isAccurateBlockPlacementEnabled;

                    TranslatableText message;

                    if (isAccurateBlockPlacementEnabled) {
                        message = new TranslatableText("net.clayborn.accurateblockplacement.modplacementmodemessage");
                    } else {
                        message = new TranslatableText("net.clayborn.accurateblockplacement.vanillaplacementmodemessage");
                    }

                    //	message.setStyle(new Style()).setColor(Formatting.DARK_AQUA));

                    client.inGameHud.addChatMessage(MessageType.SYSTEM, message, client.player.getUuid());
                }
                wasAccurateBlockPlacementToggleKeyPressed = true;
            } else {
                wasAccurateBlockPlacementToggleKeyPressed = false;
            }
        });
    }
}