import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-text-holder',
  imports: [],
  templateUrl: './text-holder.html',
  styleUrl: './text-holder.css',
  host: {
    class: "flex flex-col w-full justify-center items-start rounded-2xl bg-(--background-200) p-4 text-base md:text-lg "
  }
})
export class TextHolder {
  @Input() text: String = '';
}
