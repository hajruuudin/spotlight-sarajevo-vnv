import { Component, Input } from '@angular/core';
import { TouristGuideShorthandModel } from '../../shared/models/tourist.guide.model';

@Component({
  selector: 'app-tourist-guide-card',
  imports: [],
  templateUrl: './tourist-guide-card.html',
  styleUrl: './tourist-guide-card.css'
})
export class TouristGuideCard {
  @Input({ required: true }) guide!: TouristGuideShorthandModel;
  @Input() lang: string = 'en';
}
