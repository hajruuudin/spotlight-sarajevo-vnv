import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-bnw-event-price-icon',
  imports: [NgClass],
  templateUrl: './bnw-event-price-icon.html',
  styleUrl: './bnw-event-price-icon.css'
})
export class BnwEventPriceIcon {
  @Input() classes: string = 'w-6 h-6'
  @Input() theme: string = "DARK"

  get iconTheme(){
    return this.theme == 'DARK' ? 'text-white' : 'text-dark'
  }
}
