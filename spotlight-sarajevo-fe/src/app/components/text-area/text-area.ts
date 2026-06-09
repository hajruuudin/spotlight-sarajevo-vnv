import { NgClass } from '@angular/common';
import { Component, forwardRef, Input } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-text-area',
  imports: [NgClass],
  templateUrl: './text-area.html',
  styleUrl: './text-area.css',
  providers: [
  {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => TextArea),
    multi: true
  }
]
})
export class TextArea implements ControlValueAccessor {
  @Input() public placeholder: String = 'Enter...';
  @Input() public id: String = 'defaultId';
  @Input() public value: String = 'defautValue';
  @Input() public type: String = 'text';
  @Input() public classAddons: String = '';

  onChange = (value: string) => {};
  onTouched = () => {};

  writeValue(value: string): void {
    this.value = value ?? '';
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {}

  onInput(event: Event): void {
    const value = (event.target as HTMLTextAreaElement).value;
    this.value = value;
    this.onChange(value);
  }
}
