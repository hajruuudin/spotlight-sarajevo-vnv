import { Component, Input } from '@angular/core';
import { SpotShorthandModel } from '../../shared/models/spot.model';
import { DecimalPipe, NgClass } from '@angular/common';
import { ZeroReview } from '../../shared/pipes/zero-review-pipe';

@Component({
  selector: 'app-search-spot-card',
  imports: [DecimalPipe, ZeroReview, NgClass],
  templateUrl: './search-spot-card.html',
  styleUrl: './search-spot-card.css',
  host: {
    class:
      'w-full dark:bg-black bg-(--primary-200) h-auto rounded-2xl outline-2 dark:outline-(--primary-200) outline-(--primary-700) hover:outline-2 hover:dark:outline-(--primary-500) hover:outline-(--primary-100) flex flex-row justify-between items-stretch group',
  },
})
export class SearchSpotCard {
  @Input() lang: String = 'en';
  @Input() classAddons: String = '';
  @Input() spot!: SpotShorthandModel;
}
