import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-spots-icon',
  imports: [NgClass],
  templateUrl: './spots-icon.html',
})
export class SpotsIcon{
  @Input() classes: string = 'w-6 h-6 text-black';
}
