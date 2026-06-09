import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-profile-icon',
  imports: [NgClass],
  templateUrl: './profile-icon.html',
})
export class ProfileIcon {
  /** Tailwind classes for size and color, e.g., 'w-8 h-8 text-red-500' */
  @Input() classes: string = 'w-6 h-6 text-black';
}
