import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { NgClass } from '@angular/common';

export interface CalendarDay {
  date: number;
  month: number;
  year: number;
  queryDate: string;
  isCurrentMonth: boolean;
  isToday: boolean;
  isPast: boolean;
  isWeekend: boolean;
  hasEvents: boolean;
}

/**
 * Monthly calendar widget component that displays a 5-row grid with 7 days per row.
 * Supports month navigation, weekend highlighting, past-day dimming, and event dot indicators.
 * Emits the selected date when a day is clicked.
 *
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Component({
  selector: 'app-calendar-widget',
  standalone: true,
  imports: [NgClass],
  templateUrl: './calendar-widget.html',
  styleUrl: './calendar-widget.css',
  host: {
    class: 'block w-full',
  },
})
export class CalendarWidget implements OnInit, OnChanges {
  @Input() lang: string = 'en';
  @Input() eventDatesMap: { [date: string]: boolean } = {};

  @Output() dateSelected = new EventEmitter<string>();
  @Output() monthChanged = new EventEmitter<{ year: number; month: number }>();

  currentYear!: number;
  currentMonth!: number;
  selectedDate: string = '';
  calendarDays: CalendarDay[] = [];

  readonly weekDaysEn = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  readonly weekDaysBs = ['Pon', 'Uto', 'Sri', 'Čet', 'Pet', 'Sub', 'Ned'];

  readonly monthNamesEn = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December',
  ];
  readonly monthNamesBs = [
    'Januar', 'Februar', 'Mart', 'April', 'Maj', 'Juni',
    'Juli', 'August', 'Septembar', 'Oktobar', 'Novembar', 'Decembar',
  ];

  get weekDays(): string[] {
    return this.lang === 'ba' ? this.weekDaysBs : this.weekDaysEn;
  }

  get monthName(): string {
    return this.lang === 'ba'
      ? this.monthNamesBs[this.currentMonth]
      : this.monthNamesEn[this.currentMonth];
  }

  ngOnInit(): void {
    const today = new Date();
    this.currentYear = today.getFullYear();
    this.currentMonth = today.getMonth();
    this.selectedDate = this.formatDate(today.getFullYear(), today.getMonth() + 1, today.getDate());
    this.buildCalendar();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['eventDatesMap'] && !changes['eventDatesMap'].firstChange) {
      this.buildCalendar();
    }
  }

  buildCalendar(): void {
    this.calendarDays = [];
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const firstDayOfMonth = new Date(this.currentYear, this.currentMonth, 1);
    const lastDayOfMonth = new Date(this.currentYear, this.currentMonth + 1, 0);

    // Monday = 0, Sunday = 6 (ISO week)
    let startDayIndex = firstDayOfMonth.getDay() - 1;
    if (startDayIndex < 0) startDayIndex = 6; // Sunday wraps to 6

    const daysInMonth = lastDayOfMonth.getDate();

    // Fill leading days from previous month
    const prevMonthLastDay = new Date(this.currentYear, this.currentMonth, 0).getDate();
    for (let i = startDayIndex - 1; i >= 0; i--) {
      const day = prevMonthLastDay - i;
      const prevMonth = this.currentMonth === 0 ? 12 : this.currentMonth;
      const prevYear = this.currentMonth === 0 ? this.currentYear - 1 : this.currentYear;
      const queryDate = this.formatDate(prevYear, prevMonth, day);
      const dateObj = new Date(prevYear, prevMonth - 1, day);
      dateObj.setHours(0, 0, 0, 0);

      this.calendarDays.push({
        date: day,
        month: prevMonth - 1,
        year: prevYear,
        queryDate,
        isCurrentMonth: false,
        isToday: false,
        isPast: dateObj < today,
        isWeekend: this.isWeekendDay(dateObj),
        hasEvents: this.eventDatesMap[queryDate] || false,
      });
    }

    // Fill current month days
    for (let day = 1; day <= daysInMonth; day++) {
      const queryDate = this.formatDate(this.currentYear, this.currentMonth + 1, day);
      const dateObj = new Date(this.currentYear, this.currentMonth, day);
      dateObj.setHours(0, 0, 0, 0);

      this.calendarDays.push({
        date: day,
        month: this.currentMonth,
        year: this.currentYear,
        queryDate,
        isCurrentMonth: true,
        isToday: dateObj.getTime() === today.getTime(),
        isPast: dateObj < today,
        isWeekend: this.isWeekendDay(dateObj),
        hasEvents: this.eventDatesMap[queryDate] || false,
      });
    }

    // Fill trailing days to complete 5 rows (35 cells)
    const totalCells = 35;
    const remaining = totalCells - this.calendarDays.length;
    for (let i = 1; i <= remaining; i++) {
      const nextMonth = this.currentMonth + 2 > 12 ? 1 : this.currentMonth + 2;
      const nextYear = this.currentMonth + 2 > 12 ? this.currentYear + 1 : this.currentYear;
      const queryDate = this.formatDate(nextYear, nextMonth, i);
      const dateObj = new Date(nextYear, nextMonth - 1, i);
      dateObj.setHours(0, 0, 0, 0);

      this.calendarDays.push({
        date: i,
        month: nextMonth - 1,
        year: nextYear,
        queryDate,
        isCurrentMonth: false,
        isToday: false,
        isPast: dateObj < today,
        isWeekend: this.isWeekendDay(dateObj),
        hasEvents: this.eventDatesMap[queryDate] || false,
      });
    }

    // If we need more than 35 cells, extend to 42 (6 rows)
    if (this.calendarDays.length > 35) {
      const extra = 42 - this.calendarDays.length;
      const lastDay = this.calendarDays[this.calendarDays.length - 1];
      for (let i = 1; i <= extra; i++) {
        const d = lastDay.date + i;
        const nextMonth = this.currentMonth + 2 > 12 ? 1 : this.currentMonth + 2;
        const nextYear = this.currentMonth + 2 > 12 ? this.currentYear + 1 : this.currentYear;
        const queryDate = this.formatDate(nextYear, nextMonth, d);
        const dateObj = new Date(nextYear, nextMonth - 1, d);
        dateObj.setHours(0, 0, 0, 0);

        this.calendarDays.push({
          date: d,
          month: nextMonth - 1,
          year: nextYear,
          queryDate,
          isCurrentMonth: false,
          isToday: false,
          isPast: dateObj < today,
          isWeekend: this.isWeekendDay(dateObj),
          hasEvents: this.eventDatesMap[queryDate] || false,
        });
      }
    }
  }

  onDayClicked(day: CalendarDay): void {
    this.selectedDate = day.queryDate;
    this.dateSelected.emit(day.queryDate);
  }

  goToPreviousMonth(): void {
    if (this.currentMonth === 0) {
      this.currentMonth = 11;
      this.currentYear--;
    } else {
      this.currentMonth--;
    }
    this.buildCalendar();
    this.monthChanged.emit({ year: this.currentYear, month: this.currentMonth + 1 });
  }

  goToNextMonth(): void {
    if (this.currentMonth === 11) {
      this.currentMonth = 0;
      this.currentYear++;
    } else {
      this.currentMonth++;
    }
    this.buildCalendar();
    this.monthChanged.emit({ year: this.currentYear, month: this.currentMonth + 1 });
  }

  private formatDate(year: number, month: number, day: number): string {
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
  }

  private isWeekendDay(date: Date): boolean {
    const day = date.getDay();
    return day === 0 || day === 6;
  }
}
