import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-button-secondary',
  imports: [NgClass],
  templateUrl: './button-secondary.html',
  styleUrl: './button-secondary.css',
})
export class ButtonSecondary {
  @Input() public buttonContent: String = '';
  @Input() public classAddons: String = '';
  @Input() public buttonIcon: String = 'DEFAULT';
  @Input() public size: 'SMALL' | 'DEFAULT' | 'LARGE' = 'DEFAULT';

  @Output() pressed: EventEmitter<void> = new EventEmitter<void>();
  handleClick() {
    this.pressed.emit();
  }

  getSizeClasses(): string {
    switch (this.size) {
      case 'SMALL':
        return 'h-8 text-sm py-1 px-3';
      case 'LARGE':
        return 'h-12 text-xl py-3 px-6';
      default:
        return 'h-10 text-base py-2 px-4';
    }
  }

  getBaseClasses(): string {
    return `dark:bg-(--secondary-200) rounded-2xl hover:dark:bg-(--secondary-300) transition-all font-semibold hover:border-4 hover:dark:border-(--secondary-200) ${this.getSizeClasses()}`;
  }
}
