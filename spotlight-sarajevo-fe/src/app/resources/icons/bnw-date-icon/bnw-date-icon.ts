import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-bnw-date-icon',
  imports: [NgClass],
  templateUrl: './bnw-date-icon.html',
  styleUrl: './bnw-date-icon.css'
})
export class BnwDateIcon {
  @Input() classes: string = 'w-6 h-6'
  @Input() theme: string = "DARK"

  get iconTheme(){
    return this.theme == 'DARK' ? 'text-white' : 'text-dark'
  }
}
