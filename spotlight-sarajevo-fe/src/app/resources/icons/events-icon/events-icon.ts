import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-events-icon',
  imports: [NgClass],
  templateUrl: './events-icon.html',
})
export class EventsIconComponent {
  
  @Input() classes: string = 'w-6 h-6 text-black';
}
