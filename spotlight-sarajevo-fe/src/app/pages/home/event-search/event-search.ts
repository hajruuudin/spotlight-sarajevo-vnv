import { ChangeDetectorRef, Component, computed } from '@angular/core';
import { PageHeader } from '../../../components/page-header/page-header';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EventCategoryModel } from '../../../shared/models/category.model';
import { CategoryService } from '../../../services/category.service';
import { SessionService } from '../../../core/services/session.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { EventShorthandModel } from '../../../shared/models/event.model';
import { HttpErrorResponse } from '@angular/common/http';
import { TranslocoPipe } from '@ngneat/transloco';
import { SearchBar } from '../../../components/search-bar/search-bar';
import { SortingSelector } from '../../../components/sorting-selector/sorting-selector';
import { ButtonSecondary } from '../../../components/button-secondary/button-secondary';
import { CategoryFilterSelector } from '../../../components/category-filter-selector/category-filter-selector';
import { SearchEventCard } from '../../../components/search-event-card/search-event-card';
import { SortOptions } from '../../../shared/constants/SortOptions';
import { EventService } from '../../../services/event.service';
import { PageResponseModel } from '../../../shared/models/shared.model';
import { SpinnerSmallComponent } from '../../../components/spinner-small-component/spinner-small-component';
import { NotFoundComponent } from '../../../components/not-found-component/not-found-component';
import { Router } from '@angular/router';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';

@Component({
  selector: 'app-event-search',
  imports: [
    PageHeader,
    ReactiveFormsModule,
    TranslocoPipe,
    SearchBar,
    SortingSelector,
    ButtonSecondary,
    CategoryFilterSelector,
    SearchEventCard,
    SpinnerSmallComponent,
    NotFoundComponent,
    ButtonPrimary,
  ],
  templateUrl: './event-search.html',
  styleUrl: './event-search.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class EventSearch {
  eventSearchForm: FormGroup;
  eventCategories: EventCategoryModel[] = [];
  sortingMethods: string[] = [SortOptions.ALPHABETICAL.toString(), SortOptions.DATE_UPCOMING.toString(), SortOptions.DATE_PAST.toString()];

  selectedCategoryIds: number[] = [];
  selectedSortingMethod: string = SortOptions.DATE_UPCOMING.toString();

  isFilterPopupLoaded: boolean = false;
  isSortingPopupLoaded: boolean = false;

  pageNumber: number = 0;
  pageSize: number = 2;
  totalElements: number = 0;
  totalPages: number = 0;

  eventSearchResults: EventShorthandModel[] = [];

  isSectionLoading = computed(() => this.spinner.loadingSection());

  constructor(
    protected categoryService: CategoryService,
    protected eventService: EventService,
    protected session: SessionService,
    protected fb: FormBuilder,
    protected spinner: SpinnerService,
    protected router: Router,
    protected toastr: HotToastService,
    protected cdr: ChangeDetectorRef,
  ) {
    this.eventSearchForm = this.fb.group({
      searchTerm: ['', Validators.required],
      sortOption: ['', Validators.required],
    });
  }


  getCurrentLanguage(): string {
    return this.session.language();
  }

  ngOnInit(): void {
    this.categoryService.getAllEventCategories().subscribe({
      next: (response: EventCategoryModel[]) => {
        this.eventCategories = response;
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        // probably redirect to error
      },
    });

    this.fetchEvents(
      this.pageNumber,
      this.pageSize,
      '',
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  onSearchTriggered(searchValue: string) {
    this.fetchEvents(
      this.pageNumber,
      this.pageSize,
      searchValue,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  onCategoryCheckboxChange(categoryID: number) {
    const index = this.selectedCategoryIds.indexOf(categoryID);

    if (index === -1) {
      this.selectedCategoryIds.push(categoryID);
    } else {
      this.selectedCategoryIds.splice(index, 1);
    }
  }

  toggleFilterPopup() {
    this.isFilterPopupLoaded = !this.isFilterPopupLoaded;
  }

  toggleSortingPopup() {
    this.isSortingPopupLoaded = !this.isSortingPopupLoaded;
  }

  onSortingMethodSelected(sortingMethod: string) {
    this.selectedSortingMethod = sortingMethod;
    this.fetchEvents(
      this.pageNumber,
      this.pageSize,
      this.eventSearchForm.get('searchTerm')?.value,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  resetCategoryFilters() {
    this.selectedCategoryIds = [];
    this.fetchEvents(
      this.pageNumber,
      this.pageSize,
      this.eventSearchForm.get('searchTerm')?.value,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  resetSortingFilters() {
    this.selectedSortingMethod = SortOptions.ALPHABETICAL.toString();
    this.fetchEvents(
      this.pageNumber,
      this.pageSize,
      this.eventSearchForm.get('searchTerm')?.value,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  fetchEvents(
    pageNumber: number,
    pageSize: number,
    searchTerm: string,
    sortOption: string,
    categoryIds: number[],
    resetPages: boolean,
    extendResultSet: boolean,
  ) {
    if (resetPages) {
      pageNumber = 0;
    }

    this.spinner.showSectionSpinner();
    this.eventService
      .findEventsPaginated(pageNumber, pageSize, searchTerm, sortOption, categoryIds)
      .subscribe({
        next: (response: PageResponseModel<EventShorthandModel>) => {
          this.spinner.hideSectionSpinner();

          if (extendResultSet) {
            this.eventSearchResults = this.eventSearchResults.concat(response.content);
          } else {
            this.eventSearchResults = response.content;
          }

          if (resetPages) {
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            this.pageNumber = 0;
          }

          this.cdr.detectChanges();
        },
      });
  }

  loadMore() {
    if (this.totalElements <= this.pageNumber + 1 * this.pageSize) {
      return;
    }
    this.pageNumber++;
    this.fetchEvents(
      this.pageNumber,
      this.pageSize,
      this.eventSearchForm.get('searchTerm')?.value,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      false,
      true,
    );
  }

  navigateToEventOverview(eventSlug: string) {
    this.router.navigate(['/events/' + eventSlug]);
  }

  get isPremiumUser(): boolean {
    return this.session.getUser()?.isPremium ?? false;
  }

  navigateToCalendarOverview() {
    this.router.navigate(['/events/calendar']);
  }
}
