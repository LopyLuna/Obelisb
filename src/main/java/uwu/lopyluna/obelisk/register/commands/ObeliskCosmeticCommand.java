package uwu.lopyluna.obelisk.register.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import static uwu.lopyluna.obelisk.client.layer.DnDesireCosmeticLayer.DISABLED_COSMETICS;

public class ObeliskCosmeticCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.literal("Command Failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register(Commands.literal("obelisk").then(Commands.literal("cosmetic")
                .requires(stack -> stack.getEntity() != null)
                .then(Commands.literal("DnDesires1milCape").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty() || ctx.getSource().getPlayer() == null) throw ERROR_FAILED.create();
                            else { boolean bool = BoolArgumentType.getBool(ctx, "enable"); var player = ctx.getSource().getPlayer();
                                var uuid = player.getUUID().toString();
                                if (bool && DISABLED_COSMETICS.contains(uuid)) DISABLED_COSMETICS.remove(uuid);
                                else if (!bool && !DISABLED_COSMETICS.contains(uuid)) DISABLED_COSMETICS.add(uuid);
                                return 0;
                            }
                        }))
                )
        ));

    }
}
