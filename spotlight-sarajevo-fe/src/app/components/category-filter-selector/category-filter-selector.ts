import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-category-filter-selector',
  imports: [NgClass],
  templateUrl: './category-filter-selector.html',
  styleUrl: './category-filter-selector.css',
})
export class CategoryFilterSelector {
  @Input() public categoryName: String = 'AA';
  @Input() public type!: 'spot' | 'event';
  @Input() public isSelected: boolean = false;

  @Output() selectionChange: EventEmitter<boolean> = new EventEmitter<boolean>();

  toggleSelection() {
    this.isSelected = !this.isSelected;
    this.selectionChange.emit(this.isSelected);
  }
}
