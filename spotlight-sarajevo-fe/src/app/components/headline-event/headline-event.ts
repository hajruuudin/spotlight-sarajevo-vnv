import { Component, input, Input } from '@angular/core';
import { EventShorthandModel } from '../../shared/models/event.model';
import { NgClass } from '@angular/common';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-headline-event',
  imports: [NgClass, TranslocoPipe],
  templateUrl: './headline-event.html',
  styleUrl: './headline-event.css',
})
export class HeadlineEvent {
  @Input() lang: String = 'en';
  @Input() classAddons: String = '';
  @Input() event!: EventShorthandModel;

  get isPastEvent(): boolean {
    if (!this.event?.endDate) return false;
    return new Date(this.event.endDate) < new Date();
  }
}
