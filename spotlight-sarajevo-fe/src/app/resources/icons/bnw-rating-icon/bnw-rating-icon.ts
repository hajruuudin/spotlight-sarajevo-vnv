import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-bnw-rating-icon',
  imports: [NgClass],
  templateUrl: './bnw-rating-icon.html',
  styleUrl: './bnw-rating-icon.css',
})
export class BnwRatingIcon {
  @Input() theme: string = 'DARK'
  
  get iconColor(){
    return this.theme == 'DARK' ? 'text-white' : 'text-black'
  }
}
