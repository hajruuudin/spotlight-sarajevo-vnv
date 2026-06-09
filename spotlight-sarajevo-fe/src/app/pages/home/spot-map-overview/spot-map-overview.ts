import { ChangeDetectorRef, Component, computed, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslocoPipe } from '@ngneat/transloco';
import { HotToastService } from '@ngxpert/hot-toast';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionService } from '../../../core/services/session.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { SpotService } from '../../../services/spot.service';
import { CategoryService } from '../../../services/category.service';
import { SpotMapModel } from '../../../shared/models/spot.model';
import { SpotCategoryModel } from '../../../shared/models/category.model';
import { MapFullscreen } from '../../../components/map-fullscreen/map-fullscreen';
import { SearchBar } from '../../../components/search-bar/search-bar';
import { CategoryFilterSelector } from '../../../components/category-filter-selector/category-filter-selector';
import { ButtonSecondary } from '../../../components/button-secondary/button-secondary';
import { SpinnerSmallComponent } from '../../../components/spinner-small-component/spinner-small-component';
import { SpotMapResolverData } from '../../../core/resolvers/spot.map.resolver';

@Component({
  selector: 'app-spot-map-overview',
  imports: [
    ReactiveFormsModule,
    TranslocoPipe,
    MapFullscreen,
    SearchBar,
    CategoryFilterSelector,
    ButtonSecondary,
    SpinnerSmallComponent,
  ],
  templateUrl: './spot-map-overview.html',
  styleUrl: './spot-map-overview.css',
  host: {
    class: 'flex flex-col w-full h-[calc(100vh-40px)] relative',
  },
})
export class SpotMapOverview implements OnInit {
  searchForm: FormGroup;
  spotCategories: SpotCategoryModel[] = [];
  spotMapPins: SpotMapModel[] = [];
  selectedCategoryIds: number[] = [];
  isFilterPopupLoaded = false;

  constructor(
    protected spotService: SpotService,
    protected categoryService: CategoryService,
    protected session: SessionService,
    protected router: Router,
    protected route: ActivatedRoute,
    protected fb: FormBuilder,
    protected spinner: SpinnerService,
    protected toastr: HotToastService,
    protected cdr: ChangeDetectorRef,
  ) {
    this.searchForm = this.fb.group({
      searchTerm: [''],
    });
  }

  protected isSectionLoading = computed(() => this.spinner.loadingSection());

  getCurrentLanguage(): string {
    return this.session.language();
  }

  ngOnInit(): void {
    // Get data from resolver
    const resolverData = this.route.snapshot.data['spotMapData'] as SpotMapResolverData;
    
    if (resolverData) {
      this.spotCategories = resolverData.spotCategories;
      this.spotMapPins = resolverData.spotMapPins;
      this.cdr.detectChanges();
    } else {
      // Fallback: load data manually if resolver didn't run
      this.categoryService.getAllSpotCategories().subscribe({
        next: (response: SpotCategoryModel[]) => {
          this.spotCategories = response;
          this.cdr.detectChanges();
        },
        error: (error: HttpErrorResponse) => {},
      });

      this.fetchSpotsForMap('', this.selectedCategoryIds);
    }
  }

  onSearchTriggered(): void {
    this.fetchSpotsForMap(
      this.searchForm.get('searchTerm')?.value || '',
      this.selectedCategoryIds,
    );
  }

  onCategoryCheckboxChange(categoryID: number): void {
    const index = this.selectedCategoryIds.indexOf(categoryID);

    if (index === -1) {
      this.selectedCategoryIds.push(categoryID);
    } else {
      this.selectedCategoryIds.splice(index, 1);
    }

    this.fetchSpotsForMap(
      this.searchForm.get('searchTerm')?.value || '',
      this.selectedCategoryIds,
    );
  }

  toggleFilterPopup(): void {
    this.isFilterPopupLoaded = !this.isFilterPopupLoaded;
  }

  resetCategoryFilters(): void {
    this.selectedCategoryIds = [];
    this.fetchSpotsForMap(
      this.searchForm.get('searchTerm')?.value || '',
      this.selectedCategoryIds,
    );
  }

  fetchSpotsForMap(searchTerm: string, categoryIds: number[]): void {
    this.spinner.showSectionSpinner();
    this.spotService.findSpotsForMap(searchTerm, categoryIds).subscribe({
      next: (response: SpotMapModel[]) => {
        this.spinner.hideSectionSpinner();
        this.spotMapPins = response;
        this.cdr.detectChanges();
      },
      error: (error: HttpErrorResponse) => {
        this.spinner.hideSectionSpinner();
        this.toastr.error(
          this.session.language() === 'en'
            ? 'Failed to load spots. Please try again.'
            : 'Učitavanje lokacija nije uspjelo. Molimo pokušajte ponovo.',
        );
      },
    });
  }


  navigateToSpotOverview(slug: string): void {
    this.router.navigate(['/spots', slug]);
  }
}
