import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { LeafletModule } from '@bluehalo/ngx-leaflet';
import * as L from 'leaflet';
import { SpotMapModel } from '../../shared/models/spot.model';

/**
 * Fullscreen Leaflet map component that displays multiple spot markers.
 * Supports dark/light theming via Jawg tile layers and emits marker click events.
 *
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Component({
  selector: 'app-map-fullscreen',
  standalone: true,
  imports: [LeafletModule],
  templateUrl: './map-fullscreen.html',
  styleUrls: ['./map-fullscreen.css'],
  host: {
    class: 'w-full h-full block',
  },
})
export class MapFullscreen implements OnInit, OnChanges {
  private readonly DEFAULT_CENTER: L.LatLngExpression = [43.8563, 18.4131]; // Sarajevo center
  private readonly DEFAULT_ZOOM = 14;

  private customIcon = L.icon({
    iconUrl: 'assets/markers/default-pin.svg',
    iconSize: [48, 48],
    iconAnchor: [24, 48],
    popupAnchor: [0, -48],
  });

  @Input() layoutMode: string = 'dark';
  @Input() spots: SpotMapModel[] = [];
  @Input() lang: string = 'en';

  @Output() markerClicked = new EventEmitter<SpotMapModel>();

  map!: L.Map;
  mapOptions!: L.MapOptions;
  private markerLayer: L.LayerGroup = L.layerGroup();

  ngOnInit(): void {
    this.prepareMap();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['spots'] || changes['lang']) && this.map) {
      this.renderMarkers();
    }
  }

  prepareMap(): void {
    const tileUrl =
      this.layoutMode === 'dark'
        ? 'https://tile.jawg.io/668ed1fa-3dc7-4f82-8320-53ee9ba7536b/{z}/{x}/{y}{r}.png?access-token=BKxt3zjFvSaHNF8hQyr8M8hn0dDlQH0Bwr8leZvo1lYS4kDzzXggeLp5fa9sypKQ'
        : 'https://tile.jawg.io/jawg-sunny/{z}/{x}/{y}{r}.png?access-token=BKxt3zjFvSaHNF8hQyr8M8hn0dDlQH0Bwr8leZvo1lYS4kDzzXggeLp5fa9sypKQ';

    this.mapOptions = {
      zoom: this.DEFAULT_ZOOM,
      center: this.DEFAULT_CENTER,
      zoomControl: false,
      layers: [
        L.tileLayer(tileUrl, {
          attribution: '&copy; <a href="https://www.jawg.io">Jawg</a>',
          maxZoom: 22,
        }),
      ],
    };
  }

  onMapReady(map: L.Map): void {
    this.map = map;
    this.markerLayer.addTo(this.map);

    L.control.zoom({ position: 'topright' }).addTo(this.map);

    setTimeout(() => {
      this.map.invalidateSize();
      this.renderMarkers();
    }, 200);
  }

  renderMarkers(): void {
    this.markerLayer.clearLayers();

    if (!this.spots || this.spots.length === 0) return;

    for (const spot of this.spots) {
      if (!spot.latitude || !spot.longitude) continue;

      const spotName = this.lang === 'en' ? spot.officialNameEn : spot.officialNameBs;
      const spotCategory = this.lang === 'en' ? spot.categoryNameEn : spot.categoryNameBs;
      const ratingDisplay = spot.combinedRating > 0 ? spot.combinedRating.toFixed(1) : 'N/A';

      const popupContent = `
        <div style="font-family: 'Kumbh Sans', sans-serif; min-width: 180px;">
          <div style="font-weight: 700; font-size: 14px; margin-bottom: 4px;">${spotName}</div>
          <div style="font-size: 12px; color: #666; margin-bottom: 4px;">${spotCategory}</div>
          <div style="font-size: 12px; margin-bottom: 6px;">⭐ ${ratingDisplay}</div>
          <a href="/spots/${spot.slug}" style="font-size: 12px; color: #088891; font-weight: 600; text-decoration: none;">View Details →</a>
        </div>
      `;

      const marker = L.marker([spot.latitude, spot.longitude], { icon: this.customIcon })
        .bindPopup(popupContent, {
          closeButton: true,
          className: 'spot-map-popup',
        });

      marker.on('click', () => {
        this.markerClicked.emit(spot);
      });

      this.markerLayer.addLayer(marker);
    }

    if (this.spots.length > 0) {
      const validSpots = this.spots.filter((s) => s.latitude && s.longitude);
      if (validSpots.length > 0) {
        const bounds = L.latLngBounds(validSpots.map((s) => [s.latitude, s.longitude]));
        this.map.fitBounds(bounds, { padding: [50, 50], maxZoom: 16 });
      }
    }
  }
}
