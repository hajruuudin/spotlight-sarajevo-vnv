import { ChangeDetectorRef, Component, computed, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslocoPipe } from '@ngneat/transloco';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionService } from '../../../core/services/session.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { EventService } from '../../../services/event.service';
import { EventShorthandModel, EventDateCheckModel } from '../../../shared/models/event.model';
import { CalendarWidget } from '../../../components/calendar-widget/calendar-widget';
import { SearchEventCard } from '../../../components/search-event-card/search-event-card';
import { NotFoundComponent } from '../../../components/not-found-component/not-found-component';
import { SpinnerSmallComponent } from '../../../components/spinner-small-component/spinner-small-component';
import { PageHeader } from '../../../components/page-header/page-header';
import { EventCalendarResolverData } from '../../../core/resolvers/event.calendar.resolver';

@Component({
  selector: 'app-event-calendar-overview',
  imports: [
    TranslocoPipe,
    CalendarWidget,
    SearchEventCard,
    NotFoundComponent,
    SpinnerSmallComponent,
    PageHeader,
  ],
  templateUrl: './event-calendar-overview.html',
  styleUrl: './event-calendar-overview.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class EventCalendarOverview implements OnInit {
  eventDatesMap: EventDateCheckModel = {};
  eventsForDay: EventShorthandModel[] = [];
  selectedDate: string = '';
  isEventsLoading = false;

  constructor(
    protected eventService: EventService,
    protected session: SessionService,
    protected router: Router,
    protected route: ActivatedRoute,
    protected spinner: SpinnerService,
    protected cdr: ChangeDetectorRef,
  ) {}

  protected isSectionLoading = computed(() => this.spinner.loadingSection());

  ngOnInit(): void {
    // Get data from resolver
    const resolverData = this.route.snapshot.data['eventCalendarData'] as EventCalendarResolverData;
    
    if (resolverData) {
      this.eventDatesMap = resolverData.eventDatesMap;
      this.eventsForDay = resolverData.eventsForDay;
      this.selectedDate = resolverData.selectedDate;
      this.cdr.detectChanges();
    } else {
      // Fallback: load data manually if resolver didn't run
      const today = new Date();
      this.selectedDate = this.formatDate(today.getFullYear(), today.getMonth() + 1, today.getDate());

      this.loadEventDatesCheck(today.getFullYear(), today.getMonth() + 1);
      this.loadEventsForDate(this.selectedDate);
    }
  }

  onDateSelected(queryDate: string): void {
    this.selectedDate = queryDate;
    this.loadEventsForDate(queryDate);
  }

  onMonthChanged(event: { year: number; month: number }): void {
    this.loadEventDatesCheck(event.year, event.month);
  }

  loadEventDatesCheck(year: number, month: number): void {
    this.eventService.findEventDatesCheck(year, month).subscribe({
      next: (response: EventDateCheckModel) => {
        this.eventDatesMap = response;
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {},
    });
  }

  loadEventsForDate(date: string): void {
    this.isEventsLoading = true;
    this.spinner.showSectionSpinner();
    this.eventService.findEventsOnDay(date).subscribe({
      next: (response: EventShorthandModel[]) => {
        this.spinner.hideSectionSpinner();
        this.isEventsLoading = false;
        this.eventsForDay = response;
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.spinner.hideSectionSpinner();
        this.isEventsLoading = false;
        this.eventsForDay = [];
        this.cdr.detectChanges();
      },
    });
  }

  navigateToEventOverview(eventSlug: string): void {
    this.router.navigate(['/events/' + eventSlug]);
  }

  private formatDate(year: number, month: number, day: number): string {
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
  }
}
