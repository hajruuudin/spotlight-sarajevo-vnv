import { Component, Input } from '@angular/core';
import { SpotReviewModel } from '../../shared/models/spot.model';
import { DatePipe, NgClass } from '@angular/common';
import { TranslocoPipe } from '@ngneat/transloco';
import { DecimalPipe } from '@angular/common';
import { ZeroReview } from '../../shared/pipes/zero-review-pipe';

@Component({
  selector: 'app-admin-spot-review-card',
  imports: [DatePipe, TranslocoPipe, DecimalPipe, ZeroReview, NgClass],
  templateUrl: './admin-spot-review-card.html',
  styleUrl: './admin-spot-review-card.css',
  host: {
    class: 'w-full h-auto rounded-lg dark:bg-(--background-100) bg-(--background-800) border border-(--primary-600) dark:border-(--primary-400) flex flex-col justify-between items-start p-3 gap-2'
  }
})
export class AdminSpotReviewCard {
  @Input() review!: SpotReviewModel;

  getOverallRatingClass(rating: number): string {
    if (rating >= 4) return 'dark:bg-(--primary-300) bg-(--accent-900)';
    if (rating >= 3) return 'dark:bg-yellow-500 bg-yellow-600';
    if (rating >= 2) return 'dark:bg-orange-500 bg-orange-600';
    return 'dark:bg-red-500 bg-red-600';
  }
}
