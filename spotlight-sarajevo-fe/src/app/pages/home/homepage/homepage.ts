import {
  ChangeDetectorRef,
  Component,
  computed,
  effect,
  ElementRef,
  HostListener,
  OnInit,
} from '@angular/core';
import { PageHeader } from '../../../components/page-header/page-header';
import { HeadlineEvent } from '../../../components/headline-event/headline-event';
import { EventShorthandModel } from '../../../shared/models/event.model';
import { SpotShorthandModel } from '../../../shared/models/spot.model';
import { HeadlineSpot } from '../../../components/headline-spot/headline-spot';
import { SmallSpotCard } from '../../../components/small-spot-card/small-spot-card';
import { TranslocoPipe } from '@ngneat/transloco';
import { SearchSpotCard } from '../../../components/search-spot-card/search-spot-card';
import { CalendarDateIcon } from '../../../components/calendar-date-icon/calendar-date-icon';
import { SearchEventCard } from '../../../components/search-event-card/search-event-card';
import { HistoricalSpotCard } from '../../../components/historical-spot-card/historical-spot-card';
import { EventCategoryModel, SpotCategoryModel } from '../../../shared/models/category.model';
import { CategoryService } from '../../../services/category.service';
import { CategoryCard } from '../../../components/category-card/category-card';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';
import { SessionService } from '../../../core/services/session.service';
import { SpotService } from '../../../services/spot.service';
import { SortOptions } from '../../../shared/constants/SortOptions';
import { HotToastService } from '@ngxpert/hot-toast';
import { EventService } from '../../../services/event.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HomepagePageData } from '../../../core/resolvers/homepage.resolver';
import { Subheading } from "../../../components/subheading/subheading";
import { NotFoundComponent } from "../../../components/not-found-component/not-found-component";
import { SpinnerSmallComponent } from "../../../components/spinner-small-component/spinner-small-component";

@Component({
  selector: 'app-homepage',
  imports: [
    PageHeader,
    HeadlineEvent,
    HeadlineSpot,
    SmallSpotCard,
    TranslocoPipe,
    SearchSpotCard,
    CalendarDateIcon,
    SearchEventCard,
    HistoricalSpotCard,
    CategoryCard,
    ButtonPrimary,
    Subheading,
    NotFoundComponent,
    SpinnerSmallComponent
],
  templateUrl: './homepage.html',
  styleUrl: './homepage.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class Homepage implements OnInit {
  headlineSpot: SpotShorthandModel | null = null;
  headlineEvent: EventShorthandModel | null = null;
  favouriteSpots: SpotShorthandModel[] = [];
  popularSpots: SpotShorthandModel[] = [];
  upcomingEvents: EventShorthandModel[] = [];
  landmarkSpots: SpotShorthandModel[] = [];
  spotCategories: SpotCategoryModel[] = [];
  eventCategories: EventCategoryModel[] = [];

  constructor(
    protected spotService: SpotService,
    protected eventService: EventService,
    protected session: SessionService,
    protected cdr: ChangeDetectorRef,
    protected route: ActivatedRoute,
    protected router: Router,
    protected categoryService: CategoryService,
    protected toastr: HotToastService,
    protected spinner: SpinnerService,
  ) {
    effect(() => {
      this.session.language();
      this.loadQueryAndDisplayDays();
    });
  }

  protected selectedDate: string = '';
  public eventCalendarDays: any = [];
  public eventsForSelectedDate: EventShorthandModel[] = [];
  public isLoadingEventsForDate: boolean = false;

  ngOnInit(): void {
    const data = this.route.snapshot.data['homepageData'] as HomepagePageData;

    this.headlineSpot = data.headlineSpot;
    this.headlineEvent = data.headlineEvent;
    this.favouriteSpots = data.favouriteSpots;
    this.popularSpots = data.popularSpots;
    this.upcomingEvents = data.upcomingEvents;
    this.landmarkSpots = data.landmarkSpots;
    this.spotCategories = data.spotCategories;
    this.eventCategories = data.eventCategories;
  }

  loadQueryAndDisplayDays() {
    this.eventCalendarDays = [];
    let date = new Date();
    const daysOfWeekEn = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    const daysOfWeekBs = ['Ned', 'Pon', 'Uto', 'Sri', 'Čet', 'Pet', 'Sub'];

    for (let i = 0; i < 7; i++) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const dayOfMonthPadded = String(date.getDate()).padStart(2, '0');
      const formattedDate = `${year}-${month}-${dayOfMonthPadded}`;
      const dayOfWeekIndex = date.getDay();
      if (this.session.language() === 'ba') {
        const dayOfWeekBsLocalized = daysOfWeekBs[dayOfWeekIndex];
        const dayOfMonth = date.getDate();

        this.eventCalendarDays.push({
          queryDate: formattedDate,
          displayInfo: { day: dayOfWeekBsLocalized, date: dayOfMonth },
        });
        date.setDate(date.getDate() + 1);
        continue;
      }
      const dayOfWeek = daysOfWeekEn[dayOfWeekIndex];
      const dayOfMonth = date.getDate();

      this.eventCalendarDays.push({
        queryDate: formattedDate,
        displayInfo: { day: dayOfWeek, date: dayOfMonth },
      });

      date.setDate(date.getDate() + 1);
    }

    this.selectedDate = this.eventCalendarDays[0].queryDate;
    this.loadEventsForDate(this.selectedDate);
  }

  handleDaySelection(selectedQueryDate: string) {
    this.selectedDate = selectedQueryDate;
    this.loadEventsForDate(selectedQueryDate);
  }

  loadEventsForDate(date: string) {
    this.isLoadingEventsForDate = true;
    this.eventService.findEventsOnDay(date).subscribe({
      next: (events) => {
        this.eventsForSelectedDate = events.slice(0, 2);
        this.isLoadingEventsForDate = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.eventsForSelectedDate = [];
        this.isLoadingEventsForDate = false;
        this.cdr.markForCheck();
      }
    });
  }

  navigateToSpotOverview(spotSlug: string): void {
    this.spinner.showNavigateSpinner();
    this.router.navigate(['/spots/' + spotSlug]);
    this.spinner.hideNavigateSpinner();
  }

  navigateToEventOverview(eventSlug: string): void {
    this.spinner.showNavigateSpinner();
    this.router.navigate(['/events/' + eventSlug]);
    this.spinner.hideNavigateSpinner();
  }
}
