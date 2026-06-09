import { NgClass } from '@angular/common';
import { ChangeDetectorRef, Component, forwardRef, Input, Optional } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { RadioButtonGroup } from '../radio-button-group/radio-button-group';

@Component({
  selector: 'app-radio-button',
  imports: [NgClass],
  standalone: true,
  templateUrl: './radio-button.html',
  styleUrl: './radio-button.css',
})
export class RadioButton {
  @Input() value: any;
  @Input() label = '';

  selected = false;
  disabled = false;

  constructor(@Optional() private group: RadioButtonGroup) {}

  ngOnInit() {
    this.group?.register(this);
  }

  update(groupValue: any, disabled: boolean) {
    this.selected = groupValue === this.value;
    this.disabled = disabled;
  }

  select() {
    if (!this.disabled) {
      this.group?.select(this.value);
    }
  }
}
