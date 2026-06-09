import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-bnw-event-cancel',
  imports: [NgClass],
  templateUrl: './bnw-event-cancel-icon.html',
  styleUrl: './bnw-event-cancel-icon.css'
})
export class BnwEventCancelIcon {
  @Input() classes: string = 'w-6 h-6'
  @Input() theme: string = "DARK"

  get iconTheme(){
    return this.theme == 'DARK' ? 'text-white' : 'text-dark'
  }
}
