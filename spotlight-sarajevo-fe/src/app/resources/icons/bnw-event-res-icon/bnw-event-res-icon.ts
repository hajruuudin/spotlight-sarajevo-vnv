import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-bnw-event-res-icon',
  imports: [NgClass],
  templateUrl: './bnw-event-res-icon.html',
  styleUrl: './bnw-event-res-icon.css'
})
export class BnwEventResIcon {
  @Input() classes: string = 'w-6 h-6'
  @Input() theme: string = "DARK"

  get iconTheme(){
    return this.theme == 'DARK' ? 'text-white' : 'text-dark'
  }
}
