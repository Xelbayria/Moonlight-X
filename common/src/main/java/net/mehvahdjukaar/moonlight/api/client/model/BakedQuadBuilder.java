package net.mehvahdjukaar.moonlight.api.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Cross loader utility to create baked quad
 * On forge just wraps its own baked quad builder. Can also be fed to render calls as it implements vertex consumer
 */
public interface BakedQuadBuilder extends VertexConsumer {

    static BakedQuadBuilder create(TextureAtlasSprite sprite) {
        return create(sprite, (Matrix4f) null);
    }

    static BakedQuadBuilder create(TextureAtlasSprite sprite, @Nullable Transformation transformation) {
        Matrix4f m = null;
        if (transformation != null) {
            m = new Matrix4f();
            m.translate(new Vector3f(0.5f, 0.5f, 0.5f));
            m.multiply(transformation.getMatrix());
            m.translate(new Vector3f(-0.5f, -0.5f, -0.5f));
        }
        return create(sprite, transformation == null ? null : m);
    }

    //
    @ExpectPlatform
    static BakedQuadBuilder create(TextureAtlasSprite sprite, @Nullable Matrix4f transformation) {
        throw new AssertionError();
    }


    BakedQuadBuilder setAutoDirection();

    BakedQuadBuilder setDirection(Direction direction);

    BakedQuadBuilder setAmbientOcclusion(boolean ambientOcclusion);

    BakedQuadBuilder setShade(boolean shade);

    BakedQuadBuilder lightEmission(int light);

    @Deprecated(forRemoval = true)
    BakedQuadBuilder fromVanilla(BakedQuad quad);

    BakedQuadBuilder setTint(int tintIndex);

    BakedQuad build();

    BakedQuadBuilder setAutoBuild(Consumer<BakedQuad> quadConsumer);


    @Override
    default BakedQuadBuilder vertex(Matrix4f matrix, float x, float y, float z) {
        VertexConsumer.super.vertex(matrix, x, y, z);
        return this;
    }

    @Override
    default BakedQuadBuilder normal(Matrix3f matrix, float x, float y, float z) {
        VertexConsumer.super.normal(matrix, x, y, z);
        return this;
    }
}
