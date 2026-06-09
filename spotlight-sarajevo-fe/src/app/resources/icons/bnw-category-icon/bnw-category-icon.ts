import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';


@Component({
  selector: 'app-bnw-category-icon',
  imports: [NgClass],
  templateUrl: './bnw-category-icon.html',
  styleUrl: './bnw-category-icon.css'
})
export class BnwCategoryIcon {
  @Input() theme: string = 'DARK'

  get iconTheme(){
    return this.theme == 'DARK' ? 'text-white' : 'text-dark'
  }
}
