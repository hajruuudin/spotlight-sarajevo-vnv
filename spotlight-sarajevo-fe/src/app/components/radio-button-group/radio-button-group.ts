import { Component, forwardRef } from '@angular/core';
import { RadioButton } from '../radio-button/radio-button';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-radio-button-group',
  imports: [],
  templateUrl: './radio-button-group.html',
  styleUrl: './radio-button-group.css',
  host: {
    class: 'w-full'
  },
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RadioButtonGroup),
    multi: true
  }]
})
export class RadioButtonGroup {
  value: any;
  disabled = false;

  radios: RadioButton[] = [];

  onChange = (_: any) => {};
  onTouched = () => {};

  register(radio: RadioButton) {
    this.radios.push(radio);
    radio.update(this.value, this.disabled);
  }

  select(value: any) {
    this.value = value;
    this.onChange(value);
    this.updateRadios();
  }

  writeValue(value: any): void {
    this.value = value;
    this.updateRadios();
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
    this.updateRadios();
  }

  private updateRadios() {
    this.radios.forEach(r =>
      r.update(this.value, this.disabled)
    );
  }
}
