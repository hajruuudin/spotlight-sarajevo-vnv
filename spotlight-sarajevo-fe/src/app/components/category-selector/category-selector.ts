import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-category-selector',
  imports: [NgClass],
  templateUrl: './category-selector.html',
  styleUrl: './category-selector.css',
  host: {
    class: 'w-auto h-auto py-1 rounded-2xl',
  },
})
export class CategorySelector {
  @Input() public categoryName: String = 'AA';
  @Input() public categoryDescription: String = '';
  @Input() public type!: 'spot' | 'event';
  @Input() public isDisabled: Boolean = false;
  @Output() selectionChange: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() public hoverChange = new EventEmitter<String>();
  public selected = false;

  toggleSelection() {
    if (this.isDisabled) return;
    this.selected = !this.selected;
    this.selectionChange.emit(this.selected);
  }

  onHover(): void {
    this.hoverChange.emit(this.categoryDescription);
  }

  onLeave(): void {
    this.hoverChange.emit('');
  }
}
