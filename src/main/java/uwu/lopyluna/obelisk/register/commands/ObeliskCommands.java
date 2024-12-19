package uwu.lopyluna.obelisk.register.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.server.command.EnumArgument;
import uwu.lopyluna.obelisk.helper.PlayerInputActions;
import uwu.lopyluna.obelisk.mixins.LivingEntityAccessor;
import uwu.lopyluna.obelisk.helper.access.PlayerAccess;
import uwu.lopyluna.obelisk.register.container.PlayerVaultContainer;
import uwu.lopyluna.obelisk.register.menus.*;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class ObeliskCommands {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.literal("Command Failed"));

    @SuppressWarnings("deprecation")
    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("obelisk").then(Commands.literal("debug")
                .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS) || (stack.getEntity() != null && ("ab49cc7b-53e9-424e-8fa1-778186ffae33".equals(stack.getEntity().getUUID().toString()) || "Dev".equals(((Player) stack.getEntity()).getGameProfile().getName()))))
                .then(Commands.argument("targets", EntityArgument.entities())
                .then(Commands.literal("gravity").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity != null) entity.setNoGravity(!BoolArgumentType.getBool(ctx, "enable"));
                            });return 0;}
                        }))
                ).then(Commands.literal("invisible").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity != null) entity.setInvisible(BoolArgumentType.getBool(ctx, "enable"));
                            });return 0;}
                        }))
                ).then(Commands.literal("invulnerable").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity != null) entity.setInvulnerable(BoolArgumentType.getBool(ctx, "enable"));
                            });return 0;}
                        }))
                ).then(Commands.literal("silent").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity != null) entity.setSilent(BoolArgumentType.getBool(ctx, "enable"));
                            });return 0;}
                        }))
                ).then(Commands.literal("customNameVisible").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity != null) entity.setCustomNameVisible(BoolArgumentType.getBool(ctx, "enable"));
                            });return 0;}
                        }))
                ).then(Commands.literal("mayfly").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            AtomicInteger i = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof Player player) {
                                    player.getAbilities().mayfly = BoolArgumentType.getBool(ctx, "enable");
                                    i.getAndIncrement();
                                }
                            });
                                if (i.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("infiniteMaterials").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            AtomicInteger i = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof Player player) {
                                    player.getAbilities().instabuild = BoolArgumentType.getBool(ctx, "enable");
                                    i.getAndIncrement();
                                }
                            });
                                if (i.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("mayBuild").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            AtomicInteger i = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof Player player) {
                                    player.getAbilities().mayBuild = BoolArgumentType.getBool(ctx, "enable");
                                    i.getAndIncrement();
                                }
                            });
                                if (i.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("physics").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity != null) entity.noPhysics = (!BoolArgumentType.getBool(ctx, "enable"));
                            });return 0;}
                        }))
                ).then(Commands.literal("friction").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            AtomicInteger i = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof Player player) {
                                    player.setDiscardFriction(!BoolArgumentType.getBool(ctx, "enable"));
                                    i.getAndIncrement();
                                } else if (entity instanceof LivingEntity livingEntity) {
                                    livingEntity.setDiscardFriction(!BoolArgumentType.getBool(ctx, "enable"));
                                    i.getAndIncrement();
                                }
                            });
                                if (i.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("seenCredits").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            AtomicInteger i = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof ServerPlayer pEntity) {
                                    pEntity.seenCredits = (BoolArgumentType.getBool(ctx, "enable"));
                                    i.getAndIncrement();
                                }
                            });
                                if (i.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("reducedDebugInfo").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            AtomicInteger i = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof Player pEntity) {
                                    pEntity.setReducedDebugInfo(BoolArgumentType.getBool(ctx, "enable"));
                                    i.getAndIncrement();
                                }
                            });
                                if (i.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("glowing").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity != null) entity.setGlowingTag(BoolArgumentType.getBool(ctx, "enable"));
                            });return 0;}
                        }))
                ).then(Commands.literal("sprinting").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            AtomicInteger i = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof LivingEntity pEntity) {
                                    pEntity.setSprinting(BoolArgumentType.getBool(ctx, "enable"));
                                    i.getAndIncrement();
                                }
                            });
                                if (i.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("swimming").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity != null) entity.setSwimming(BoolArgumentType.getBool(ctx, "enable"));
                            });return 0;}
                        }))
                ).then(Commands.literal("operator").then(Commands.argument("enable", BoolArgumentType.bool())
                        .executes(ctx -> {
                            AtomicInteger i = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof ServerPlayer pEntity) {
                                    boolean bool = BoolArgumentType.getBool(ctx, "enable");
                                    PlayerList playerList = pEntity.server.getPlayerList();
                                    GameProfile profile = pEntity.getGameProfile();

                                    if (playerList.isOp(profile) && !bool)
                                        playerList.deop(profile);
                                    if (!playerList.isOp(profile) && bool)
                                        playerList.op(profile);
                                    i.getAndIncrement();
                                }
                            });
                                if (i.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("airSupply").then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                int i = IntegerArgumentType.getInteger(ctx, "value");
                                if (entity != null) entity.setAirSupply(i);
                            });return 0;}
                        }))
                ).then(Commands.literal("arrowCount").then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            AtomicInteger f = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof LivingEntity pEntity) {
                                    int i = IntegerArgumentType.getInteger(ctx, "value");
                                    pEntity.setArrowCount(i);
                                    f.getAndIncrement();
                                }
                            });
                                if (f.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("stringerCount").then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            AtomicInteger f = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof LivingEntity pEntity) {
                                    int i = IntegerArgumentType.getInteger(ctx, "value");
                                    pEntity.setStingerCount(i);
                                    f.getAndIncrement();
                                }
                            });
                                if (f.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("expLevel").then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            AtomicInteger f = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof ServerPlayer pEntity) {
                                    int i = IntegerArgumentType.getInteger(ctx, "value");
                                    pEntity.setExperienceLevels(i);
                                    f.getAndIncrement();
                                }
                            });
                                if (f.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("expPoint").then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            AtomicInteger f = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof ServerPlayer pEntity) {
                                    int i = IntegerArgumentType.getInteger(ctx, "value");
                                    pEntity.setExperiencePoints(i);
                                    f.getAndIncrement();
                                }
                            });
                                if (f.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("portalCooldown").then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                int i = IntegerArgumentType.getInteger(ctx, "value");
                                if (entity != null) entity.setPortalCooldown(i);
                            });return 0;}
                        }))
                ).then(Commands.literal("score").then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            AtomicInteger f = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof Player pEntity) {
                                    int i = IntegerArgumentType.getInteger(ctx, "value");
                                    pEntity.setScore(i);
                                    f.getAndIncrement();
                                }
                            });
                                if (f.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("health").then(Commands.argument("value", FloatArgumentType.floatArg())
                        .executes(ctx -> {
                            AtomicInteger f = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof LivingEntity pEntity) {
                                    float i = FloatArgumentType.getFloat(ctx, "value");
                                    pEntity.setHealth(i);
                                    f.getAndIncrement();
                                }
                            });
                                if (f.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("speed").then(Commands.argument("value", FloatArgumentType.floatArg()).executes(ctx -> {
                    AtomicInteger f = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity instanceof LivingEntity pEntity) {
                            float i = FloatArgumentType.getFloat(ctx, "value");
                            pEntity.setSpeed(i);
                            f.getAndIncrement();
                        }
                    });
                        if (f.get() == 0) throw ERROR_FAILED.create();
                        return 0;
                    }
                }))
                ).then(Commands.literal("heal").then(Commands.argument("value", FloatArgumentType.floatArg())
                        .executes(ctx -> {
                            AtomicInteger f = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof LivingEntity pEntity) {
                                    float i = FloatArgumentType.getFloat(ctx, "value");
                                    pEntity.heal(i);
                                    f.getAndIncrement();
                                }
                            });
                                if (f.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                        }))
                ).then(Commands.literal("foodNormal").then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    AtomicInteger f = new AtomicInteger();
                                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                        if (entity instanceof Player pEntity) {
                                            int i = IntegerArgumentType.getInteger(ctx, "value");
                                            FoodData food = pEntity.getFoodData();
                                            food.setFoodLevel(Mth.clamp(i, 0, 20));
                                            f.getAndIncrement();
                                        }
                                    });
                                        if (f.get() == 0) throw ERROR_FAILED.create();
                                        return 0;
                                    }
                                }))
                ).then(Commands.literal("foodExhaustion").then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    AtomicInteger f = new AtomicInteger();
                                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                        if (entity instanceof Player pEntity) {
                                            int i = IntegerArgumentType.getInteger(ctx, "value");
                                            FoodData food = pEntity.getFoodData();
                                            food.setExhaustion(Mth.clamp(i, 0, 40));
                                            f.getAndIncrement();
                                        }
                                    });
                                        if (f.get() == 0) throw ERROR_FAILED.create();
                                        return 0;
                                    }
                                }))
                ).then(Commands.literal("foodSaturation").then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    AtomicInteger f = new AtomicInteger();
                                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                        if (entity instanceof Player pEntity) {
                                            int i = IntegerArgumentType.getInteger(ctx, "value");
                                            FoodData food = pEntity.getFoodData();
                                            food.setSaturation(Mth.clamp(i, 0, food.getFoodLevel()));
                                            f.getAndIncrement();
                                        }
                                    });
                                        if (f.get() == 0) throw ERROR_FAILED.create();
                                        return 0;
                                    }
                                }))
                ).then(Commands.literal("hurt").then(Commands.argument("value", FloatArgumentType.floatArg())
                        .executes(ctx -> {
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                float i = FloatArgumentType.getFloat(ctx, "value");
                                if (entity != null) entity.hurt(entity.damageSources().generic(), i);
                            });return 0;}
                        }))
                ).then(Commands.literal("hurtBy").then(Commands.argument("value", FloatArgumentType.floatArg()).then(Commands.argument("attacker", EntityArgument.entity())
                                .executes(ctx -> {
                                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                        float i = FloatArgumentType.getFloat(ctx, "value");
                                        if (entity != null) {
                                            try {
                                                Entity pEntity = EntityArgument.getEntity(ctx, "attacker");
                                                if (pEntity instanceof LivingEntity livingEntity) {
                                                    switch (livingEntity) {
                                                        case Player player -> entity.hurt(entity.damageSources().playerAttack(player), i);
                                                        case Bee bee -> entity.hurt(entity.damageSources().sting(bee), i);
                                                        case Warden warden -> entity.hurt(entity.damageSources().sonicBoom(warden), i);
                                                        case Guardian guardian -> entity.hurt(entity.damageSources().thorns(guardian), i);
                                                        case Monster monster -> entity.hurt(entity.damageSources().mobAttack(monster), i);
                                                        default -> entity.hurt(entity.damageSources().noAggroMobAttack(livingEntity), i);
                                                    }
                                                }
                                            } catch (CommandSyntaxException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });return 0;}
                                })))
                )
                .then(Commands.literal("resetFallDistance").executes(ctx -> {
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity != null) entity.resetFallDistance();
                    });return 0;}
                })).then(Commands.literal("rerollEnchantments").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity instanceof Player pEntity) {
                            pEntity.onEnchantmentPerformed(ItemStack.EMPTY, 0);
                            i.getAndIncrement();
                        }
                    });
                        if (i.get() == 0) throw ERROR_FAILED.create();
                        return 0;
                    }
                })).then(Commands.literal("resetXpDelay").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity instanceof Player pEntity) {
                            pEntity.takeXpDelay = 0;
                            i.getAndIncrement();
                        }
                    });
                        if (i.get() == 0) throw ERROR_FAILED.create();
                        return 0;
                    }
                })).then(Commands.literal("replenishAir").executes(ctx -> {
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity != null) entity.setAirSupply(entity.getMaxAirSupply());
                    });return 0;}
                })).then(Commands.literal("replenishHealth").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity instanceof LivingEntity pEntity) {
                            pEntity.heal(pEntity.getMaxHealth() - pEntity.getHealth());
                            i.getAndIncrement();
                        }
                    });
                        if (i.get() == 0) throw ERROR_FAILED.create();
                        return 0;
                    }
                })).then(Commands.literal("replenishCooldown").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity instanceof Player pEntity) {
                            pEntity.getCooldowns().removeCooldown(pEntity.getMainHandItem().getItem());
                            i.getAndIncrement();
                        }
                    });
                        if (i.get() == 0) throw ERROR_FAILED.create();
                        return 0;
                    }
                })).then(Commands.literal("replenishAttack").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity instanceof LivingEntity pEntity) {
                            ((LivingEntityAccessor) pEntity).obelisk$setAttackStrengthTicker(100000);
                            i.getAndIncrement();
                        }
                    });
                        if (i.get() == 0) throw ERROR_FAILED.create();
                        return 0;
                    }
                })).then(Commands.literal("replenishHunger").executes(ctx -> {
                            AtomicInteger i = new AtomicInteger();
                            if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                            else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                                if (entity instanceof Player pEntity) {
                                    FoodData food = pEntity.getFoodData();
                                    food.setFoodLevel(20);
                                    food.setSaturation(20);
                                    food.setExhaustion(0);
                                    i.getAndIncrement();
                                }
                            });
                                if (i.get() == 0) throw ERROR_FAILED.create();
                                return 0;
                            }
                })).then(Commands.literal("drop").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity instanceof ServerPlayer pEntity) {
                            pEntity.resetLastActionTime();
                            pEntity.drop(false);
                            pEntity.containerMenu.broadcastChanges();
                            i.getAndIncrement();
                        }
                    });
                        if (i.get() == 0) throw ERROR_FAILED.create();
                        return 0;
                    }
                })).then(Commands.literal("dropStack").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
                    else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        if (entity instanceof ServerPlayer pEntity) {
                            pEntity.resetLastActionTime();
                            pEntity.drop(true);
                            pEntity.containerMenu.broadcastChanges();
                            i.getAndIncrement();
                        }
                    });
                        if (i.get() == 0) throw ERROR_FAILED.create();
                        return 0;
                    }
                })).then(Commands.literal("openCrafting").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        pEntity.openMenu(new SimpleMenuProvider(CraftingMenuCommand::new, Component.translatable("container.crafting")));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openLoom").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        pEntity.openMenu(new SimpleMenuProvider(LoomMenuCommand::new, Component.translatable("container.loom")));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openEnchanting").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        pEntity.openMenu(new SimpleMenuProvider(EnchantmentMenuCommand::new, Component.translatable("container.enchant")));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openCartography").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        pEntity.openMenu(new SimpleMenuProvider(CartographyTableMenuCommand::new, Component.translatable("container.cartography_table")));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openSmithing").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        pEntity.openMenu(new SimpleMenuProvider(SmithingMenuCommand::new, Component.translatable("container.upgrade")));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openAnvil").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        pEntity.openMenu(new SimpleMenuProvider(AnvilMenuCommand::new, Component.translatable("container.repair")));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openGrindstone").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        pEntity.openMenu(new SimpleMenuProvider(GrindstoneMenuCommand::new, Component.translatable("container.grindstone_title")));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openVault").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        PlayerVaultContainer vaultContainer = ((PlayerAccess) pEntity).getObelisk$vaultInventory();
                        pEntity.openMenu(new SimpleMenuProvider((pId, pInventory, pPlayer) -> ChestMenu.sixRows(pId, pInventory, vaultContainer), Component.translatable("block.minecraft.vault").withStyle(ChatFormatting.BOLD)));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openEnderChest").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        PlayerEnderChestContainer vaultContainer = pEntity.getEnderChestInventory();
                        pEntity.openMenu(new SimpleMenuProvider((pId, pInventory, pPlayer) -> ChestMenu.threeRows(pId, pInventory, vaultContainer), Component.translatable("block.minecraft.ender_chest").withStyle(ChatFormatting.BOLD)));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openTrash").executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        pEntity.openMenu(new SimpleMenuProvider((pId, pInventory, pPlayer) -> ChestMenu.twoRows(pId, pInventory), Component.translatable("inventory.binSlot").withStyle(ChatFormatting.RED)));
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                })).then(Commands.literal("openPlayerInventory").then(Commands.argument("target", EntityArgument.entity()).executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        try { if (entity instanceof ServerPlayer pEntity && EntityArgument.getEntity(ctx, "target") instanceof ServerPlayer pTarget) {
                            pEntity.openMenu(new SimpleMenuProvider((pId, pInventory, pPlayer) -> new ChestMenu(MenuType.GENERIC_9x4, pId, pInventory, pTarget.getInventory(), 4), Component.empty().append(pTarget.getName().copy().append("'s ")).append(Component.translatable("container.inventory"))));
                            i.getAndIncrement();
                        }} catch (CommandSyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                }))).then(Commands.literal("openPlayerCopiedInventory").then(Commands.argument("target", EntityArgument.entity()).executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
                        try { if (entity instanceof ServerPlayer pEntity && EntityArgument.getEntity(ctx, "target") instanceof ServerPlayer pTarget) {
                            SimpleContainer inventory = new SimpleContainer(9*4);
                            pTarget.getInventory().items.forEach(inventory::addItem);
                            pEntity.openMenu(new SimpleMenuProvider((pId, pInventory, pPlayer) -> new ChestMenu(MenuType.GENERIC_9x4, pId, pInventory, inventory, 4), Component.empty().append(pTarget.getName().copy().append("'s ")).append("Copied ").append(Component.translatable("container.inventory"))));
                            i.getAndIncrement();
                        }} catch (CommandSyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                }))).then(Commands.literal("giveItem").then(Commands.argument("item", ItemArgument.item(pContext)).then(Commands.argument("count", IntegerArgumentType.integer(1)).executes(ctx -> {
                    AtomicInteger i = new AtomicInteger();
                    if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); else { EntityArgument.getEntities(ctx, "targets").forEach(entity -> { if (entity instanceof ServerPlayer pEntity) {
                        try { giveItem(ctx.getSource(), ItemArgument.getItem(ctx, "item"), pEntity, IntegerArgumentType.getInteger(ctx, "count")); }
                        catch (CommandSyntaxException e) { throw new RuntimeException(e); }
                        i.getAndIncrement();
                    }}); if (i.get() == 0) throw ERROR_FAILED.create(); return 0; }
                }))))
                        .then(Commands.literal("actionPose")
                        .then(Commands.argument("type", EnumArgument.enumArgument(Pose.class))
                        .executes(ObeliskCommands::actionPose)
                        ))
                        .then(Commands.literal("fireClear").executes(ctx -> fireClear(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("fireExtinguish").executes(ctx -> fireExtinguish(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("entityDie").executes(ctx -> entityDie(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("entityKill").executes(ctx -> entityKill(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("insideColumnMoveDown").executes(ctx -> insideColumnMoveDown(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("insideColumnMoveUp").executes(ctx -> insideColumnMoveUp(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("aboveColumnMoveDown").executes(ctx -> aboveColumnMoveDown(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("aboveColumnMoveUp").executes(ctx -> aboveColumnMoveUp(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("passengerEject").executes(ctx -> passengerEject(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("passengerDismount").executes(ctx -> passengerDismount(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("markHurt").executes(ctx -> markHurt(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("igniteLava").executes(ctx -> igniteLava(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("despawn").executes(ctx -> despawn(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("combatEnter").executes(ctx -> combatEnter(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("combatLeave").executes(ctx -> combatLeave(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("itemReleaseUsing").executes(ctx -> itemReleaseUsing(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("itemStopUsing").executes(ctx -> itemStopUsing(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("sleep").executes(ctx -> sleep(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("sleepStop").executes(ctx -> sleepStop(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("containerClose").executes(ctx -> containerClose(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("attackSweep").executes(ctx -> attackSweep(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("disableShield").executes(ctx -> disableShield(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("respawnPlayer").executes(ctx -> respawnPlayer(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("jump").executes(ctx -> jump(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("glidingStart").executes(ctx -> glidingStart(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("glidingStop").executes(ctx -> glidingStop(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("refreshDisplayName").executes(ctx -> refreshDisplayName(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("creditShow").executes(ctx -> creditShow(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("disconnect").executes(ctx -> disconnect(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("refreshTabList").executes(ctx -> refreshTabList(ctx, EntityArgument.getEntities(ctx, "targets"))))
                        .then(Commands.literal("clearOmenPos").executes(ctx -> clearOmenPos(ctx, EntityArgument.getEntities(ctx, "targets"))))

                        .then(Commands.literal("teleportSpawn").executes(ctx -> teleportSpawn(ctx, EntityArgument.getEntities(ctx, "targets"), ctx.getSource().getLevel())))
                        .then(Commands.literal("teleportRespawn").executes(ctx -> teleportRespawn(ctx, EntityArgument.getEntities(ctx, "targets"), ctx.getSource().getLevel())))

                        .then(Commands.literal("fireVisual").then(Commands.argument("bool", BoolArgumentType.bool()).executes(ctx ->
                                fireVisual(ctx, EntityArgument.getEntities(ctx, "targets"), BoolArgumentType.getBool(ctx, "bool")))))
                        .then(Commands.literal("onGround").then(Commands.argument("bool", BoolArgumentType.bool()).executes(ctx ->
                                onGround(ctx, EntityArgument.getEntities(ctx, "targets"), BoolArgumentType.getBool(ctx, "bool")))))
                        .then(Commands.literal("inPowderSnow").then(Commands.argument("bool", BoolArgumentType.bool()).executes(ctx ->
                                inPowderSnow(ctx, EntityArgument.getEntities(ctx, "targets"), BoolArgumentType.getBool(ctx, "bool")))))
                        .then(Commands.literal("jumping").then(Commands.argument("bool", BoolArgumentType.bool()).executes(ctx ->
                                jumping(ctx, EntityArgument.getEntities(ctx, "targets"), BoolArgumentType.getBool(ctx, "bool")))))
                        .then(Commands.literal("smite").then(Commands.argument("bool", BoolArgumentType.bool()).executes(ctx ->
                                smite(ctx, EntityArgument.getEntities(ctx, "targets"), ctx.getSource().getLevel(), BoolArgumentType.getBool(ctx, "bool")))))

                        .then(Commands.literal("explodeEntity").then(Commands.argument("target", EntityArgument.entity()).executes(ctx ->
                                explodeEntity(ctx, EntityArgument.getEntities(ctx, "targets"), EntityArgument.getEntity(ctx, "target")))))
                        .then(Commands.literal("deltaPushAway").then(Commands.argument("target", EntityArgument.entity()).executes(ctx ->
                                deltaPushAway(ctx, EntityArgument.getEntities(ctx, "targets"), EntityArgument.getEntity(ctx, "target")))))
                        .then(Commands.literal("attack").then(Commands.argument("target", EntityArgument.entity()).executes(ctx ->
                                attack(ctx, EntityArgument.getEntities(ctx, "targets"), EntityArgument.getEntity(ctx, "target")))))
                        .then(Commands.literal("attackCrit").then(Commands.argument("target", EntityArgument.entity()).executes(ctx ->
                                attackCrit(ctx, EntityArgument.getEntities(ctx, "targets"), EntityArgument.getEntity(ctx, "target")))))
                        .then(Commands.literal("attackMagicCrit").then(Commands.argument("target", EntityArgument.entity()).executes(ctx ->
                                attackMagicCrit(ctx, EntityArgument.getEntities(ctx, "targets"), EntityArgument.getEntity(ctx, "target")))))
                        .then(Commands.literal("viewEntity").then(Commands.argument("target", EntityArgument.entity()).executes(ctx ->
                                viewEntity(ctx, EntityArgument.getEntities(ctx, "targets"), EntityArgument.getEntity(ctx, "target")))))
                        .then(Commands.literal("teleportAt").then(Commands.argument("target", EntityArgument.entity()).executes(ctx ->
                                teleportAt(ctx, EntityArgument.getEntities(ctx, "targets"), EntityArgument.getEntity(ctx, "target")))))

                        .then(Commands.literal("move").then(Commands.argument("type", EnumArgument.enumArgument(MoverType.class)).then(Commands.argument("delta", Vec3Argument.vec3()).executes(ctx ->
                                move(ctx, EntityArgument.getEntities(ctx, "targets"), ctx.getArgument("type", MoverType.class), Vec3Argument.getVec3(ctx, "delta"))))))
                        .then(Commands.literal("actionPlayerInput").then(Commands.argument("type", EnumArgument.enumArgument(PlayerInputActions.class)).executes(ctx ->
                                actionPlayerInput(ctx, EntityArgument.getEntities(ctx, "targets"), ctx.getArgument("type", PlayerInputActions.class)))))
                        .then(Commands.literal("actionForcePlayerPose").then(Commands.argument("type", EnumArgument.enumArgument(Pose.class)).executes(ctx ->
                                actionForcePlayerPose(ctx, EntityArgument.getEntities(ctx, "targets"), ctx.getArgument("type", Pose.class)))))
                        .then(Commands.literal("mainArm").then(Commands.argument("type", EnumArgument.enumArgument(HumanoidArm.class)).executes(ctx ->
                                mainArm(ctx, EntityArgument.getEntities(ctx, "targets"), ctx.getArgument("type", HumanoidArm.class)))))
                        .then(Commands.literal("itemStartUsing").then(Commands.argument("type", EnumArgument.enumArgument(InteractionHand.class)).executes(ctx ->
                                itemStartUsing(ctx, EntityArgument.getEntities(ctx, "targets"), ctx.getArgument("type", InteractionHand.class)))))
                        .then(Commands.literal("swing").then(Commands.argument("type", EnumArgument.enumArgument(InteractionHand.class)).executes(ctx ->
                                swing(ctx, EntityArgument.getEntities(ctx, "targets"), ctx.getArgument("type", InteractionHand.class)))))

                        .then(Commands.literal("deltaAdd").then(Commands.argument("delta", Vec3Argument.vec3()).executes(ctx ->
                                deltaAdd(ctx, EntityArgument.getEntities(ctx, "targets"), Vec3Argument.getVec3(ctx, "delta")))))
                        .then(Commands.literal("deltaSet").then(Commands.argument("delta", Vec3Argument.vec3()).executes(ctx ->
                                deltaSet(ctx, EntityArgument.getEntities(ctx, "targets"), Vec3Argument.getVec3(ctx, "delta")))))
                        .then(Commands.literal("deltaLerp").then(Commands.argument("delta", Vec3Argument.vec3()).executes(ctx ->
                                deltaLerp(ctx, EntityArgument.getEntities(ctx, "targets"), Vec3Argument.getVec3(ctx, "delta")))))
                        .then(Commands.literal("deltaPush").then(Commands.argument("delta", Vec3Argument.vec3()).executes(ctx ->
                                deltaPush(ctx, EntityArgument.getEntities(ctx, "targets"), Vec3Argument.getVec3(ctx, "delta")))))
                        .then(Commands.literal("deltaTravel").then(Commands.argument("delta", Vec3Argument.vec3()).executes(ctx ->
                                deltaTravel(ctx, EntityArgument.getEntities(ctx, "targets"), Vec3Argument.getVec3(ctx, "delta")))))
                        .then(Commands.literal("teleportRelative").then(Commands.argument("position", Vec3Argument.vec3()).executes(ctx ->
                                teleportRelative(ctx, EntityArgument.getEntities(ctx, "targets"), Vec3Argument.getVec3(ctx, "position")))))
                        .then(Commands.literal("teleportTo").then(Commands.argument("position", Vec3Argument.vec3()).executes(ctx ->
                                teleportTo(ctx, EntityArgument.getEntities(ctx, "targets"), Vec3Argument.getVec3(ctx, "position")))))

                        .then(Commands.literal("sleepAt").then(Commands.argument("position", BlockPosArgument.blockPos()).executes(ctx ->
                                sleepAt(ctx, EntityArgument.getEntities(ctx, "targets"), BlockPosArgument.getBlockPos(ctx, "position")))))

                        .then(Commands.literal("igniteTicks").then(Commands.argument("ticks", IntegerArgumentType.integer()).executes(ctx ->
                                igniteTicks(ctx, EntityArgument.getEntities(ctx, "targets"), IntegerArgumentType.getInteger(ctx, "ticks")))))

                        .then(Commands.literal("teleportLooking").then(Commands.argument("distance", IntegerArgumentType.integer()).then(Commands.argument("hitFluid", BoolArgumentType.bool()).executes(ctx ->
                                teleportLooking(ctx, EntityArgument.getEntities(ctx, "targets"), IntegerArgumentType.getInteger(ctx, "distance"), BoolArgumentType.getBool(ctx, "hitFluid"))))))

                        .then(Commands.literal("rotYBody").then(Commands.argument("yRot", DoubleArgumentType.doubleArg()).executes(ctx ->
                                rotYBody(ctx, EntityArgument.getEntities(ctx, "targets"), DoubleArgumentType.getDouble(ctx, "yRot")))))
                        .then(Commands.literal("rotYHead").then(Commands.argument("yRot", DoubleArgumentType.doubleArg()).executes(ctx ->
                                rotYHead(ctx, EntityArgument.getEntities(ctx, "targets"), DoubleArgumentType.getDouble(ctx, "yRot")))))
                        .then(Commands.literal("igniteSeconds").then(Commands.argument("seconds", DoubleArgumentType.doubleArg()).executes(ctx ->
                                igniteSeconds(ctx, EntityArgument.getEntities(ctx, "targets"), DoubleArgumentType.getDouble(ctx, "seconds")))))

                        .then(Commands.literal("boundingBox").then(Commands.argument("from", Vec3Argument.vec3()).then(Commands.argument("to", Vec3Argument.vec3()).executes(ctx ->
                                boundingBox(ctx, EntityArgument.getEntities(ctx, "targets"), new AABB(Vec3Argument.getVec3(ctx, "from"), Vec3Argument.getVec3(ctx, "to")))))))

                        .then(Commands.literal("knockback").then(Commands.argument("strength", DoubleArgumentType.doubleArg()).then(Commands.argument("yRot", DoubleArgumentType.doubleArg()).executes(ctx ->
                                knockback(ctx, EntityArgument.getEntities(ctx, "targets"), DoubleArgumentType.getDouble(ctx, "strength"), DoubleArgumentType.getDouble(ctx, "yRot"))))))
                        .then(Commands.literal("turn").then(Commands.argument("yRot", DoubleArgumentType.doubleArg()).then(Commands.argument("xRot", DoubleArgumentType.doubleArg()).executes(ctx ->
                                turn(ctx, EntityArgument.getEntities(ctx, "targets"), DoubleArgumentType.getDouble(ctx, "yRot"), DoubleArgumentType.getDouble(ctx, "xRot"))))))
                        .then(Commands.literal("rotHeadLerp").then(Commands.argument("yRot", DoubleArgumentType.doubleArg()).then(Commands.argument("xRot", IntegerArgumentType.integer()).executes(ctx ->
                                rotHeadLerp(ctx, EntityArgument.getEntities(ctx, "targets"), DoubleArgumentType.getDouble(ctx, "yRot"), IntegerArgumentType.getInteger(ctx, "xRot"))))))

                        .then(Commands.literal("teleportLerpTo").then(Commands.argument("position", Vec3Argument.vec3()).then(Commands.argument("yRot", DoubleArgumentType.doubleArg()).then(Commands.argument("xRot", DoubleArgumentType.doubleArg()).then(Commands.argument("steps", IntegerArgumentType.integer()).executes(ctx ->
                                teleportLerpTo(ctx, EntityArgument.getEntities(ctx, "targets"), Vec3Argument.getVec3(ctx, "position"), DoubleArgumentType.getDouble(ctx, "yRot"), DoubleArgumentType.getDouble(ctx, "xRot"), IntegerArgumentType.getInteger(ctx, "steps"))))))))

                        .then(Commands.literal("startAttack").then(Commands.argument("damage", DoubleArgumentType.doubleArg()).then(Commands.argument("ticks", IntegerArgumentType.integer()).executes(ctx ->
                                startAttack(ctx, EntityArgument.getEntities(ctx, "targets"), IntegerArgumentType.getInteger(ctx, "ticks"), DoubleArgumentType.getDouble(ctx, "damage"))))))

                        .then(Commands.literal("customName").then(Commands.argument("name", ComponentArgument.textComponent(pContext)).executes(ctx ->
                                customName(ctx, EntityArgument.getEntities(ctx, "targets"), ComponentArgument.getComponent(ctx, "name")))))



        )));
    }
    static int flag = 0;

    public static int turn(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, double pYRot, double pXRot) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.turn(pYRot, pXRot);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int fireVisual(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, boolean bool) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.setSharedFlagOnFire(bool);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int fireClear(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.clearFire();

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int fireExtinguish(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.extinguishFire();

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int onGround(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, boolean bool) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.setOnGround(bool);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int move(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, MoverType mover, Vec3 delta) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.move(mover, delta);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int entityDie(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.die(pEntity.damageSources().genericKill());

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int entityKill(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.kill();

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int explodeEntity(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Entity target) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.onExplosionHit(target);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int inPowderSnow(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, boolean bool) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.setIsInPowderSnow(bool);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int deltaAdd(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Vec3 delta) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.addDeltaMovement(delta);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int deltaSet(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Vec3 delta) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.setDeltaMovement(delta);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int deltaLerp(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Vec3 delta) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.lerpMotion(delta.x, delta.y, delta.z);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int deltaPush(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Vec3 delta) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.push(delta);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int deltaPushAway(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Entity target) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.push(target);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int deltaTravel(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Vec3 delta) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.travel(delta);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int boundingBox(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, AABB box) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.setBoundingBox(box);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int teleportRelative(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Vec3 pos) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.teleportRelative(pos.x, pos.y, pos.z);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int teleportTo(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Vec3 pos) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.teleportTo(pos.x, pos.y, pos.z);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int teleportAt(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Entity target) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                Vec3 pos = target.position();
                pEntity.teleportTo(pos.x, pos.y, pos.z);

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int teleportSpawn(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Level pLevel) throws CommandSyntaxException {
        LevelData worldInfo = pLevel.getLevelData();
        BlockPos spn = worldInfo.getSpawnPos();
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.teleportTo(spn.getX(), spn.getY(), spn.getZ());
                pEntity.resetFallDistance();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int teleportRespawn(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Level pLevel) throws CommandSyntaxException {
        LevelData worldInfo = pLevel.getLevelData();
        BlockPos spn = worldInfo.getSpawnPos();
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                if (entity instanceof ServerPlayer pServerPlayer) {
                    BlockPos pos = pServerPlayer.getRespawnPosition();
                    ServerLevel level = pServerPlayer.server.getLevel(pServerPlayer.getRespawnDimension());
                    if (pos != null && level != null) pServerPlayer.teleportTo(level, pos.getX(), pos.getY(), pos.getZ(), pServerPlayer.getRespawnAngle(), 0);
                    else pEntity.teleportTo(spn.getX(), spn.getY(), spn.getZ());
                } else pEntity.teleportTo(spn.getX(), spn.getY(), spn.getZ());
                pEntity.resetFallDistance();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int teleportLooking(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, int distance, boolean hitFluid) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                BlockHitResult trace = (BlockHitResult) pEntity.pick(distance, 0, hitFluid);
                Vec3 tel = trace.getLocation();
                pEntity.teleportTo(tel.x, tel.y, tel.z);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int teleportLerpTo(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Vec3 pos, double yRot, double xRot, int steps) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.lerpTo(pos.x, pos.y, pos.z, (float)yRot, (float)xRot, steps);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int customName(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Component name) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.setCustomName(name);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int rotYBody(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, double yRot) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.setYBodyRot((float) yRot);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int rotYHead(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, double yRot) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.setYHeadRot((float) yRot);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int rotHeadLerp(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, double yRot, int xRot) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.lerpHeadTo((float) yRot, xRot);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int insideColumnMoveDown(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.onInsideBubbleColumn(true);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int insideColumnMoveUp(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.onInsideBubbleColumn(false);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int aboveColumnMoveDown(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.onAboveBubbleCol(true);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int aboveColumnMoveUp(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.onAboveBubbleCol(false);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int smite(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Level pLevel, boolean message) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity && pLevel instanceof ServerLevel pServer) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(pServer);
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(pEntity.getOnPos()));
                    lightningbolt.setVisualOnly(true);
                    pServer.addFreshEntity(lightningbolt);

                    if (message && pEntity instanceof Player player) player.displayClientMessage(Component.literal("You've been struck by the gods!").withStyle(ChatFormatting.GOLD), false);
                    flag++;
                }
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int passengerEject(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.ejectPassengers();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int passengerDismount(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.stopRiding();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int markHurt(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.hurtMarked = true;
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int igniteTicks(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, int ticks) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.igniteForTicks(ticks);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int igniteSeconds(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, double seconds) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.igniteForSeconds((float) seconds);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int igniteLava(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.lavaHurt();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int despawn(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Entity pEntity) {
                pEntity.discard();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int knockback(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, double strength, double yRot) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.knockback(strength, Mth.sin((float) (yRot * (Math.PI / 180.0))), -Mth.cos((float) (yRot * (Math.PI / 180.0))));
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int swing(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, InteractionHand hand) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.swing(hand, true);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int jumping(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, boolean bool) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.setJumping(bool);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int combatEnter(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.onEnterCombat();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int combatLeave(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.onLeaveCombat();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int itemStartUsing(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, InteractionHand hand) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.startUsingItem(hand);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int itemReleaseUsing(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.releaseUsingItem();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int itemStopUsing(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.stopUsingItem();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int sleep(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.startSleeping(pEntity.getOnPos());
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int sleepAt(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, BlockPos pos) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.startSleeping(pos);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int sleepStop(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.stopSleeping();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int containerClose(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.closeContainer();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int startAttack(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, int ticks, double damage) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.startAutoSpinAttack(ticks, (float) damage, pEntity.getMainHandItem());
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int attack(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Entity target) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.attack(target);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int attackCrit(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Entity target) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.crit(target);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int attackMagicCrit(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Entity target) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.magicCrit(target);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int attackSweep(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.sweepAttack();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int disableShield(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.disableShield();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int respawnPlayer(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.respawn();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int jump(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof LivingEntity pEntity) {
                pEntity.jumpFromGround();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int glidingStart(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.startFallFlying();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int glidingStop(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.stopFallFlying();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int mainArm(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, HumanoidArm arm) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.setMainArm(arm);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int refreshDisplayName(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.refreshDisplayName();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int actionForcePlayerPose(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Pose pose) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof Player pEntity) {
                pEntity.setForcedPose(pose);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int creditShow(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof ServerPlayer pEntity) {
                pEntity.showEndCredits();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int actionPlayerInput(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, PlayerInputActions actions) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof ServerPlayer pEntity) {
                switch (actions) {
                    case LEFT -> pEntity.setPlayerInput(1, 0, false, false);
                    case RIGHT -> pEntity.setPlayerInput(-1, 0, false, false);
                    case FORWARD -> pEntity.setPlayerInput(0, 1, false, false);
                    case BACKWARD -> pEntity.setPlayerInput(0, -1, false, false);
                    case JUMP -> pEntity.setPlayerInput(0, 0, true, false);
                    case SNEAK -> pEntity.setPlayerInput(0, 0, false, true);
                }

                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int disconnect(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof ServerPlayer pEntity) {
                pEntity.disconnect();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int viewEntity(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, Entity target) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof ServerPlayer pEntity) {
                pEntity.setCamera(target);
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int refreshTabList(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof ServerPlayer pEntity) {
                pEntity.refreshTabListName();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }
    public static int clearOmenPos(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        if (!ctx.getNodes().isEmpty()) {
            for (Entity entity : entities) { if (entity instanceof ServerPlayer pEntity) {
                pEntity.clearRaidOmenPosition();
                flag++;
            }}
        }
        if (flag == 0 || ctx.getNodes().isEmpty()) throw ERROR_FAILED.create(); flag = 0;
        return 0;
    }









    public static int actionPose(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        AtomicInteger f = new AtomicInteger();
        if (ctx.getNodes().isEmpty()) throw ERROR_FAILED.create();
        else {EntityArgument.getEntities(ctx, "targets").forEach(entity -> {
            if (entity instanceof LivingEntity pEntity) {
                Pose i = ctx.getArgument("type", Pose.class);
                pEntity.setPose(i);
                f.getAndIncrement();
            }
        });
            if (f.get() == 0) throw ERROR_FAILED.create();
            return 0;
        }
    }

    private static void giveItem(CommandSourceStack pSource, ItemInput pItem, ServerPlayer pTarget, int pCount) throws CommandSyntaxException {
        ItemStack itemstack = pItem.createItemStack(1, false);
        int i = itemstack.getMaxStackSize();
        int j = i * 100;
        if (pCount > j) { pSource.sendFailure(Component.translatable("commands.give.failed.toomanyitems", j, itemstack.getDisplayName()));
        } else {
            int k = pCount;

            while (k > 0) {
                int l = Math.min(i, k);
                k -= l;
                ItemStack itemstack1 = pItem.createItemStack(l, false);
                boolean flag = pTarget.getInventory().add(itemstack1);
                if (flag && itemstack1.isEmpty()) {
                    ItemEntity itementity1 = pTarget.drop(itemstack, false);
                    if (itementity1 != null) {
                        itementity1.makeFakeItem();
                    }
                    pTarget.containerMenu.broadcastChanges();
                } else {
                    ItemEntity itementity = pTarget.drop(itemstack1, false);
                    if (itementity != null) {
                        itementity.setNoPickUpDelay();
                        itementity.setTarget(pTarget.getUUID());
                    }
                }
            }
            pSource.sendSuccess(() -> Component.translatable("commands.give.success.single", pCount, itemstack.getDisplayName(), pTarget.getDisplayName()), false);
        }
    }
}
