import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-request-icon',
  imports: [NgClass],
  templateUrl: './request-icon.html',
})
export class RequestIcon {
  @Input() classes: string = 'w-6 h-6 text-black';
}
