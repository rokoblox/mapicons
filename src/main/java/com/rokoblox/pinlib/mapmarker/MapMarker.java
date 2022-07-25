package com.rokoblox.pinlib.mapmarker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A custom map marker type.
 * <p>
 * Used to create MapMarkerEntity.
 */
public class MapMarker {
    private final Identifier id;
    private final boolean dynamic;
    @Environment(EnvType.CLIENT)

    private RenderLayer iconRenderLayer;

    public MapMarker(Identifier id, Boolean dynamic) {
        if (id.getPath().equals("null"))
            throw new IllegalArgumentException("Map marker identifier cannot use the path \"null\".");
        this.id = id;
        this.dynamic = dynamic;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            this.iconRenderLayer = RenderLayer.getText(new Identifier(id.getNamespace(), "textures/map/icons/" + id.getPath() + ".png"));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        MapMarker mapMarker = (MapMarker)o;
        return Objects.equals(this.id, mapMarker.id) && Objects.equals(this.dynamic, mapMarker.dynamic);
    }

    public int hashCode() {
        return Objects.hash("pinLibMapMarker", this.id, this.dynamic);
    }

    @NotNull
    public Identifier getId() {
        return this.id;
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    @Environment(EnvType.CLIENT)

    public RenderLayer getIconRenderLayer() {
        return iconRenderLayer;
    }
}
