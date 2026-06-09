import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-spinner-navigate',
  imports: [],
  templateUrl: './spinner-navigate.html',
  styleUrl: './spinner-navigate.css'
})
export class SpinnerNavigate {
  @Input() visible = false;
}
