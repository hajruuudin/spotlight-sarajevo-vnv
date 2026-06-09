import { Component, Input } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-slider-input',
  imports: [],
  templateUrl: './slider-input.html',
  styleUrl: './slider-input.css',
  host: {
    class: 'w-3/4'
  },
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: SliderInput,
      multi: true
    }
  ]
})
export class SliderInput implements ControlValueAccessor {
  @Input() min: number = 1
  @Input() max: number = 10
  @Input() step: number = 0.5

  @Input() sliderClass: string = "";
  @Input() valueClass: string = "";

  // All sliders start at 5.5, the average value
  value: number = 5.5
  onChange = (value: number) => {};
  onTouched = () => {};

  writeValue(value: number): void {
    if (value !== undefined && value !== null) {
      this.value = value;
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  handleChange(event: Event) {
    const newValue = Number((event.target as HTMLInputElement).value);
    this.value = newValue;
    this.onChange(newValue);
  }
}
