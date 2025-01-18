package uwu.lopyluna.obelisk;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import uwu.lopyluna.obelisk.helper.Registration;
import uwu.lopyluna.obelisk.register.commands.ObeliskCommands;
import uwu.lopyluna.obelisk.register.commands.ObeliskCosmeticCommand;

@SuppressWarnings("unused")
@Mod(Obelisk.MOD_ID)
public class Obelisk {
    public static final String MOD_ID = "obelisk";
    public static final String NAME = "Obelisb";
    public static Registration REG = Registration.register(MOD_ID, NAME);

    public Obelisk(IEventBus bus) {
        bus.addListener(this::commonSetup);
        REG.register(bus);

        ModRegistry.register();

        REG.generateData(bus);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        ObeliskCosmeticCommand.register(event.getDispatcher());
        ObeliskCommands.register(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
