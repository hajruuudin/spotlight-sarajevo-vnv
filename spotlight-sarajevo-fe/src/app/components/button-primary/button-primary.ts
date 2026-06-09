import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-button-primary',
  imports: [NgClass],
  templateUrl: './button-primary.html',
  styleUrl: './button-primary.css',
  host: {
    class: 'w-auto h-auto'
  }
})
export class ButtonPrimary {
  @Input() public buttonContent: String = '';
  @Input() public classAddons: String = '';
  @Input() public buttonType: String = 'DEFAULT';
  @Input() public size: 'SMALL' | 'DEFAULT' | 'LARGE' = 'DEFAULT';
  @Input() public isActive: boolean = false;

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
  return `
    rounded-2xl
    transition-all font-semibold
    flex items-center justify-center
    ${this.getSizeClasses()}
  `;
}

getBgClass(): string {
  if (this.isActive) {
    return 'dark:bg-(--primary-200) bg-(--primary-300) dark:text-black text-white';
  }
  switch (this.buttonType) {
    case 'DANGER':
      return 'dark:bg-(--accent-400) bg-(--accent-700) dark:hover:bg-(--accent-700) hover:bg-(--accent-800)';
    case 'INFO':
      return 'dark:bg-yellow-600 bg-yellow-300 dark:hover:bg-(--accent-700) hover:bg-(--accent-800)';
    default:
      return 'dark:bg-(--secondary-400) bg-(--primary-700) dark:hover:bg-(--secondary-500) hover:bg-(--primary-800) dark:text-white text-black';
  }
}
}
