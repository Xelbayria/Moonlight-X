package net.mehvahdjukaar.moonlight.api.fluids.fabric;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.misc.Triplet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public class SoftFluidImpl {

    public static void addFluidSpecificAttributes(SoftFluid.Builder builder, Fluid fluid) {
    }

    public static Triplet<ResourceLocation, ResourceLocation, Integer> getRenderingData(ResourceLocation useTexturesFrom) {
        var fluid = BuiltInRegistries.FLUID.getOptional(useTexturesFrom);
        if (fluid.isPresent()) {
            var f = fluid.get();
            var prop = FluidRenderHandlerRegistry.INSTANCE.get(f);
            if (prop != null) {
                var textures = prop.getFluidSprites(null, null, f.defaultFluidState());
                int tint = prop.getFluidColor(null, null, f.defaultFluidState());
                return Triplet.of(textures[0].contents().name(), textures[1].contents().name(), tint);
            }
        }
        return null;
    }
}
