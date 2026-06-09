package com.spotlightsarajevo.modules.transport.utils;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.springframework.stereotype.Component;

@Component
public class TransportUtilities {

    public String convertGeometryToGeoJson(Geometry geometry) {
        if (geometry == null) {
            return null;
        }

        GeoJsonWriter writer = new GeoJsonWriter();
        return writer.write(geometry);
    }
}
