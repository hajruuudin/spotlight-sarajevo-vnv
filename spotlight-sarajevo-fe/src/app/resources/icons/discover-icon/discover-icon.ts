import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';


@Component({
  selector: 'app-discover-icon',
  templateUrl: './discover-icon.html',
  imports: [NgClass],
})
export class DiscoverIcon {
  @Input() classes: string = 'w-6 h-6 text-black';
}