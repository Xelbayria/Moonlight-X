package net.mehvahdjukaar.moonlight.api.fluids.fabric;

import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.mixins.fabric.MappedRegistryAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.IdentityHashMap;
import java.util.Map;

import static net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry.KEY;

public class SoftFluidRegistryImpl {

    public static void init() {
    }

    private static final Map<Fluid, SoftFluid> FLUID_MAP = new IdentityHashMap<>();
    private static final Map<Item, SoftFluid> ITEM_MAP = new IdentityHashMap<>();


    public static void registerExistingVanillaFluids() {
        //only runs on the first object
        var fluidMap = getFluidsMap();
        MappedRegistry<SoftFluid> reg = (MappedRegistry<SoftFluid>) SoftFluidRegistry.hackyGetRegistry();
        ((MappedRegistryAccessor) reg).setFrozen(false);
        for (Fluid f : BuiltInRegistries.FLUID) {
            try {
                if (f == null) continue;
                if (f instanceof FlowingFluid flowingFluid && flowingFluid.getSource() != f) continue;
                if (f == Fluids.EMPTY) continue;
                //if fluid map contains fluid it means that another equivalent fluid has already been registered
                if (fluidMap.containsKey(f)) continue;
                //is not equivalent: create new SoftFluid from forge fluid

                SoftFluid sf = (new SoftFluid.Builder(f)).build();
                //calling vanilla register function because calling that deferred register or forge registry now does nothing
                //cope
                //SOFT_FLUIDS.get().register(sf.getRegistryName(),sf);
                Registry.register(reg, Utils.getID(f), sf);
                fluidMap.put(f, sf);

            } catch (Exception ignored) {
            }
        }
        //adds empty fluid
        //Registry.register(reg, Moonlight.res("empty"), SoftFluidRegistry.EMPTY);
        reg.freeze();
    }

    public static Map<Fluid, SoftFluid> getFluidsMap() {
        return FLUID_MAP;
    }

    public static Map<Item, SoftFluid> getItemsMap() {
        return ITEM_MAP;
    }


    public static void bootstrap(BootstapContext<SoftFluid> bootstapContext) {
        bootstapContext.register(ResourceKey.create(KEY, SoftFluidRegistry.EMPTY_ID),
                new SoftFluid.Builder(new ResourceLocation(""), new ResourceLocation("")).build());
    }


}
