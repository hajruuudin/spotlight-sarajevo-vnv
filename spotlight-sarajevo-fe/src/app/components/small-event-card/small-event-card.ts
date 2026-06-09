import { Component, Input } from '@angular/core';
import { EventShorthandModel } from '../../shared/models/event.model';
import { DatePipe, NgClass } from '@angular/common';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-small-event-card',
  imports: [DatePipe, NgClass, TranslocoPipe],
  templateUrl: './small-event-card.html',
  styleUrl: './small-event-card.css',
  host: {
    class:
      'w-full min-w-md md:min-w-xl dark:bg-black bg-(--primary-200) h-auto rounded-2xl outline-2 dark:outline-(--primary-200) outline-(--primary-700) hover:outline-2 hover:dark:outline-(--primary-500) hover:outline-(--primary-100) flex flex-row justify-between group',
  },
})
export class SmallEventCard {
  @Input() lang: string = 'en';
  @Input() classAddons: String = '';
  @Input() event!: EventShorthandModel;

  get isPastEvent(): boolean {
    if (!this.event?.endDate) return false;
    return new Date(this.event.endDate) < new Date();
  }
}
