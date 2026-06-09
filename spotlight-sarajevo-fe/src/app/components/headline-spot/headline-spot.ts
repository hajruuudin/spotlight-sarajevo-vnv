import { Component, Input } from '@angular/core';
import { SpotShorthandModel } from '../../shared/models/spot.model';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-headline-spot',
  imports: [NgClass],
  templateUrl: './headline-spot.html',
  styleUrl: './headline-spot.css',
})
export class HeadlineSpot {
  @Input() lang: string = 'en';
  @Input() classAddons: String = '';
  @Input() spot!: SpotShorthandModel;
}
