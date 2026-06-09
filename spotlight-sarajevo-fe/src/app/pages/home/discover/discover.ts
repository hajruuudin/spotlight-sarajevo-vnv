import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PageHeader } from '../../../components/page-header/page-header';
import { TranslocoPipe } from '@ngneat/transloco';
import { HotToastService } from '@ngxpert/hot-toast';
import { HistoricalSpotCard } from '../../../components/historical-spot-card/historical-spot-card';
import { SpotShorthandModel } from '../../../shared/models/spot.model';
import { EventShorthandModel } from '../../../shared/models/event.model';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';
import { SmallSpotCard } from '../../../components/small-spot-card/small-spot-card';
import { SmallEventCard } from '../../../components/small-event-card/small-event-card';
import { SpotService } from '../../../services/spot.service';
import { SessionService } from '../../../core/services/session.service';
import { EventService } from '../../../services/event.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DiscoverPageData } from '../../../core/resolvers/discover.resolver';
import { Subheading } from "../../../components/subheading/subheading";
import { SpinnerService } from '../../../core/services/spinner.service';

@Component({
  selector: 'app-discover',
  imports: [
    PageHeader,
    TranslocoPipe,
    ButtonPrimary,
    SmallSpotCard,
    HistoricalSpotCard,
    SmallEventCard,
    Subheading
],
  templateUrl: './discover.html',
  styleUrl: './discover.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class Discover implements OnInit {
  recentlyAddedSpots: SpotShorthandModel[] = [];
  landmarkSpots: SpotShorthandModel[] = [];
  popularSpots: SpotShorthandModel[] = [];
  upcomingEvents: EventShorthandModel[] = [];
  favouriteSpots: SpotShorthandModel[] = [];

  constructor(
    protected spotService: SpotService,
    protected eventService: EventService,
    protected route: ActivatedRoute,
    protected router: Router,
    protected session: SessionService,
    protected cdr: ChangeDetectorRef,
    protected toastr: HotToastService,
    protected spinnerService: SpinnerService,
  ) {}

  ngOnInit(): void {
    const data = this.route.snapshot.data['discoverData'] as DiscoverPageData;

    this.recentlyAddedSpots = data.recentlyAddedSpots;
    this.landmarkSpots = data.landmarkSpots;
    this.upcomingEvents = data.upcomingEvents;
    this.favouriteSpots = data.favouriteSpots;
    this.popularSpots = data.popularSpots;
  }

  navigateToSpotOverview(spotSlug: string): void {
    this.spinnerService.showNavigateSpinner();
    this.router.navigate(['/spots/' + spotSlug]);
    this.spinnerService.hideNavigateSpinner();
  }

  navigateToEventOverview(eventSlug: string): void {
    this.spinnerService.showNavigateSpinner();
    this.router.navigate(['/events/' + eventSlug]);
    this.spinnerService.hideNavigateSpinner();
  }
}
