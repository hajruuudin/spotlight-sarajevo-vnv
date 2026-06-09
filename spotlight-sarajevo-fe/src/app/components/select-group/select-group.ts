import { NgFor, NgIf } from '@angular/common';
import { ChangeDetectorRef, Component, forwardRef, Input } from '@angular/core';
import { NG_VALUE_ACCESSOR, NgForm } from '@angular/forms';

@Component({
  selector: 'app-select-group',
  imports: [],
  templateUrl: './select-group.html',
  styleUrl: './select-group.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectGroup),
      multi: true,
    },
  ],
})
export class SelectGroup {
  @Input() options: { label: string; value: any }[] = [];
  @Input() placeholder: string = 'Select...';

  isOpen = false;
  selectedValue: any = null;
  disabled = false;

  onChange: any = () => {};
  onTouched: any = () => {};

  constructor(private cdr: ChangeDetectorRef) {}

  writeValue(value: any): void {
    this.selectedValue = value;
    this.cdr.detectChanges();
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  toggleDropdown(): void {
    if (!this.disabled) this.isOpen = !this.isOpen;
  }

  get selectedLabel(): string {
    const selected = this.options.find((o) => o.value === this.selectedValue);
    return selected ? selected.label : this.placeholder;
  }

  selectOption(option: any): void {
    if (this.disabled) return;
    this.selectedValue = option.value;
    this.onChange(option.value);
    this.onTouched();
    this.isOpen = false;
    this.cdr.detectChanges();
  }

  isSelected(option: any): boolean {
    return this.selectedValue === option.value;
  }
}
