import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TranslocoPipe } from '@ngneat/transloco';
import { PageHeader } from '../../../components/page-header/page-header';
import { SpotOverviewTable } from '../../../components/admin-overview-base-table/admin-overview-entity-tables/spot-overview-table/spot-overview-table';
import {
  SpotOverviewModel,
  SpotReviewModel,
  SpotShorthandModel,
  SpotUpdateModel,
} from '../../../shared/models/spot.model';
import { SpotService } from '../../../services/spot.service';
import { SessionService } from '../../../core/services/session.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { SortOptions } from '../../../shared/constants/SortOptions';
import { PageResponseModel } from '../../../shared/models/shared.model';
import { SpinnerService } from '../../../core/services/spinner.service';
import { ReviewService } from '../../../services/review.service';
import { SearchBar } from '../../../components/search-bar/search-bar';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ModalService } from '../../../core/services/modal.service';
import { DeleteModal } from '../../../components/modals/delete-modal/delete-modal';

@Component({
  selector: 'app-admin-spot-overview',
  imports: [TranslocoPipe, PageHeader, SpotOverviewTable, SearchBar, ReactiveFormsModule],
  templateUrl: './admin-spot-overview.html',
  styleUrl: './admin-spot-overview.css',
})
export class AdminSpotOverview implements OnInit {
  tableDefinitions: string[] = [
    'admin.spotOverview.tableKeys.id',
    'admin.spotOverview.tableKeys.slug',
    'admin.spotOverview.tableKeys.officialName',
    'admin.spotOverview.tableKeys.smallDescription',
    'admin.spotOverview.tableKeys.categoryName',
    'admin.spotOverview.tableKeys.combinedRating',
  ];

  tableSelectedItem: SpotOverviewModel | null = null;
  tableShorthandData: SpotShorthandModel[] = [];
  tableSpotReviews: SpotReviewModel[] = [];
  isLoadingReviews: boolean = false;
  tableSearchForm: FormGroup;

  currentPage: number = 0;
  pageSize: number = 4;
  totalItems: number = 999;
  totalPages: number = 999;

  constructor(
    protected spotService: SpotService,
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
    this.spotService
      .findSpotsPaginated(this.currentPage, this.pageSize, '', SortOptions.ALPHABETICAL, [])
      .subscribe({
        next: (response: PageResponseModel<SpotShorthandModel>) => {
          this.tableShorthandData = response.content;
          this.totalItems = response.totalElements;
          this.totalPages = response.totalPages;
          this.cdr.detectChanges();
        },
      });
  }

  handleSpotSearch() {
    this.spinnerService.showSectionSpinner();
    this.spotService
      .findSpotsPaginated(
        this.currentPage,
        this.pageSize,
        this.tableSearchForm.get('searchTerm')?.value,
        SortOptions.ALPHABETICAL,
        [],
      )
      .subscribe({
        next: (response: PageResponseModel<SpotShorthandModel>) => {
          this.tableShorthandData = response.content;
          this.totalItems = response.totalElements;
          this.totalPages = response.totalPages;
          this.cdr.detectChanges();
        },
      });
  }

  handleOverviewSelect(spotId: number): void {
    const spot = this.tableShorthandData.find((s) => s.id === spotId);
    if (spot?.slug) {
      this.spotService.findSpotOverview(spot.slug).subscribe({
        next: (overview) => {
          this.tableSelectedItem = overview;
          this.handleReviewLoad(spotId, SortOptions.ALPHABETICAL);
          this.cdr.detectChanges();
        },
      });
    }
  }

  handleReviewLoad(spotId: number, sortOption: SortOptions): void {
    this.isLoadingReviews = true;
    this.reviewService.findAllSpotReviews(0, 10, spotId, sortOption).subscribe({
      next: (response: PageResponseModel<SpotReviewModel>) => {
        this.tableSpotReviews = response.content;
        this.isLoadingReviews = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.isLoadingReviews = false;
        this.cdr.detectChanges();
      },
    });
  }

  handleUpdateItem(finalPayload: SpotUpdateModel): void {
    this.spotService.updateSpot(finalPayload).subscribe({
      next: (response) => {
        this.spinnerService.hideNavigateSpinner();
        this.toastr.success('Spot updated successfully!');
        this.handleOverviewSelect(finalPayload.id);
      },
      error: (error) => {
        this.spinnerService.hideNavigateSpinner();
        this.toastr.error('Failed to update spot. Please try again.');
      },
    });
  }

  async handleDeleteItem(spotId: number): Promise<void> {
    const result = await this.modal.openAsync<{ confirmed: boolean }>(DeleteModal, {
      titleKey: 'admin.spotOverview.delete',
      confirmMessageKey: 'admin.spotOverview.deleteConfirm',
    });

    if (!result.confirmed) return;

    this.spinnerService.showSectionSpinner();
    this.spotService.deleteSpot(spotId).subscribe({
      next: (response) => {
        this.spinnerService.hideSectionSpinner();
        this.toastr.success('Spot deleted successfully!');
        this.tableSelectedItem = null;
        this.tableSpotReviews = [];
        this.cdr.detectChanges();
        this.loadSpots();
      },
      error: (error) => {
        this.spinnerService.hideSectionSpinner();
        this.toastr.error('Failed to delete spot. Please try again.');
      },
    });
  }

  handleNextPage(page: number): void {
    this.currentPage++;
    this.loadSpots();
  }

  handlePreviousPage(page: number): void {
    this.currentPage--;
    this.loadSpots();
  }

  handleReviewLoadMore(page: number): void {
    if (this.tableSelectedItem) {
      this.isLoadingReviews = true;
      this.reviewService
        .findAllSpotReviews(page, 10, this.tableSelectedItem.id, SortOptions.RATING)
        .subscribe({
          next: (response: PageResponseModel<SpotReviewModel>) => {
            this.tableSpotReviews = [...this.tableSpotReviews, ...response.content];
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
        .findAllSpotReviews(0, 10, this.tableSelectedItem.id, sortOption)
        .subscribe({
          next: (response: PageResponseModel<SpotReviewModel>) => {
            this.tableSpotReviews = response.content;
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

  private loadSpots(): void {
    this.spotService
      .findSpotsPaginated(this.currentPage, this.pageSize, '', SortOptions.ALPHABETICAL, [])
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
