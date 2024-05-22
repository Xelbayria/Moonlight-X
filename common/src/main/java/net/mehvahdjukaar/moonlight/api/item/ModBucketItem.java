package net.mehvahdjukaar.moonlight.api.item;

import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.lang.reflect.Field;
import java.util.function.Supplier;

//no clue why this class even exists
public class ModBucketItem extends BucketItem {
    private static final Field CONTENT = PlatformHelper.findField(BucketItem.class, "content");
    private Supplier<Fluid> supplier;


    public ModBucketItem(Supplier<Fluid> fluid, Properties properties) {
        super(PlatformHelper.getPlatform().isForge() ? Fluids.EMPTY : fluid.get(), properties);
        if (PlatformHelper.getPlatform().isForge()) {
            try {
                //forge needs this to null
                CONTENT.setAccessible(true);
                CONTENT.set(this, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        supplier = fluid;
    }


    @Deprecated(forRemoval = true)
    public ModBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
        if (PlatformHelper.getPlatform().isForge()) {
            try {
                //forge needs this to null
                CONTENT.setAccessible(true);
                CONTENT.set(this, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Fluid getFluid() {
        return this.supplier.get();
    }

}
