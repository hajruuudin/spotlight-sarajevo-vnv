import { Pipe, PipeTransform } from '@angular/core';

/**
 * A pipe that transforms a numeric value, returning a default string if the value is zero or not a number.
 *
 * Usage in template:
 *   {{ value | zeroReview:'No Reviews' }}
 *
 * If `value` is 0, null, undefined, or not a number, it will display 'No Reviews'.
 * Otherwise, it will display the numeric value.
 */
@Pipe({
  name: 'zeroReview',
  standalone: true
})
export class ZeroReview implements PipeTransform {

  transform(value: number | string | null | undefined, defaultValue: string = 'N/A'): string | number {
    const numericValue = Number(value);

    if (isNaN(numericValue) || numericValue === 0) {
      return defaultValue;
    }

    return numericValue;
  }
}
