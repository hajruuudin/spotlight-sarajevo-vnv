import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-spinner-small-component',
  imports: [],
  templateUrl: './spinner-small-component.html',
  styleUrl: './spinner-small-component.css',
})
export class SpinnerSmallComponent {
  @Input() visible = false;
  @Input() message = 'Loading...';
}
