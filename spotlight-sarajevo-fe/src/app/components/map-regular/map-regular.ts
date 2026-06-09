import { Component, Input, OnInit } from '@angular/core';
import { LeafletModule } from '@bluehalo/ngx-leaflet';
import * as L from 'leaflet';
import 'leaflet-routing-machine';

@Component({
  selector: 'app-map-regular',
  standalone: true,
  imports: [LeafletModule],
  templateUrl: './map-regular.html',
  styleUrls: ['./map-regular.css'],
  host: {
    class: 'w-full h-full rounded-2xl',
  },
})
export class MapRegular implements OnInit {
  private customIcon = L.icon({
    iconUrl: 'assets/markers/default-pin.svg',
    iconSize: [60, 60],
    iconAnchor: [20, 40],
  });

  @Input() layoutMode: string = 'dark';
  @Input() objectLat: number = 0.0;
  @Input() objectLong: number = 0.0;
  @Input() objectType: string = 'no-type';
  @Input() objectCategoryEn: String = 'no-category';
  @Input() objectCategoryBs: String = 'no-category';

  map!: L.Map;
  mapOptions!: L.MapOptions;

  ngOnInit(): void {
    this.prepareMap();
  }

  prepareMap() {
    if (this.layoutMode == 'dark') {
      this.mapOptions = {
        zoom: 16,
        center: [this.objectLat, this.objectLong],
        layers: [
          L.tileLayer(
            'https://tile.jawg.io/668ed1fa-3dc7-4f82-8320-53ee9ba7536b/{z}/{x}/{y}{r}.png?access-token=BKxt3zjFvSaHNF8hQyr8M8hn0dDlQH0Bwr8leZvo1lYS4kDzzXggeLp5fa9sypKQ',
            {
              attribution: '&copy; <a href="https://www.jawg.io">Jawg</a>',
              maxZoom: 22,
            }
          ),
        ],
      };
    } else if (this.layoutMode == 'light'){
      this.mapOptions = {
        zoom: 16,
        center: [this.objectLat, this.objectLong],
        layers: [
          L.tileLayer(
            'https://tile.jawg.io/jawg-sunny/{z}/{x}/{y}{r}.png?access-token=BKxt3zjFvSaHNF8hQyr8M8hn0dDlQH0Bwr8leZvo1lYS4kDzzXggeLp5fa9sypKQ',
            {
              attribution: '&copy; <a href="https://www.jawg.io">Jawg</a>',
              maxZoom: 22,
            }
          ),
        ],
      };
    }
  }

  onMapReady(map: L.Map) {
    this.map = map;
    this.addSpotMarker();
  }

  addSpotMarker() {
    L.marker([this.objectLat, this.objectLong], { icon: this.customIcon }).addTo(this.map);
  }
}
