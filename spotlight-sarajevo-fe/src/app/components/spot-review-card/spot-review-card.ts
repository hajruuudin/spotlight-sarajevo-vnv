import { Component, Input } from '@angular/core';
import { SpotReviewModel } from '../../shared/models/spot.model';
import { DatePipe, NgClass } from '@angular/common';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-spot-review-card',
  imports: [NgClass, DatePipe, TranslocoPipe],
  templateUrl: './spot-review-card.html',
  styleUrl: './spot-review-card.css',
  host: {
    class: 'w-full h-auto rounded-2xl dark:bg-(--background-200) bg-white border-4 dark:border-(--primary-300) border-(--primary-900) flex flex-col justify-between items-center py-2 px-4 break-inside-avoid'
  }
})
export class SpotReviewCard {
  @Input() review!: SpotReviewModel
  protected ratingBg: string = 'bg-(--primary-400)'

  public get value() : string {
    switch(true){
      case this.inRange(this.review.userOverallRating, 0, 3) : return 'bg-(--accent-300)'
      case this.inRange(this.review.userOverallRating, 3.1, 6) : return 'bg-cyan-500'
      case this.inRange(this.review.userOverallRating, 6.1, 8.9) : return 'bg-(--primary-600)'
      default: return 'bg-yellow'
    }
  }
  
  private inRange(x: number, min: number, max: number): boolean {
    return x >= min && x <= max;
  }
}
