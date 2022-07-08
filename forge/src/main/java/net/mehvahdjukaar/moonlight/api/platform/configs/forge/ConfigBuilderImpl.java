package net.mehvahdjukaar.moonlight.api.platform.configs.forge;

import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConfigBuilderImpl extends ConfigBuilder {

    public static ConfigBuilder create(ResourceLocation name, ConfigType type) {
        return new ConfigBuilderImpl(name, type);
    }

    private final ForgeConfigSpec.Builder builder;

    private String cat = null;

    public ConfigBuilderImpl(ResourceLocation name, ConfigType type) {
        super(name, type);
        this.builder = new ForgeConfigSpec.Builder();
    }

    @Override
    protected String currentCategory() {
        return cat;
    }


    @Override
    public ConfigSpecWrapper build() {
        return new ConfigSpecWrapper(this.getName(), this.builder.build(), this.type);
    }

    @Override
    public ConfigBuilderImpl push(String category) {
        assert cat == null;
        builder.push(category);
        cat = category;
        return this;
    }

    @Override
    public ConfigBuilderImpl pop() {
        assert cat != null;
        builder.pop();
        cat = null;
        return this;
    }

    @Override
    public Supplier<Boolean> define(String name, boolean defaultValue) {
        maybeAddComment(name);

        return builder.translation(translationKey(name)).define(name, defaultValue);
    }

    @Override
    public Supplier<Double> define(String name, double defaultValue, double min, double max) {
        maybeAddComment(name);
        return builder.translation(translationKey(name)).defineInRange(name, defaultValue, min, max);
    }

    @Override
    public Supplier<Integer> define(String name, int defaultValue, int min, int max) {
        maybeAddComment(name);
        return builder.translation(translationKey(name)).defineInRange(name, defaultValue, min, max);
    }

    @Override
    public Supplier<Integer> defineColor(String name, int defaultValue) {
        maybeAddComment(name);
        var stringConfig = builder.translation(translationKey(name)).define(name, Integer.toHexString(defaultValue),ConfigBuilder.COLOR_CHECK);
        return ()-> Integer.parseUnsignedInt(stringConfig.get());
    }

    @Override
    public Supplier<String> define(String name, String defaultValue, Predicate<Object> validator) {
        maybeAddComment(name);
        return builder.translation(translationKey(name)).define(name, defaultValue, validator);
    }

    @Override
    public <T extends String> Supplier<List<String>> define(String name, List<? extends T> defaultValue, Predicate<Object> predicate) {
        maybeAddComment(name);
           var value = builder.translation(translationKey(name)).defineList(name, defaultValue, predicate);
            return ()-> (List<String>) value.get();
    }

    @Override
    public <T> Supplier<List<? extends T>> defineForgeList(String name, List<? extends T> defaultValue, Predicate<Object> predicate) {
        maybeAddComment(name);
        var value = builder.translation(translationKey(name)).defineList(name, defaultValue, predicate);
        return (Supplier<List<? extends T>>) value;
    }

    @Override
    public <V extends Enum<V>> Supplier<V> define(String name, V defaultValue) {
        maybeAddComment(name);
        return builder.translation(translationKey(name)).defineEnum(name, defaultValue);
    }

    @Override
    public ConfigBuilder comment(String comment) {
        //builder.comment(comment);
        //TODO: choose. either add a translation or a comment literal not both
        return super.comment(comment);
    }
}