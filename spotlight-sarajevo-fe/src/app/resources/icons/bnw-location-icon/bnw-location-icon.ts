import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-bnw-location-icon',
  imports: [NgClass],
  templateUrl: './bnw-location-icon.html',
  styleUrl: './bnw-location-icon.css'
})
export class BnwLocationIcon {
  @Input() theme: string = "DARK"

  get iconTheme(){
    return this.theme == 'DARK' ? 'text-white' : 'text-dark'
  }
}
