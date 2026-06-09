import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-page-header',
  imports: [],
  templateUrl: './page-header.html',
  styleUrl: './page-header.css',
  host: {
    class: 'w-full flex flex-col jusitfy-center items-center space-y-2',
  },
})
export class PageHeader {
  @Input() public title: string = 'DEFAULT';
  @Input() public subtitle: string = 'DEFAULT_SUBTITLE';
  @Input() public theme: string = 'DYNAMIC'

  get themeColors(){
    return this.theme == 'DYNAMIC' ? 'dark:text-white' : 'text-white text-(--text-900)'
  }

  get dividerColor(){
    return this.theme == 'DYNAMIC' ? 'dark:bg-white bg-(--background-50)' : 'bg-white bg-(--text-900)'
  }
}
