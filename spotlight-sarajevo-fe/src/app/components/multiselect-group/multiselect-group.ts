import { Component, forwardRef, Input } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-multiselect-group',
  imports: [],
  templateUrl: './multiselect-group.html',
  styleUrl: './multiselect-group.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MultiSelectGroup),
      multi: true,
    },
  ],
})
export class MultiSelectGroup {
  @Input() options: { label: string; value: any }[] = [];
  @Input() placeholder: string = 'Select...';
  @Input() minSelections: number = 0;
  @Input() maxSelections: number = Infinity;

  isOpen = false;
  selectedValues: any[] = [];
  disabled = false;

  onChange: any = () => {};
  onTouched: any = () => {};

  constructor(private cdr: ChangeDetectorRef) {}

  writeValue(values: any[]): void {
    this.selectedValues = Array.isArray(values) ? [...values] : [];
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
    if (this.selectedValues.length === 0) return this.placeholder;
    return `${this.selectedValues.length} selected`;
  }

  toggleOption(option: any): void {
    if (this.disabled) return;

    const index = this.selectedValues.findIndex((v) => v === option.value);

    if (index > -1) {
      if (this.selectedValues.length <= this.minSelections) {
        return;
      }
      this.selectedValues.splice(index, 1);
    } else {
      if (this.selectedValues.length >= this.maxSelections) {
        return;
      }
      this.selectedValues.push(option.value);
    }

    this.onChange([...this.selectedValues]);
    this.onTouched();
    this.cdr.detectChanges();
  }

  isSelected(option: any): boolean {
    return this.selectedValues.includes(option.value);
  }

  isOptionDisabled(option: any): boolean {
    return !this.isSelected(option) && this.selectedValues.length >= this.maxSelections;
  }

  canRemoveTag(): boolean {
    return !this.disabled && this.selectedValues.length > this.minSelections;
  }

  removeTag(value: any): void {
    if (!this.canRemoveTag()) {
      return;
    }

    const index = this.selectedValues.indexOf(value);
    if (index > -1) {
      this.selectedValues.splice(index, 1);
      this.onChange([...this.selectedValues]);
      this.cdr.detectChanges();
    }
  }

  getOptionLabel(value: any): string {
    const option = this.options.find((o) => o.value === value);
    return option ? option.label : String(value);
  }

  get isValid(): boolean {
    return (
      this.selectedValues.length >= this.minSelections &&
      this.selectedValues.length <= this.maxSelections
    );
  }

  get validationMessage(): string {
    if (this.selectedValues.length < this.minSelections) {
      return `Select at least ${this.minSelections} items (${this.selectedValues.length}/${this.minSelections})`;
    }
    if (this.selectedValues.length > this.maxSelections) {
      return `Maximum ${this.maxSelections} items allowed`;
    }
    return '';
  }
}
