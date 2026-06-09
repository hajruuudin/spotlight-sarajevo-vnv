import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-calendar-date-icon',
  imports: [NgClass],
  templateUrl: './calendar-date-icon.html',
  styleUrl: './calendar-date-icon.css',
})
export class CalendarDateIcon {
  @Input() day: any = {};
  @Input() isSelectedDay: boolean = false;
  @Output() daySelected: EventEmitter<string> = new EventEmitter<string>();

  onDayClicked() {
    console.log('Day clicked:', this.day);
    this.daySelected.emit(this.day.queryDate);
  }
}
