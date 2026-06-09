import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-sorting-selector',
  imports: [NgClass],
  templateUrl: './sorting-selector.html',
  styleUrl: './sorting-selector.css',
  host: {
    class: 'w-full',
  },
})
export class SortingSelector {
  @Input() sortingMethod: string = 'Alphabetical';
  @Input() isSelected: boolean = false;
  @Output() selectedMethod = new EventEmitter<string>();

  onMethodSelect(method: string) {
    this.selectedMethod.emit(method);
  }
}
