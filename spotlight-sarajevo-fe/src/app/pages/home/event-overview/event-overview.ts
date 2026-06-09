import { ChangeDetectorRef, Component, ElementRef, HostListener, NgZone } from '@angular/core';
import {
  EventOrganiserReviewCreateModel,
  EventOrganiserReviewModel,
  EventOrganiserReviewUpdateModel,
  EventOverviewModel,
} from '../../../shared/models/event.model';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../../../services/event.service';
import { ReviewService } from '../../../services/review.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { ModalService } from '../../../core/services/modal.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { SessionService } from '../../../core/services/session.service';
import { PageHeader } from '../../../components/page-header/page-header';
import { TranslocoPipe } from '@ngneat/transloco';
import { BnwLocationIcon } from '../../../resources/icons/bnw-location-icon/bnw-location-icon';
import { BnwCategoryIcon } from '../../../resources/icons/bnw-category-icon/bnw-category-icon';
import { Subheading } from '../../../components/subheading/subheading';
import { ImageCarousel } from '../../../components/image-carousel/image-carousel';
import { MapRegular } from '../../../components/map-regular/map-regular';
import { DatePipe } from '@angular/common';
import { BnwDateIcon } from '../../../resources/icons/bnw-date-icon/bnw-date-icon';
import { EventInfoCard } from '../../../components/event-info-card/event-info-card';
import { HttpErrorResponse } from '@angular/common/http';
import { PageResponseModel } from '../../../shared/models/shared.model';
import { NotFoundComponent } from '../../../components/not-found-component/not-found-component';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';
import { EditReviewModal } from '../../../components/modals/edit-review-modal/edit-review-modal';
import { DeleteReviewModal } from '../../../components/modals/delete-review-modal/delete-review-modal';
import { AddReviewModal } from '../../../components/modals/add-review-modal/add-review-modal';
import { OrganiserReiewCard } from '../../../components/organiser-reiew-card/organiser-reiew-card';
import { AddToCollectionModal } from '../../../components/modals/add-to-collection-modal/add-to-collection-modal';
import { CollectionService } from '../../../services/collection.service';
import { CollectionAddItemModel } from '../../../shared/models/collection.model';
import { SortingSelector } from '../../../components/sorting-selector/sorting-selector';

@Component({
  selector: 'app-event-overview',
  imports: [
    PageHeader,
    TranslocoPipe,
    BnwLocationIcon,
    BnwCategoryIcon,
    Subheading,
    ImageCarousel,
    MapRegular,
    DatePipe,
    BnwDateIcon,
    EventInfoCard,
    NotFoundComponent,
    ButtonPrimary,
    OrganiserReiewCard,
    SortingSelector,
  ],
  templateUrl: './event-overview.html',
  styleUrl: './event-overview.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class EventOverview {
  eventOverview!: EventOverviewModel;
  isAttended: Boolean = false;
  isSaved: boolean = false;
  images: string[] = []; // TEMPORARY FOR DEMONSTRATION

  headerContainer!: HTMLElement;

  userEventOrganiserReview: EventOrganiserReviewModel | null = null;
  eventOrganiserReviews: EventOrganiserReviewModel[] = [];

  reviewPageNumber: number = 0;
  reviewPageSize: number = 20;
  reviewSortOption: string = 'ALPHABETICAL';
  reviewSortOptions: string[] = ['ALPHABETICAL', 'ALPHABETICAL_DESC', 'RATING', 'RATING_DESC'];
  reviewSortTranslationKeys: { [key: string]: string } = {
    'ALPHABETICAL': 'eventOverview.sortAlphabetical',
    'ALPHABETICAL_DESC': 'eventOverview.sortAlphabeticalDesc',
    'RATING': 'eventOverview.sortRating',
    'RATING_DESC': 'eventOverview.sortRatingDesc'
  };

  get isPastEvent(): boolean {
    if (!this.eventOverview?.endDate) return false;
    return new Date(this.eventOverview.endDate) < new Date();
  }

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventService: EventService,
    protected collectionService: CollectionService,
    protected reviewService: ReviewService,
    protected el: ElementRef,
    protected toastr: HotToastService,
    protected modal: ModalService,
    protected spinner: SpinnerService,
    protected session: SessionService,
    protected cdr: ChangeDetectorRef,
    protected ngZone: NgZone,
  ) {}

  ngOnInit(): void {
    this.headerContainer = this.el.nativeElement.querySelector('#headerContainer');

    this.eventOverview = this.activatedRoute.snapshot.data['eventData'].event as EventOverviewModel;
    this.isAttended = this.activatedRoute.snapshot.data['eventData'].isAttended as Boolean;

    this.loadUserOrganiserReview(this.eventOverview.organiser.id);
    this.loadOtherOrganiserReviews(this.eventOverview.organiser.id);
    if(this.session.getUser() != null){
      this.checkIfPresentInSystemCollection();
    } 
  }

  @HostListener('document:scroll')
  scrollHeaderSlow(): void {
    if (!this.headerContainer) return;

    scrollY = document.documentElement.scrollTop || document.body.scrollTop;

    const parallaxOffset = scrollY * 0.3;

    this.headerContainer.style.transform = `translateY(${parallaxOffset}px)`;
  }

  loadUserOrganiserReview(organiserId: number) {
    if (this.session.getUser() == null) {
      return;
    } else {
      this.reviewService.findUserEventOrganiserReview(organiserId).subscribe({
        next: (response: EventOrganiserReviewModel) => {
          this.userEventOrganiserReview = response;
        },
        error: (response: HttpErrorResponse) => {
          // do something
        },
      });
    }
  }

  loadOtherOrganiserReviews(organiserId: number) {
    this.reviewService
      .findAllEventOrganiserReviews(
        this.reviewPageNumber,
        this.reviewPageSize,
        organiserId,
        this.reviewSortOption,
      )
      .subscribe({
        next: (response: PageResponseModel<EventOrganiserReviewModel>) => {
          const filteredResult = response.content.filter(
            (review) => review.userId != this.session.getUserId(),
          );
          this.eventOrganiserReviews = filteredResult;
        },
        error: (error: HttpErrorResponse) => {
          // do something
        },
      });
  }

  onReviewSortChange(sortOption: string) {
    this.reviewSortOption = sortOption;
    this.loadOtherOrganiserReviews(this.eventOverview.organiser.id);
  }

  async openAddModal() {
    const result = await this.modal.openAsync<{ type: string; data?: any }>(AddReviewModal, {
      organiserId: this.eventOverview.organiser.id,
      reviewType: false,
    });

    if (result?.type === 'cancel') return;
    if (result?.type === 'invalid') {
      this.toastr.info('All fields are required!');
      return;
    }

    if (result.type === 'add') {
      this.handleAddEditReview(result.data, false);
    }
  }

  async openEditModal() {
    const result = await this.modal.openAsync<{ type: string; data?: any }>(EditReviewModal, {
      organiserId: this.eventOverview.organiser.id,
      reviewModel: this.userEventOrganiserReview,
    });

    if (result?.type === 'cancel') return;
    if (result?.type === 'invalid') {
      this.toastr.info('All fields are required!');
      return;
    }

    if (result.type === 'edit') {
      this.handleAddEditReview(result.data, true);
    }
  }

  private handleAddEditReview(formData: any, isEdit: boolean) {
    if (!isEdit) {
      const reviewAdd = new EventOrganiserReviewCreateModel(
        formData.organiserId,
        formData.header,
        formData.body,
        formData.overallRating,
        formData.quality,
        formData.atmosphere,
        formData.enjoyability,
      );

      this.spinner.showNavigateSpinner();
      this.reviewService.addEventOrganiserReview(reviewAdd).subscribe({
        next: (review: EventOrganiserReviewModel) => {
          this.spinner.hideNavigateSpinner();
          this.toastr.success('Review Added!');
          this.ngZone.run(() => {
            this.userEventOrganiserReview = review;
          });
          this.cdr.markForCheck();
        },
        error: () => {
          this.toastr.error('There was an error :(');
        },
      });
    } else {
      const reviewEdit = new EventOrganiserReviewUpdateModel(
        this.userEventOrganiserReview!.id,
        formData.organiserId,
        this.userEventOrganiserReview!.userId,
        formData.header,
        formData.body,
        formData.overallRating,
        formData.quality,
        formData.atmosphere,
        formData.enjoyability,
      );

      this.spinner.showNavigateSpinner();
      this.reviewService.updateEventOrganiserReview(reviewEdit).subscribe({
        next: (review: EventOrganiserReviewModel) => {
          this.spinner.hideNavigateSpinner();
          this.toastr.success('Review Edited!');
          this.ngZone.run(() => {
            this.userEventOrganiserReview = review;
          });
          this.cdr.markForCheck();
        },
        error: () => {
          this.toastr.error('There was an error :(');
        },
      });
    }
  }

  async openDeleteReviewModal() {
    const result = await this.modal.openAsync<{ confirmed: boolean }>(DeleteReviewModal, {
      reviewType: false,
    });

    if (!result.confirmed) return;

    this.spinner.showNavigateSpinner();
    await this.reviewService
      .deleteEventOrganiserReview(
        this.eventOverview.organiser.id,
        this.userEventOrganiserReview!.id,
      )
      .subscribe({
        next: (response: EventOrganiserReviewModel) => {
          this.spinner.hideNavigateSpinner();
          this.toastr.success('Review deleted');
          this.ngZone.run(() => {
            this.userEventOrganiserReview = null;
          });
          this.cdr.markForCheck();
        },
        error: (response: HttpErrorResponse) => {
          this.toastr.error('Something went wrong, try again later!');
        },
      });
  }

  redirectToLogin() {
    this.router.navigate(['/auth/login'], {
      queryParams: {
        returnUrl: `/events/${this.eventOverview.slug}`,
      },
    });
  }

  async openAddCollectionModal() {
    const result = await this.modal.openAsync<{ type: string; data: any; confirmed: boolean }>(
      AddToCollectionModal,
      {
        objectId: this.eventOverview.id,
        objectType: 'EVENT',
      },
    );

    if (result.type == 'exit') return;

    if (result.type == 'success-remove') {
      this.toastr.success('Items removed!');
    } else if (result.type == 'success-add') {
      this.toastr.success('Items added!');
    } else if (result.type == 'success-both') {
      this.toastr.success('Changes made!');
    }
  }

  checkIfPresentInSystemCollection() {
    this.collectionService.checkIfPresentInCollection(this.eventOverview.id, 'EVENT').subscribe({
      next: (present) => (this.isSaved = present),
      error: (err) => console.error('Error checking saved status', err),
    });
  }

  saveToAllEvents() {
    const request: CollectionAddItemModel = {
      objectId: this.eventOverview.id,
      objectType: 'EVENT',
      collectionId: 0,
      isSystem: true,
    };

    this.collectionService.addItemToCollection(request).subscribe({
      next: () => {
        this.isSaved = true;
        this.toastr.success('Item saved!');
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error saving to system collection', err),
    });
  }

  removeFromAllEvents() {
    this.collectionService
      .removeItemFromCollection(0, this.eventOverview.id, 'EVENT', true)
      .subscribe({
        next: () => {
          this.isSaved = false;
          this.toastr.success('Item unsaved!');
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Error removing from system collection', err),
      });
  }

  markEventAsAttended() {
    this.eventService.addEventAsAttended(this.eventOverview.id).subscribe({
      next: () => {
        this.toastr.success('Event marked as attended!');
        this.isAttended = true;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error marking event as attended', err),
    });
  }

  unmarkEventAsAttended() {
    this.eventService.removeEventFromAttended(this.eventOverview.id).subscribe({
      next: () => {
        this.toastr.success('Event unmarked as attended!');
        this.isAttended = false;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error unmarking event as attended', err),
    });
  }
}
