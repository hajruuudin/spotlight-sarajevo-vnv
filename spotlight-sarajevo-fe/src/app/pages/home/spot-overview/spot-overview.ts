import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  HostListener,
  NgZone,
  OnInit,
} from '@angular/core';
import { SpotService } from '../../../services/spot.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { SpinnerService } from '../../../core/services/spinner.service';
import { ActivatedRoute, Router } from '@angular/router';
import {
  SpotOverviewModel,
  SpotReviewCreateModel,
  SpotReviewModel,
  SpotReviewUpdateModel,
  SpotWorkHoursModel,
} from '../../../shared/models/spot.model';
import { PageHeader } from '../../../components/page-header/page-header';
import { SessionService } from '../../../core/services/session.service';
import { Subheading } from '../../../components/subheading/subheading';
import { ImageCarousel } from '../../../components/image-carousel/image-carousel';
import { ChartConfiguration } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { MapRegular } from '../../../components/map-regular/map-regular';
import { SpotReviewCard } from '../../../components/spot-review-card/spot-review-card';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';
import { ModalService } from '../../../core/services/modal.service';
import { AddReviewModal } from '../../../components/modals/add-review-modal/add-review-modal';
import { HttpErrorResponse } from '@angular/common/http';
import { NotFoundComponent } from '../../../components/not-found-component/not-found-component';
import { DeleteReviewModal } from '../../../components/modals/delete-review-modal/delete-review-modal';
import { EditReviewModal } from '../../../components/modals/edit-review-modal/edit-review-modal';
import { PageResponseModel } from '../../../shared/models/shared.model';
import { ReviewService } from '../../../services/review.service';
import { DecimalPipe } from '@angular/common';
import { TranslocoPipe, TranslocoService } from '@ngneat/transloco';
import { BnwRatingIcon } from '../../../resources/icons/bnw-rating-icon/bnw-rating-icon';
import { BnwLocationIcon } from '../../../resources/icons/bnw-location-icon/bnw-location-icon';
import { BnwCategoryIcon } from '../../../resources/icons/bnw-category-icon/bnw-category-icon';
import { AddToCollectionModal } from '../../../components/modals/add-to-collection-modal/add-to-collection-modal';
import { CollectionService } from '../../../services/collection.service';
import { CollectionAddItemModel } from '../../../shared/models/collection.model';
import { ZeroReview } from '../../../shared/pipes/zero-review-pipe';
import { SortingSelector } from '../../../components/sorting-selector/sorting-selector';

@Component({
  selector: 'app-spot-overview',
  imports: [
    PageHeader,
    ImageCarousel,
    Subheading,
    BaseChartDirective,
    MapRegular,
    SpotReviewCard,
    ButtonPrimary,
    NotFoundComponent,
    DecimalPipe,
    ZeroReview,
    TranslocoPipe,
    BnwRatingIcon,
    BnwLocationIcon,
    BnwCategoryIcon,
    SortingSelector,
  ],
  templateUrl: './spot-overview.html',
  styleUrl: './spot-overview.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class SpotOverview implements OnInit {
  spotOverview!: SpotOverviewModel;
  isSaved: boolean = false;
  isVisited: Boolean = false;
  images: string[] = [];

  headerContainer!: HTMLElement;
  formattedSpotWorkHours: SpotWorkHoursModel[] = [];

  barChartData: ChartConfiguration<'bar'>['data'] = { labels: [], datasets: [] };
  barChartOptions: ChartConfiguration<'bar'>['options'] = {};

  userReview: SpotReviewModel | null = null;
  spotReviews: SpotReviewModel[] = [];

  reviewPageNumber: number = 0;
  reviewPageSize: number = 20;
  reviewSortOption: string = 'ALPHABETICAL';
  reviewSortOptions: string[] = ['ALPHABETICAL', 'ALPHABETICAL_DESC', 'RATING', 'RATING_DESC'];
  reviewSortTranslationKeys: { [key: string]: string } = {
    'ALPHABETICAL': 'spotOverview.sortAlphabetical',
    'ALPHABETICAL_DESC': 'spotOverview.sortAlphabeticalDesc',
    'RATING': 'spotOverview.sortRating',
    'RATING_DESC': 'spotOverview.sortRatingDesc'
  };

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected spotService: SpotService,
    protected collectionService: CollectionService,
    protected reviewService: ReviewService,
    protected el: ElementRef,
    protected toastr: HotToastService,
    protected modal: ModalService,
    protected spinner: SpinnerService,
    protected session: SessionService,
    protected cdr: ChangeDetectorRef,
    protected ngZone: NgZone,
    protected transloco: TranslocoService,
  ) {}

  ngOnInit(): void {
    this.headerContainer = this.el.nativeElement.querySelector('#headerContainer');

    this.spotOverview = this.activatedRoute.snapshot.data['spotData'].spot as SpotOverviewModel;
    this.isVisited = this.activatedRoute.snapshot.data['spotData'].isVisited as Boolean;

    this.formatSpotWorkHours(this.spotOverview.workHours);
    this.initialiseRadarChart(this.session.language()!, this.session.theme()!);
    this.loadUserSpotReview(this.spotOverview.id);
    this.loadOtherSpotReviews(this.spotOverview.id);
    this.transformImageObjectToUrl()
    
    if (this.session.getUser() != null){
      this.checkIfPresentInSystemCollection();
    }
  }

  @HostListener('document:scroll')
  scrollHeaderSlow(): void {
    if (!this.headerContainer) return;

    const scrollY = document.documentElement.scrollTop || document.body.scrollTop;

    const parallaxOffset = scrollY * 0.3;

    this.headerContainer.style.transform = `translateY(${parallaxOffset}px)`;
  }

  formatSpotWorkHours(hours: SpotWorkHoursModel[]) {
    const DAYS = [
      { index: 1, name: 'Monday' },
      { index: 2, name: 'Tuesday' },
      { index: 3, name: 'Wednesday' },
      { index: 4, name: 'Thursday' },
      { index: 5, name: 'Friday' },
      { index: 6, name: 'Saturday' },
      { index: 7, name: 'Sunday' },
    ];

    const map = new Map(hours.map((h) => [h.dayIndex, h]));

    const result: SpotWorkHoursModel[] = DAYS.map((d) => {
      const found = map.get(d.index);
      return found
        ? found
        : new SpotWorkHoursModel(d.index, d.name, 'Closed', 'Closed', hours[0]?.spotId ?? 0);
    });

    this.formattedSpotWorkHours = result;
  }

  loadUserSpotReview(spotId: number) {
    if (this.session.getUser() == null) {
      return;
    } else {
      this.reviewService.findUserSpotReview(spotId).subscribe({
        next: (response: SpotReviewModel) => {
          this.userReview = response;
          this.cdr.markForCheck();
        },
        error: (response: HttpErrorResponse) => {
          // do something
          this.cdr.markForCheck();
        },
      });
    }
  }

  loadOtherSpotReviews(spotId: number) {
    this.reviewService
      .findAllSpotReviews(this.reviewPageNumber, this.reviewPageSize, spotId, this.reviewSortOption)
      .subscribe({
        next: (response: PageResponseModel<SpotReviewModel>) => {
          const filteredResult = response.content.filter(
            (review) => review.userId != this.session.getUserId(),
          );
          this.spotReviews = filteredResult;
          this.cdr.markForCheck();
        },
        error: (error: HttpErrorResponse) => {
          // do something
          this.cdr.markForCheck();
        },
      });
  }

  onReviewSortChange(sortOption: string) {
    this.reviewSortOption = sortOption;
    this.loadOtherSpotReviews(this.spotOverview.id);
  }

  async openAddModal() {
    const result = await this.modal.openAsync<{ type: string; data?: any }>(AddReviewModal, {
      spotId: this.spotOverview.id,
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
      spotId: this.spotOverview.id,
      reviewModel: this.userReview,
    });

    if (result?.type === 'cancel') return;
    if (result?.type === 'invalid') {
      this.toastr.info('All fields are required!');
      return;
    }

    if (result.type === 'add') {
      this.handleAddEditReview(result.data, false);
    } else if (result.type === 'edit') {
      this.handleAddEditReview(result.data, true);
    }
  }

  private handleAddEditReview(formData: any, isEdit: boolean) {
    if (!isEdit) {
      const reviewAdd = new SpotReviewCreateModel(
        formData.spotId,
        formData.header,
        formData.body,
        formData.overallRating,
        formData.atmosphere,
        formData.accessibility,
        formData.staffKindness,
        formData.affordability,
        formData.cleanliness,
        formData.localeQuality,
      );

      this.spinner.showNavigateSpinner();
      this.reviewService.addSpotReview(reviewAdd).subscribe({
        next: (review: SpotReviewModel) => {
          this.spinner.hideNavigateSpinner();
          this.toastr.success('Review Added!');
          this.ngZone.run(() => {
            this.userReview = review;
          });
          this.cdr.markForCheck();
        },
        error: () => {
          this.toastr.error('There was an error :(');
        },
      });
    } else {
      const reviewEdit = new SpotReviewUpdateModel(
        this.userReview!.id,
        this.userReview!.userId,
        formData.spotId,
        formData.header,
        formData.body,
        formData.overallRating,
        formData.atmosphere,
        formData.accessibility,
        formData.staffKindness,
        formData.affordability,
        formData.cleanliness,
        formData.localeQuality,
      );

      this.spinner.showNavigateSpinner();
      this.reviewService.updateSpotReview(reviewEdit).subscribe({
        next: (review: SpotReviewModel) => {
          this.spinner.hideNavigateSpinner();
          this.toastr.success('Review Edited!');
          this.ngZone.run(() => {
            this.userReview = review;
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
    const result = await this.modal.openAsync<{ confirmed: boolean }>(DeleteReviewModal, {});

    if (!result.confirmed) return;

    this.spinner.showNavigateSpinner();
    await this.reviewService.deleteSpotReview(this.spotOverview.id, this.userReview!.id).subscribe({
      next: (response: SpotReviewModel) => {
        this.spinner.hideNavigateSpinner();
        this.toastr.success('Review deleted');
        this.ngZone.run(() => {
          this.userReview = null;
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
        returnUrl: `/spots/${this.spotOverview.slug}`,
      },
    });
  }
  
  transformImageObjectToUrl(){
    if(this.spotOverview.images == null || this.spotOverview.images.length == 0) return;
    for(let image of this.spotOverview.images){
      this.images.push(image.imageUrl)
    }
  }

  initialiseRadarChart(lang: string, theme: string) {
    let textColor = theme == 'light' ? '#111111' : '#ffffff';
    let gridColor = theme == 'light' ? '#111111AA' : '#ffffff66';
    let labels =
      lang == 'en'
        ? [
            'Affordability 💸',
            'Accessibility 🚗',
            'Atmosphere 🎉',
            'Staff Kindness 😊',
            'Locale Quality 💯',
            'Cleanliness ✨',
          ]
        : [
            'Cjenovna Pristupačnost 💸',
            'Pristupačnost lokacije 🚗',
            'Atmosfera 🎉',
            'Kultura Osoblja 😊',
            'Kvalitet Prostorije 💯',
            'Čistoća ✨',
          ];
    this.barChartData.labels = labels;
    this.barChartData.datasets = [
      {
        label: 'Stats',
        data: [
          this.spotOverview.combinedAffordability,
          this.spotOverview.combinedAccessibility,
          this.spotOverview.combinedAtmosphere,
          this.spotOverview.combinedLocaleQuality,
          this.spotOverview.combinedStaffKindness,
          this.spotOverview.combinedCleanliness,
        ],
        backgroundColor: ['#056766', '#07777B', '#088891', '#0AA1A0', '#1BB7B5', '#33CDCB'],
        borderColor: '#e7fcfe',
        borderWidth: 0,
        borderRadius: 100,
        barThickness: 40,
      },
    ];

    this.barChartOptions = {
      responsive: true,
      maintainAspectRatio: false,
      indexAxis: 'x',
      scales: {
        x: {
          min: 0,
          max: 10,
          grid: {
            display: false,
          },
          ticks: {
            color: textColor,
            font: { size: 12, family: 'Kumbh Sans' },
            stepSize: 2,
          },
        },
        y: {
          min: 0,
          max: 10,
          grid: { color: gridColor },
          ticks: {
            color: textColor,
            font: {
              size: 16,
              weight: 'bold',
              family: 'Kumbh Sans',
            },
          },
        },
      },
      plugins: {
        legend: { display: false },
        tooltip: { enabled: false },
      },
    };
  }

  async openAddCollectionModal() {
    const result = await this.modal.openAsync<{ type: string; data: any; confirmed: boolean }>(
      AddToCollectionModal,
      {
        objectId: this.spotOverview.id,
        objectType: 'SPOT',
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
    this.collectionService.checkIfPresentInCollection(this.spotOverview.id, 'SPOT').subscribe({
      next: (present) => {
        this.isSaved = present;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error checking saved status', err);
        this.cdr.markForCheck();
      },
    });
  }

  saveToAllSpots() {
    const request: CollectionAddItemModel = {
      objectId: this.spotOverview.id,
      objectType: 'SPOT',
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

  removeFromAllSpots() {
    this.collectionService
      .removeItemFromCollection(0, this.spotOverview.id, 'SPOT', true)
      .subscribe({
        next: () => {
          this.isSaved = false;
          this.toastr.success('Item unsaved!');
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Error removing from system collection', err),
      });
  }

  markSpotAsVisited() {
    this.spotService.addSpotAsVisited(this.spotOverview.id).subscribe({
      next: () => {
        this.toastr.success('Spot marked as visited!');
        this.isVisited = true;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error marking spot as visited', err),
    });
  }

  removeSpotFromVisited() {
    this.spotService.removeSpotFromVisited(this.spotOverview.id).subscribe({
      next: () => {
        this.toastr.success('Spot unmarked as visited!');
        this.isVisited = false;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error unmarking spot as visited', err),
    });
  }
}
