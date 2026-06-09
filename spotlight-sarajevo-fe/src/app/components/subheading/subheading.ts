import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-subheading',
  imports: [],
  templateUrl: './subheading.html',
  styleUrl: './subheading.css',
  host: {
    class: 'w-full h-auto flex flex-col'
  }
})
export class Subheading {
  @Input() sectionHead: string = 'SECTION'
  @Input() sectionDescription: string = 'This is the section description'
  @Input() alignment: string = 'CENTER'

  get alignmentClasses(): string {
    switch(this.alignment.toUpperCase()) {
      case 'CENTER':
        return 'text-center';
      case 'RIGHT':
        return 'text-end';
      case 'LEFT':
      default:
        return 'text-start';
    }
  }
}
