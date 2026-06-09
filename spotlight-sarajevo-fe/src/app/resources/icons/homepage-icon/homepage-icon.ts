import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-homepage-icon',
  imports: [NgClass],
  templateUrl: './homepage-icon.html',
})
export class HomepageIcon {
  @Input() classes: string = 'w-6 h-6 text-black';
}
