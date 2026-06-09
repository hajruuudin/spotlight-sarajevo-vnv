import { ChangeDetectorRef, Component } from '@angular/core';
import { PageHeader } from '../../../components/page-header/page-header';
import {
  TouristGuideModel,
  TouristGuideOverviewModel,
  TouristGuideShorthandModel,
  TouristGuideUpdateModel,
} from '../../../shared/models/tourist.guide.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TouristGuideService } from '../../../services/tourist.guide.service';
import { SessionService } from '../../../core/services/session.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { ModalService } from '../../../core/services/modal.service';
import { DeleteModal } from '../../../components/modals/delete-modal/delete-modal';
import { GuideOverviewTable } from "../../../components/admin-overview-base-table/admin-overview-entity-tables/guide-overview-table/guide-overview-table";
import { SortOptions } from '../../../shared/constants/SortOptions';
import { PageResponseModel } from '../../../shared/models/shared.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-admin-guide-overview',
  imports: [PageHeader, GuideOverviewTable],
  templateUrl: './admin-guide-overview.html',
  styleUrl: './admin-guide-overview.css',
})
export class AdminGuideOverview {
  tableDefinitions: string[] = [
    'ID',
    'SLUG',
    'GUIDE TITLE',
    'SMALL DESCRIPTION',
    'CATEGORY',
    'PREVIEW',
  ];

  tableSelectedItem: TouristGuideOverviewModel | null = null;
  tableShorthandData: TouristGuideShorthandModel[] = [];
  tableSearchForm: FormGroup;

  currentPage: number = 0;
  pageSize: number = 4;
  totalItems: number = 999;
  totalPages: number = 999;

  constructor(
    protected guideService: TouristGuideService,
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
    this.loadGuides();
  }

  handleGuideSearch() {
    this.loadGuides();
  }

  handleOverviewSelect(guideId: number): void {
    let selectedSlug : string | null = null

    for(let guide of this.tableShorthandData){
      if(guide.id == guideId) selectedSlug = guide.slug
    }

    if(selectedSlug == null){
      return
    } else {
      this.guideService.findGuideOverview(selectedSlug).subscribe({
        next: (response : TouristGuideOverviewModel) => {
          this.tableSelectedItem = response
          this.cdr.detectChanges()
        },
        error: (error : HttpErrorResponse) => {
          this.toastr.error("Guide could not be loaded")
        }
      })
    }
  }

  handleSaveChanges(finalPayload: TouristGuideUpdateModel): void {
    this.spinnerService.showNavigateSpinner()
    if(finalPayload != null){
      this.guideService.updateGuide(finalPayload).subscribe({
        next: (response : TouristGuideModel) => {
          this.spinnerService.hideNavigateSpinner()
          this.toastr.success("Guide Updated wiht new Sections")
          this.cdr.detectChanges()
        },
        error: (response: HttpErrorResponse) => {
          this.spinnerService.hideNavigateSpinner()
          this.toastr.error("Error while updating guide. Please try again later or view logs for more info!")
          this.cdr.detectChanges()
        }
      })
    }
  }

  async handleDeleteItem(guideId: number): Promise<void> {
    const result = await this.modal.openAsync<{ confirmed: boolean }>(DeleteModal, {
      titleKey: 'Delete Tourist Guide',
      confirmMessageKey:
        'Are You sure You want to delete this guide from the system? This is an IRREVERSIBLE process?',
    });

    if (!result.confirmed) return;

    this.spinnerService.showSectionSpinner();
    this.guideService.deleteGuide(guideId).subscribe({
      next: (response) => {
        this.spinnerService.hideSectionSpinner();
        this.toastr.success('Guide deleted successfully!');
        this.tableSelectedItem = null;
        this.cdr.detectChanges();
        this.loadGuides();
      },
      error: (error) => {
        this.spinnerService.hideSectionSpinner();
        this.toastr.error('Failed to delete guide. Please try again.');
      },
    });
  }

  handleNextPage(page: number): void {
    this.currentPage++;
    this.loadGuides();
  }

  handlePreviousPage(page: number): void {
    this.currentPage--;
    this.loadGuides();
  }

  private loadGuides(): void {
    this.guideService.findGuidesPaginated(this.currentPage, this.pageSize, this.tableSearchForm.get('searchTerm')?.value, SortOptions.ALPHABETICAL).subscribe({
      next: (response: PageResponseModel<TouristGuideShorthandModel>) => {
        this.tableShorthandData = response.content;
        this.totalItems = response.totalElements
        this.totalPages = response.totalPages
        this.cdr.detectChanges();
      },
    });
  }
}
