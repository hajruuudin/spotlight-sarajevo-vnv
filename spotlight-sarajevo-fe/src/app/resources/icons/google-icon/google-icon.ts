import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-google-icon',
  imports: [NgClass],
  templateUrl: './google-icon.html',
  styleUrl: './google-icon.css'
})
export class GoogleIcon {
  @Input() theme: string = 'dark'
  
  get iconColor(){
    return this.theme === 'dark' ? 'text-dark' : 'text-black'
  }
}
