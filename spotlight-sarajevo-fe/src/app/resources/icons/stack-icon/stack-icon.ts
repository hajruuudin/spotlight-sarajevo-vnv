import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-stack-icon',
  imports: [NgClass],
  templateUrl: './stack-icon.html',
})
export class StackIcon {
  @Input() classes: string = 'w-6 h-6 text-black';
}
