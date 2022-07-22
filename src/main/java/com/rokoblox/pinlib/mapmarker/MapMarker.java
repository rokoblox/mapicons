package com.rokoblox.pinlib.mapmarker;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.map.MapIcon;
import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * A custom map marker type.
 * <p>
 * Used to create MapMarkerEntity.
 */
public class MapMarker {
    private final Identifier id;
    private final boolean dynamic;
    private final RenderLayer iconRenderLayer;

    public MapMarker(Identifier id, Boolean dynamic) {
        this.id = id;
        this.dynamic = dynamic;
        this.iconRenderLayer = RenderLayer.getText(new Identifier(id.getNamespace(), "textures/map/icons/" + id.getPath() + ".png"));;
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

    public Identifier getId() {
        return this.id;
    }

    public boolean isDynamic() {
        return this.dynamic;
    }
}
