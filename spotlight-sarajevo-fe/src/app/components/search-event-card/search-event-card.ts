import { Component, Input } from '@angular/core';
import { EventShorthandModel } from '../../shared/models/event.model';
import { DatePipe, NgClass } from '@angular/common';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-search-event-card',
  imports: [DatePipe, NgClass, TranslocoPipe],
  templateUrl: './search-event-card.html',
  styleUrl: './search-event-card.css',
  host: {
    class:
      'w-full dark:bg-black bg-(--primary-200) h-auto rounded-2xl outline-2 outline-(--primary-200) hover:outline-2 hover:outline-(--primary-500) flex flex-row justify-between items-stretch group',
  },
})
export class SearchEventCard {
  @Input() lang: string = '';
  @Input() event!: EventShorthandModel;

  get isPastEvent(): boolean {
    if (!this.event?.endDate) return false;
    return new Date(this.event.endDate) < new Date();
  }
}
