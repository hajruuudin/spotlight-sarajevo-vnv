import { Component, EventEmitter, forwardRef, Input, Output } from '@angular/core';
import { SearchIcon } from '../../resources/icons/search-icon/search-icon';
import { TranslocoPipe } from '@ngneat/transloco';
import {
  ControlValueAccessor,
  FormControl,
  NG_VALUE_ACCESSOR,
  ReactiveFormsModule,
} from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  imports: [SearchIcon, TranslocoPipe, ReactiveFormsModule],
  templateUrl: './search-bar.html',
  styleUrl: './search-bar.css',
  host: {
    class: 'w-full',
  },
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SearchBar),
      multi: true,
    },
  ],
})
export class SearchBar implements ControlValueAccessor {
  @Input() placeholder: string = 'Search object...';
  @Input() style: 'DEFAULT' | 'ADMIN' = 'DEFAULT';
  @Output() search: EventEmitter<string> = new EventEmitter<string>();

  value: string = '';

  onChange = (value: string) => {};
  onTouched = () => {};

  writeValue(value: string): void {
    this.value = value || '';
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {}

  onInput(event: Event) {
    const inputValue = (event.target as HTMLInputElement).value;
    this.value = inputValue;
    this.onChange(inputValue);
  }

  onSearchPressed() {
    this.onTouched();
    this.search.emit(this.value);
  }
}
