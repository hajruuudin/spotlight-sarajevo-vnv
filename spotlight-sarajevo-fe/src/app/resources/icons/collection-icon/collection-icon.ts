import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-collection-icon',
  templateUrl: './collection-icon.html',
  imports: [NgClass],
})
export class CollectionIcon {
  @Input() classes: string = 'w-6 h-6 text-black';
}