import { ChangeDetectorRef, Component, computed, OnInit } from '@angular/core';
import { PageHeader } from '../../../components/page-header/page-header';
import { SearchBar } from '../../../components/search-bar/search-bar';
import { TranslocoPipe } from '@ngneat/transloco';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SessionService } from '../../../core/services/session.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { CategoryService } from '../../../services/category.service';
import { SpotCategoryModel } from '../../../shared/models/category.model';
import { HttpErrorResponse } from '@angular/common/http';
import { CategoryFilterSelector } from '../../../components/category-filter-selector/category-filter-selector';
import { ButtonSecondary } from '../../../components/button-secondary/button-secondary';
import { SortingSelector } from '../../../components/sorting-selector/sorting-selector';
import { SpotShorthandModel } from '../../../shared/models/spot.model';
import { SearchSpotCard } from '../../../components/search-spot-card/search-spot-card';
import { SortOptions } from '../../../shared/constants/SortOptions';
import { SpotService } from '../../../services/spot.service';
import { SpinnerSmallComponent } from '../../../components/spinner-small-component/spinner-small-component';
import { PageResponseModel } from '../../../shared/models/shared.model';
import { NotFoundComponent } from '../../../components/not-found-component/not-found-component';
import { Router } from '@angular/router';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-spot-search',
  imports: [
    PageHeader,
    SearchBar,
    TranslocoPipe,
    ReactiveFormsModule,
    CategoryFilterSelector,
    ButtonSecondary,
    SortingSelector,
    SearchSpotCard,
    SpinnerSmallComponent,
    NotFoundComponent,
    ButtonPrimary,
    NgClass,
  ],
  templateUrl: './spot-search.html',
  styleUrl: './spot-search.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class SpotSearch implements OnInit {
  spotSearchForm: FormGroup;
  spotCategories: SpotCategoryModel[] = [];
  sortingMethods: string[] = [
    SortOptions.ALPHABETICAL.toString(),
    SortOptions.RATING.toString(),
    SortOptions.PROXIMITY.toString()
  ];

  selectedCategoryIds: number[] = [];
  selectedSortingMethod: string = SortOptions.ALPHABETICAL.toString();
  isFilterPopupLoaded = false;
  isSortingPopupLoaded = false;
  showOnlyNonVisited: boolean = false;
  pageNumber = 0;
  pageSize = 4;
  totalElements = 0;
  totalPages = 0;
  spotSearchResults: SpotShorthandModel[] = [];
  
  // Geolocation state
  userLatitude: number | null = null;
  userLongitude: number | null = null;
  locationPermissionGranted: boolean = false;

  constructor(
    protected categoryService: CategoryService,
    protected spotService: SpotService,
    protected session: SessionService,
    protected router: Router,
    protected fb: FormBuilder,
    protected spinner: SpinnerService,
    protected toastr: HotToastService,
    protected cdr: ChangeDetectorRef,
  ) {
    this.spotSearchForm = this.fb.group({
      searchTerm: ['', Validators.required],
      sortOption: ['', Validators.required],
    });
  }

  protected isSectionLoading = computed(() => this.spinner.loadingSection());

  getCurrentLanguage(): string {
    return this.session.language();
  }

  ngOnInit(): void {
    this.categoryService.getAllSpotCategories().subscribe({
      next: (response: SpotCategoryModel[]) => {
        this.spotCategories = response;
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {},
    });

    this.spinner.showSectionSpinner();
    this.fetchSpots(
      this.pageNumber,
      this.pageSize,
      '',
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  onSearchTriggered() {
    this.fetchSpots(
      this.pageNumber,
      this.pageSize,
      this.spotSearchForm.get('searchTerm')?.value,
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
    this.fetchSpots(
      this.pageNumber,
      this.pageSize,
      this.spotSearchForm.get('searchTerm')?.value,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  toggleNonVisitedFilter() {
    this.showOnlyNonVisited = !this.showOnlyNonVisited;
    this.fetchSpots(
      this.pageNumber,
      this.pageSize,
      this.spotSearchForm.get('searchTerm')?.value,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  resetCategoryFilters() {
    this.selectedCategoryIds = [];
    this.showOnlyNonVisited = false;
    this.fetchSpots(
      this.pageNumber,
      this.pageSize,
      this.spotSearchForm.get('searchTerm')?.value,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  resetSortingFilters() {
    this.selectedSortingMethod = SortOptions.ALPHABETICAL.toString();
    this.fetchSpots(
      this.pageNumber,
      this.pageSize,
      this.spotSearchForm.get('searchTerm')?.value,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      true,
      false,
    );
  }

  async requestLocationPermission(): Promise<void> {
    if (!navigator.geolocation) {
      this.toastr.error(this.session.language() === 'en' 
        ? 'Geolocation is not supported by your browser'
        : 'Vaš preglednik ne podržava geolokaciju');
      return;
    }

    return new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          this.userLatitude = position.coords.latitude;
          this.userLongitude = position.coords.longitude;
          this.locationPermissionGranted = true;
          resolve();
        },
        (error) => {
          this.locationPermissionGranted = false;
          let errorMessage = '';
          
          if (this.session.language() === 'en') {
            switch (error.code) {
              case error.PERMISSION_DENIED:
                errorMessage = 'Location permission denied. Please enable location access to use proximity sorting.';
                break;
              case error.POSITION_UNAVAILABLE:
                errorMessage = 'Location information unavailable.';
                break;
              case error.TIMEOUT:
                errorMessage = 'Location request timed out.';
                break;
              default:
                errorMessage = 'An unknown error occurred while getting location.';
            }
          } else {
            switch (error.code) {
              case error.PERMISSION_DENIED:
                errorMessage = 'Dozvola za lokaciju odbijena. Molimo omogućite pristup lokaciji za sortiranje po blizini.';
                break;
              case error.POSITION_UNAVAILABLE:
                errorMessage = 'Informacije o lokaciji nisu dostupne.';
                break;
              case error.TIMEOUT:
                errorMessage = 'Zahtjev za lokaciju je istekao.';
                break;
              default:
                errorMessage = 'Došlo je do nepoznate greške prilikom dobijanja lokacije.';
            }
          }
          
          this.toastr.error(errorMessage);
          // Reset to alphabetical if location fails
          this.selectedSortingMethod = SortOptions.ALPHABETICAL.toString();
          reject(error);
        },
        {
          enableHighAccuracy: true,
          timeout: 5000,
          maximumAge: 0
        }
      );
    });
  }

  fetchSpots(
    pageNumber: number,
    pageSize: number,
    searchValue: string,
    sortingMethod: string,
    categoryIds: number[],
    resetPages: boolean,
    extendResultSet: boolean,
  ) {
    if (resetPages) {
      pageNumber = 0;
    }

    // If proximity sorting is selected and we don't have location yet, request it
    if (sortingMethod === SortOptions.PROXIMITY.toString() && !this.locationPermissionGranted) {
      this.requestLocationPermission().then(() => {
        this.executeFetchSpots(pageNumber, pageSize, searchValue, sortingMethod, categoryIds, resetPages, extendResultSet);
      }).catch(() => {
        // Location permission failed, already handled in requestLocationPermission
        // Fetch with alphabetical sorting instead
        this.executeFetchSpots(pageNumber, pageSize, searchValue, SortOptions.ALPHABETICAL.toString(), categoryIds, resetPages, extendResultSet);
      });
    } else {
      this.executeFetchSpots(pageNumber, pageSize, searchValue, sortingMethod, categoryIds, resetPages, extendResultSet);
    }
  }

  executeFetchSpots(
    pageNumber: number,
    pageSize: number,
    searchValue: string,
    sortingMethod: string,
    categoryIds: number[],
    resetPages: boolean,
    extendResultSet: boolean,
  ) {
    this.spinner.showSectionSpinner();
    
    // Pass user location for proximity sorting
    const latitude = sortingMethod === SortOptions.PROXIMITY.toString() ? this.userLatitude : null;
    const longitude = sortingMethod === SortOptions.PROXIMITY.toString() ? this.userLongitude : null;
    
    // Pass isNotVisited parameter if the filter is enabled
    const isNotVisited = this.showOnlyNonVisited ? true : null;
    
    this.spotService
      .findSpotsPaginated(pageNumber, pageSize, searchValue, sortingMethod, categoryIds, latitude, longitude, isNotVisited)
      .subscribe({
        next: (response: PageResponseModel<SpotShorthandModel>) => {
          this.spinner.hideSectionSpinner();

          if (extendResultSet) {
            this.spotSearchResults = this.spotSearchResults.concat(response.content);
          } else {
            this.spotSearchResults = response.content;
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
    this.fetchSpots(
      this.pageNumber,
      this.pageSize,
      this.spotSearchForm.get('searchTerm')?.value,
      this.selectedSortingMethod,
      this.selectedCategoryIds,
      false,
      true,
    );
  }

  navigateToSpotOverview(spotSlug: string) {
    this.router.navigate(['/spots/' + spotSlug]);
  }

  get isPremiumUser(): boolean {
    return this.session.getUser()?.isPremium ?? false;
  }

  navigateToMapOverview() {
    this.router.navigate(['/spots/map']);
  }
}
