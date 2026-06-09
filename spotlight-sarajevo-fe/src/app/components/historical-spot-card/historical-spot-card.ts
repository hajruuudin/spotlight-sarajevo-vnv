import { Component, Input } from '@angular/core';
import { SpotShorthandModel } from '../../shared/models/spot.model';

@Component({
  selector: 'app-historical-spot-card',
  imports: [],
  templateUrl: './historical-spot-card.html',
  styleUrl: './historical-spot-card.css',
})
export class HistoricalSpotCard {
  @Input() lang: String = 'en';
  @Input() spot!: SpotShorthandModel;
}
