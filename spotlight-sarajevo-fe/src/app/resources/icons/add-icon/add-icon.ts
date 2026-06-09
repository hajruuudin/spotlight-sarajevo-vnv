import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-add-icon',
  imports: [NgClass],
  templateUrl: './add-icon.html',
})
export class AddIcon {
  @Input() classes: string = 'w-8 h-8 text-black';
}
