import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-guides-icon',
  imports: [NgClass],
  templateUrl: './guides-icon.html',
})
export class GuidesIcon {
  /** Tailwind classes for size and color, e.g., 'w-8 h-8 text-red-500' */
  @Input() classes: string = 'w-6 h-6 text-black';
}
