import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PageHeader } from '../../../components/page-header/page-header';
import {
  EventOrganiserModel,
  EventOrganiserReviewModel,
  EventOrganiserUpdateModel,
  EventOverviewModel,
  EventShorthandModel,
  EventUpdateModel,
} from '../../../shared/models/event.model';
import { PageResponseModel } from '../../../shared/models/shared.model';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { EventService } from '../../../services/event.service';
import { ReviewService } from '../../../services/review.service';
import { SessionService } from '../../../core/services/session.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { ModalService } from '../../../core/services/modal.service';
import { SortOptions } from '../../../shared/constants/SortOptions';
import { DeleteModal } from '../../../components/modals/delete-modal/delete-modal';
import { SearchBar } from '../../../components/search-bar/search-bar';
import { EventOverviewTable } from '../../../components/admin-overview-base-table/admin-overview-entity-tables/event-overview-table/event-overview-table';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-admin-event-overview',
  imports: [PageHeader, ReactiveFormsModule, SearchBar, EventOverviewTable],
  templateUrl: './admin-event-overview.html',
  styleUrl: './admin-event-overview.css',
})
export class AdminEventOverview implements OnInit {
  tableDefinitions: string[] = [
    'ID',
    'SLUG',
    'OFFICIAL NAME',
    'SMALL DESCRIPTION',
    'CATEGORY NAME',
    'START DATE',
  ];

  tableSelectedItem: EventOverviewModel | null = null;
  tableShorthandData: EventShorthandModel[] = [];
  tableEventOrganiserReviews: EventOrganiserReviewModel[] = [];
  isLoadingReviews: boolean = false;
  tableSearchForm: FormGroup;

  currentPage: number = 0;
  pageSize: number = 5;
  totalItems: number = 999;
  totalPages: number = 999;

  constructor(
    protected eventService: EventService,
    protected reviewService: ReviewService,
    protected sessionService: SessionService,
    protected spinnerService: SpinnerService,
    protected toastr: HotToastService,
    protected fb: FormBuilder,
    protected cdr: ChangeDetectorRef,
    protected modal: ModalService,
  ) {
    this.tableSearchForm = this.fb.group({
      searchTerm: [''],
    });
  }

  ngOnInit(): void {
    this.loadEvents()
  }

  handleEventSearch() {
    this.loadEvents()
  }

  handleOverviewSelect(eventId: number): void {
    const event = this.tableShorthandData.find((e) => e.id === eventId);
    if (event?.slug) {
      this.eventService.findEventOverview(event.slug).subscribe({
        next: (overview) => {
          this.tableSelectedItem = overview;
          this.handleReviewLoad(overview.organiser.id, SortOptions.RATING);
          this.cdr.detectChanges();
        },
      });
    }
  }

  handleReviewLoad(organiserId: number, sortOption: SortOptions | string): void {
    this.isLoadingReviews = true;
    this.reviewService.findAllEventOrganiserReviews(0, 10, organiserId, sortOption).subscribe({
      next: (response: PageResponseModel<EventOrganiserReviewModel>) => {
        this.tableEventOrganiserReviews = response.content;
        this.isLoadingReviews = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.isLoadingReviews = false;
        this.cdr.detectChanges();
      },
    });
  }

  handleUpdateItem(finalPayload: EventUpdateModel): void {
    this.eventService.updateEvent(finalPayload).subscribe({
      next: () => {
        this.spinnerService.hideNavigateSpinner();
        this.toastr.success('Event updated successfully!');
        this.handleOverviewSelect(finalPayload.id);
      },
      error: (error) => {
        this.spinnerService.hideNavigateSpinner();
        this.toastr.error('Failed to update event. Please try again.');
      },
    });
  }

  handleOrganiserUpdateItem(finalPayload: EventOrganiserUpdateModel){
    this.spinnerService.showNavigateSpinner()
    this.eventService.updateEventOrganiser(finalPayload).subscribe({
      next: (result : EventOrganiserModel) => {
        this.spinnerService.hideNavigateSpinner()
        this.toastr.success("Organiser updated")
        this.handleOverviewSelect(this.tableSelectedItem!.id)
      },
      error: (error : HttpErrorResponse) => {
        this.spinnerService.hideNavigateSpinner()
        this.toastr.error("Error while updating the organiser. Reffer to logs for more")
      }
    })
  }

  async handleDeleteItem(eventId: number): Promise<void> {
    const result = await this.modal.openAsync<{ confirmed: boolean }>(DeleteModal, {
      titleKey: 'admin.eventOverview.delete',
      confirmMessageKey: 'admin.eventOverview.deleteConfirm',
    });

    if (!result.confirmed) return;

    this.spinnerService.showSectionSpinner();
    this.eventService.deleteEvent(eventId).subscribe({
      next: () => {
        this.spinnerService.hideSectionSpinner();
        this.toastr.success('Event deleted successfully!');
        this.tableSelectedItem = null;
        this.tableEventOrganiserReviews = [];
        this.cdr.detectChanges();
        this.loadEvents();
      },
      error: (error) => {
        this.spinnerService.hideSectionSpinner();
        this.toastr.error('Failed to delete event. Please try again.');
      },
    });
  }

  handleNextPage(page: number): void {
    this.currentPage++;
    this.loadEvents();
  }

  handlePreviousPage(page: number): void {
    this.currentPage--;
    this.loadEvents();
  }

  handleReviewLoadMore(page: number): void {
    if (this.tableSelectedItem) {
      this.isLoadingReviews = true;
      this.reviewService
        .findAllEventOrganiserReviews(page, 10, this.tableSelectedItem.organiser.id, SortOptions.RATING)
        .subscribe({
          next: (response: PageResponseModel<EventOrganiserReviewModel>) => {
            this.tableEventOrganiserReviews = [...this.tableEventOrganiserReviews, ...response.content];
            this.isLoadingReviews = false;
            this.cdr.detectChanges();
          },
          error: (error) => {
            this.isLoadingReviews = false;
            this.toastr.error('Failed to load more reviews');
            this.cdr.detectChanges();
          },
        });
    }
  }

  handleReviewSortChange(sortOption: string): void {
    if (this.tableSelectedItem) {
      this.isLoadingReviews = true;
      this.reviewService
        .findAllEventOrganiserReviews(0, 10, this.tableSelectedItem.organiser.id, sortOption)
        .subscribe({
          next: (response: PageResponseModel<EventOrganiserReviewModel>) => {
            this.tableEventOrganiserReviews = response.content;
            this.isLoadingReviews = false;
            this.cdr.detectChanges();
          },
          error: (error) => {
            this.isLoadingReviews = false;
            this.toastr.error('Failed to sort reviews');
            this.cdr.detectChanges();
          },
        });
    }
  }

  private loadEvents(): void {
    this.eventService
      .findEventsPaginated(
        this.currentPage, 
        this.pageSize, 
        this.tableSearchForm.get('searchTerm')?.value, 
        SortOptions.ALPHABETICAL, 
        [])
      .subscribe({
        next: (response) => {
          this.tableShorthandData = response.content;
          this.totalItems = response.totalElements;
          this.totalPages = response.totalPages;
          this.cdr.detectChanges();
        },
      });
  }
}
