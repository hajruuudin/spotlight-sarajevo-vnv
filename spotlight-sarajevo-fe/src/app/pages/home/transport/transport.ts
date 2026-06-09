import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PageHeader } from '../../../components/page-header/page-header';
import { Subheading } from '../../../components/subheading/subheading';
import { TranslocoPipe } from '@ngneat/transloco';
import { SessionService } from '../../../core/services/session.service';
import { PublicTransportService } from '../../../services/transport.service';
import { TransportPageData } from '../../../core/resolvers/transport.resolver';
import {
  TransportMethodModel,
  TransportMethodShorthandModel,
  TransportMethodLineModel,
  TaxiOperatorModel,
} from '../../../shared/models/transport.model';
import { TransportType, TRANSPORT_OPERATORS } from '../../../shared/constants/TransportOperators';
import { LeafletModule } from '@bluehalo/ngx-leaflet';
import * as L from 'leaflet';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';

@Component({
  selector: 'app-transport',
  imports: [PageHeader, Subheading, TranslocoPipe, LeafletModule, ButtonPrimary],
  templateUrl: './transport.html',
  styleUrl: './transport.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class Transport implements OnInit {
  transportMethods: TransportMethodShorthandModel[] = [];
  selectedMethod: TransportMethodModel | null = null;
  selectedMethodId: number = TransportType.TRAMCAR;
  transportLines: TransportMethodLineModel[] = [];
  taxiCompanies: TaxiOperatorModel[] = [];

  map!: L.Map;
  mapOptions!: L.MapOptions;
  geoJsonLayer: L.GeoJSON | null = null;

  readonly TransportType = TransportType;

  constructor(
    public session: SessionService,
    private transportService: PublicTransportService,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    const resolvedData: TransportPageData = this.route.snapshot.data['transportData'];

    if (resolvedData) {
      this.transportMethods = resolvedData.transportMethods;
      this.selectedMethod = resolvedData.initialMethod;
      this.transportLines = resolvedData.initialLines;
    }

    this.prepareMap();
    this.cdr.detectChanges();
  }

  selectMethod(methodId: number): void {
    this.selectedMethodId = methodId;

    this.transportService.findMethodById(methodId).subscribe({
      next: (method) => {
        this.selectedMethod = method;
        this.updateMapWithGeoJson(method.geometryGeoJson);
      },
      error: (err) => {
        console.error('Error loading transport method:', err);
      },
    });

    if (methodId !== TransportType.TAXI) {
      this.loadTransportLines(methodId);
    } else {
      this.loadTaxiOperators();
    }
  }

  loadTaxiOperators(): void {
    this.transportService.findTaxiOperators().subscribe({
      next: (companies) => {
        this.taxiCompanies = companies;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading taxi operators:', err);
      },
    });
  }

  loadTransportLines(transportTypeId: number): void {
    const allLines: TransportMethodLineModel[] = [];
    let completedRequests = 0;

    TRANSPORT_OPERATORS.forEach((operator) => {
      this.transportService
        .findLinesByOperatorAndTransportType(operator.id, transportTypeId)
        .subscribe({
          next: (lines) => {
            allLines.push(...lines);
            completedRequests++;
            if (completedRequests === TRANSPORT_OPERATORS.length) {
              this.transportLines = allLines;
            }
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.error('Error loading transport lines:', err);
            completedRequests++;
          },
        });
    });
  }

  prepareMap(): void {
    const sarajevoCenter: L.LatLngTuple = [43.8563, 18.4131];

    if (this.session.theme() === 'dark') {
      this.mapOptions = {
        zoom: 12,
        center: sarajevoCenter,
        layers: [
          L.tileLayer(
            'https://tile.jawg.io/668ed1fa-3dc7-4f82-8320-53ee9ba7536b/{z}/{x}/{y}{r}.png?access-token=BKxt3zjFvSaHNF8hQyr8M8hn0dDlQH0Bwr8leZvo1lYS4kDzzXggeLp5fa9sypKQ',
            {
              attribution: '&copy; <a href="https://www.jawg.io">Jawg</a>',
              maxZoom: 22,
            },
          ),
        ],
      };
    } else {
      this.mapOptions = {
        zoom: 12,
        center: sarajevoCenter,
        layers: [
          L.tileLayer(
            'https://tile.jawg.io/jawg-sunny/{z}/{x}/{y}{r}.png?access-token=BKxt3zjFvSaHNF8hQyr8M8hn0dDlQH0Bwr8leZvo1lYS4kDzzXggeLp5fa9sypKQ',
            {
              attribution: '&copy; <a href="https://www.jawg.io">Jawg</a>',
              maxZoom: 22,
            },
          ),
        ],
      };
    }
  }

  onMapReady(map: L.Map): void {
    this.map = map;
    if (this.selectedMethod?.geometryGeoJson) {
      this.updateMapWithGeoJson(this.selectedMethod.geometryGeoJson);
    }
  }

  getTransportMarkerIcon(transportType: number): L.Icon {
    let iconUrl = '';
    switch (transportType) {
      case TransportType.TRAMCAR:
        iconUrl = 'assets/markers/transport/TRAM_MARKER.png';
        break;
      case TransportType.TROLLEY:
        iconUrl = 'assets/markers/transport/TROLLEY_MARKER.png';
        break;
      case TransportType.BUS:
        iconUrl = 'assets/markers/transport/BUS_MARKER.png';
        break;
      case TransportType.TAXI:
        iconUrl = 'assets/markers/transport/TAXI_MARKER.png';
        break;
      default:
        iconUrl = 'assets/markers/default-pin.svg';
    }
    return L.icon({
      iconUrl,
      iconSize: [40, 40],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32],
    });
  }

  updateMapWithGeoJson(geoJsonString: string): void {
    if (!this.map) return;
    this.clearGeoJsonLayer();
    try {
      const geoJsonData = JSON.parse(geoJsonString);
      const lineColor = this.getLineColor();
      const markerIcon = this.getTransportMarkerIcon(this.selectedMethodId);

      this.geoJsonLayer = L.geoJSON(geoJsonData, {
        style: {
          color: lineColor,
          weight: 4,
          opacity: 0.8,
        },
        pointToLayer: (feature, latlng) => {
          return L.marker(latlng, { icon: markerIcon });
        },
      }).addTo(this.map);

      if (this.geoJsonLayer.getBounds().isValid()) {
        this.map.fitBounds(this.geoJsonLayer.getBounds(), { padding: [20, 20] });
      }
    } catch (e) {
      console.error('Error parsing GeoJSON:', e);
    }
  }

  clearGeoJsonLayer(): void {
    if (this.geoJsonLayer && this.map) {
      this.map.removeLayer(this.geoJsonLayer);
      this.geoJsonLayer = null;
    }
  }

  getLineColor(): string {
    switch (this.selectedMethodId) {
      case TransportType.TRAMCAR:
        return '#bb0068';
      case TransportType.TROLLEY:
        return '#956623';
      case TransportType.BUS:
        return '#3684a6';
      default:
        return '#8b5cf6';
    }
  }

  isTaxi(): boolean {
    return this.selectedMethodId === TransportType.TAXI;
  }
}
