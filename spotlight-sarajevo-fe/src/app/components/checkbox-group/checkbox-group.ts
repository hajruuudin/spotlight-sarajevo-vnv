import { NgClass, NgForOf } from '@angular/common';
import { Component, Input, forwardRef, ChangeDetectorRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

export interface CheckboxOption {
  label: string;
  value: any;
}

@Component({
  selector: 'app-checkbox-group',
  imports: [NgClass, NgForOf],
  templateUrl: './checkbox-group.html',
  styleUrls: ['./checkbox-group.css'],
  host: {
    class: 'w-full'
  },
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CollectionCheckboxGroupComponent),
      multi: true
    }
  ]
})
export class CollectionCheckboxGroupComponent implements ControlValueAccessor {
  @Input() options: CheckboxOption[] = [];

  selectedValues: any[] = [];
  disabled = false;

  onChange: any = () => {};
  onTouched: any = () => {};

  constructor(private cdr: ChangeDetectorRef) {}

  writeValue(values: any[]): void {
    this.selectedValues = Array.isArray(values) ? values : [];
    this.cdr.detectChanges();
  }

  registerOnChange(fn: any): void { this.onChange = fn; }
  registerOnTouched(fn: any): void { this.onTouched = fn; }
  setDisabledState(isDisabled: boolean): void { this.disabled = isDisabled; }

  isSelected(value: any): boolean {
    return this.selectedValues.includes(value);
  }

  toggleValue(option: CheckboxOption) {
    if (this.disabled) return;

    const index = this.selectedValues.indexOf(option.value);
    if (index > -1) {
      this.selectedValues.splice(index, 1);
    } else {
      this.selectedValues.push(option.value);
    }

    this.onChange([...this.selectedValues]); // emit new array
    this.onTouched();
    this.cdr.detectChanges();
  }
}
