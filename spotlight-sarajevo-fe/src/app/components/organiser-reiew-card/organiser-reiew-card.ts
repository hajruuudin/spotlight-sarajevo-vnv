import { Component, Input } from '@angular/core';
import { EventOrganiserReviewModel } from '../../shared/models/event.model';
import { DatePipe, NgClass } from '@angular/common';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-organiser-reiew-card',
  imports: [DatePipe, TranslocoPipe, NgClass],
  templateUrl: './organiser-reiew-card.html',
  styleUrl: './organiser-reiew-card.css',
  host: {
    class: 'w-full h-auto rounded-2xl dark:bg-(--background-200) bg-white border-4 dark:border-(--primary-300) border-(--primary-900) flex flex-col justify-between items-center py-2 px-4 break-inside-avoid'
  }
})
export class OrganiserReiewCard {
  @Input() review!: EventOrganiserReviewModel
}
