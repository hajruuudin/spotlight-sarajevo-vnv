import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';
import { SpinnerNavigate } from "../../../components/spinner-navigate/spinner-navigate";

@Component({
  selector: 'app-bnw-event-age-icon',
  imports: [NgClass],
  templateUrl: './bnw-event-age-icon.html',
  styleUrl: './bnw-event-age-icon.css'
})
export class BnwEventAgeIcon {
  @Input() classes: string = 'w-6 h-6'
  @Input() theme: string = "DARK"

  get iconTheme(){
    return this.theme == 'DARK' ? 'text-white' : 'text-dark'
  }
}
