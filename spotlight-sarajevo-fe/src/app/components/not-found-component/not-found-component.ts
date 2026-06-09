import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-not-found-component',
  imports: [],
  templateUrl: './not-found-component.html',
  styleUrl: './not-found-component.css',
  host: {
    class: 'flex flex-col justify-center',
  },
})
export class NotFoundComponent {
  @Input() headerMessage: string = 'Oops, there was an error';
  @Input() messageDescription: string = 'There was an error while processing the request :(';
}
