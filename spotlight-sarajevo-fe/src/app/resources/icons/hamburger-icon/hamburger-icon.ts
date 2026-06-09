import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-hamburger-icon',
  imports: [NgClass],
  templateUrl: './hamburger-icon.html',
  styleUrl: './hamburger-icon.css'
})
export class HamburgerIcon {
  @Input() classes: string = 'w-6 h-6 text-black';
}
