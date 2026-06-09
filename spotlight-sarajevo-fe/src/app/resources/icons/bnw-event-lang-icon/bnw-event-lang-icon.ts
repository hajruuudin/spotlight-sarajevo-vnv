import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-bnw-event-lang-icon',
  imports: [NgClass],
  templateUrl: './bnw-event-lang-icon.html',
  styleUrl: './bnw-event-lang-icon.css'
})
export class BnwEventLangIcon {
  @Input() classes: string = 'w-6 h-6'
  @Input() theme: string = "DARK"

  get iconTheme(){
    return this.theme == 'DARK' ? 'text-white' : 'text-dark'
  }
}
