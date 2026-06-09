import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-transport-icon',
  imports: [NgClass],
  templateUrl: './transport-icon.html',
})
export class TransportIcon {
  @Input() classes: string = 'w-6 h-6 text-black';
}
