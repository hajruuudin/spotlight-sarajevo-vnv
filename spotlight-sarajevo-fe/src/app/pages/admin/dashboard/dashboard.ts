import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PageHeader } from "../../../components/page-header/page-header";
import { CommonModule } from '@angular/common';
import { CommunityRequestCard } from '../../../components/community-request-card/community-request-card';
import { SpotService } from '../../../services/spot.service';
import { EventService } from '../../../services/event.service';
import { CommunityRequestService } from '../../../services/community.request.service';
import { SpotShorthandModel } from '../../../shared/models/spot.model';
import { EventShorthandModel } from '../../../shared/models/event.model';
import { CommunityRequestModel } from '../../../shared/models/community.request.model';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  imports: [PageHeader, CommonModule, CommunityRequestCard],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class AdminDashboard implements OnInit {
  // Recently added data
  recentSpots: SpotShorthandModel[] = [];
  recentEvents: EventShorthandModel[] = [];
  recentCommunityRequests: CommunityRequestModel[] = [];

  // Total counts
  spotsTotal: number = 0;
  eventsTotal: number = 0;

  // Loading states
  isLoadingSpots = true;
  isLoadingEvents = true;
  isLoadingRequests = true;
  isLoadingCounts = true;

  // Error states
  spotsError = false;
  eventsError = false;
  requestsError = false;
  countsError = false;

  constructor(
    private spotService: SpotService,
    private eventService: EventService,
    private communityRequestService: CommunityRequestService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
    this.cdr.detectChanges();
  }

  private loadDashboardData(): void {
    forkJoin({
      recentSpots: this.spotService.getRecentlyAddedSpots(5).pipe(
        catchError((error) => {
          this.spotsError = true;
          return of([]);
        })
      ),
      recentEvents: this.eventService.getRecentlyAddedEvents(5).pipe(
        catchError((error) => {
          this.eventsError = true;
          return of([]);
        })
      ),
      recentRequests: this.communityRequestService.getRecentlyAddedCommunityRequests(10).pipe(
        catchError((error) => {
          this.requestsError = true;
          return of([]);
        })
      ),
      spotCount: this.spotService.getSpotsTotalCount().pipe(
        catchError((error) => {
          this.countsError = true;
          return of(0);
        })
      ),
      eventCount: this.eventService.getEventsTotalCount().pipe(
        catchError((error) => {
          this.countsError = true;
          return of(0);
        })
      )
    }).subscribe({
      next: (results) => {
        this.recentSpots = results.recentSpots || [];
        this.recentEvents = results.recentEvents || [];
        this.recentCommunityRequests = results.recentRequests || [];
        this.spotsTotal = results.spotCount || 0;
        this.eventsTotal = results.eventCount || 0;

        this.isLoadingSpots = false;
        this.isLoadingEvents = false;
        this.isLoadingRequests = false;
        this.isLoadingCounts = false;

        this.cdr.detectChanges();
      },
      error: (error) => {
        this.isLoadingSpots = false;
        this.isLoadingEvents = false;
        this.isLoadingRequests = false;
        this.isLoadingCounts = false;
      }
    });
  }
}
