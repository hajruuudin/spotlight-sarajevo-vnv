import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PageHeader } from '../../../components/page-header/page-header';
import { TranslocoPipe } from '@ngneat/transloco';
import { CommunityRequestModel, CommunityRequestOverviewModel, CommunityRequestStatusUpdate } from '../../../shared/models/community.request.model';
import { SortOptions } from '../../../shared/constants/SortOptions';
import { FilterOptions } from '../../../shared/constants/FilterOptions';
import { RequestStatus } from '../../../shared/constants/ObjectTypes';
import { EventService } from '../../../services/event.service';
import { ReviewService } from '../../../services/review.service';
import { SessionService } from '../../../core/services/session.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { CommunityRequestService } from '../../../services/community.request.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ModalService } from '../../../core/services/modal.service';
import { CommunityOverviewTable } from "../../../components/admin-overview-base-table/admin-overview-entity-tables/community-overview-table/community-overview-table";
import { SelectGroup } from '../../../components/select-group/select-group';

@Component({
  selector: 'app-admin-request-overview',
  imports: [PageHeader, TranslocoPipe, CommunityOverviewTable, ReactiveFormsModule, SelectGroup],
  templateUrl: './admin-request-overview.html',
  styleUrl: './admin-request-overview.css'
})
export class AdminRequestOverview implements OnInit{
  tableDefinitions: string[] = [
    'ID',
    'USERNAME & ID',
    'REQUEST HEADER',
    'REQUEST TYPE',
    'OBJECT TYPE',
    'CREATED AT',
    'STATUS'
  ];

  tableSelectedItem: CommunityRequestOverviewModel | null = null
  tableShorthandData: CommunityRequestModel[] = []
  tableFilters: FilterOptions[] = [FilterOptions.ALL, FilterOptions.APPROVED, FilterOptions.PENDING, FilterOptions.REJECTED]
  filterOptions: { label: string; value: FilterOptions }[] = [];
  filterForm: FormGroup;
  
  currentPage: number = 0;
  pageSize: number = 5;
  totalItems: number = 999;
  totalPages: number = 999;
  currentFilter: FilterOptions = FilterOptions.ALL;

  constructor(
    protected requestService: CommunityRequestService,
    protected sessionService: SessionService,
    protected spinnerService: SpinnerService,
    protected toastr: HotToastService,
    protected fb: FormBuilder,
    protected cdr: ChangeDetectorRef,
    protected modal: ModalService,
  ) {
    this.filterForm = this.fb.group({
      filter: [FilterOptions.ALL]
    });
  }

  ngOnInit(): void {
    this.initializeFilterOptions();
    this.setupFilterValueChanges();
    this.loadCommunityRequests();
  }

  private initializeFilterOptions(): void {
    this.filterOptions = [
      { label: 'All', value: FilterOptions.ALL },
      { label: 'Pending', value: FilterOptions.PENDING },
      { label: 'Approved', value: FilterOptions.APPROVED },
      { label: 'Rejected', value: FilterOptions.REJECTED }
    ];
  }

  private setupFilterValueChanges(): void {
    this.filterForm.get('filter')?.valueChanges.subscribe((filter: FilterOptions) => {
      this.handleFilterChange(filter);
    });
  }

  handleOverviewSelect(requestId: number): void{
    this.requestService.getRequestOverview(requestId).subscribe({
      next: (requestOverview: CommunityRequestOverviewModel) => {
        this.tableSelectedItem = requestOverview;
        this.cdr.markForCheck();
      },
      error: (error) => {
        this.toastr.error('Failed to load community request overview. Please try again later.');
      }
    })
  }

  handleFilterChange(filter: FilterOptions): void {
    this.currentFilter = filter;
    this.currentPage = 0;
    this.loadCommunityRequests();
  }

  handleRequestApproval(requestId: number): void {
    const statusUpdate = new CommunityRequestStatusUpdate(
      requestId,
      RequestStatus.APPROVED
    );

    this.spinnerService.showNavigateSpinner();

    this.requestService.updateCommunityRequestStatus(statusUpdate).subscribe({
      next: (updatedRequest: CommunityRequestModel) => {
        this.spinnerService.hideNavigateSpinner();
        this.toastr.success('Community request approved successfully!');
        this.loadCommunityRequests();
        this.handleOverviewSelect(requestId);
      },
      error: (error) => {
        this.spinnerService.hideNavigateSpinner();
        this.toastr.error('Failed to approve community request. Please try again.');
      },
    });
  }

  handleRequestRejection(requestId: number): void {
    const statusUpdate = new CommunityRequestStatusUpdate(
      requestId,
      RequestStatus.REJECTED
    );

    this.spinnerService.showNavigateSpinner();

    this.requestService.updateCommunityRequestStatus(statusUpdate).subscribe({
      next: (updatedRequest: CommunityRequestModel) => {
        this.spinnerService.hideNavigateSpinner();
        this.toastr.success('Community request rejected successfully!');
        this.loadCommunityRequests();
        this.handleOverviewSelect(requestId);
      },
      error: (error) => {
        this.spinnerService.hideNavigateSpinner();
        this.toastr.error('Failed to reject community request. Please try again.');
      },
    });
  }

  handleRequestIntegration(integrationInfo: any){}

  handleRequestDelete(requestId: number){}

  handleNextPage(page: number): void {
    this.currentPage++;
    this.loadCommunityRequests();
  }

  handlePreviousPage(page: number): void {
    this.currentPage--;
    this.loadCommunityRequests();
  }

  /* Loads all the requests initially upon page load, and upon refreshing the data */
  private loadCommunityRequests(){
    this.requestService.getCommunityRequests(this.currentFilter.toString()).subscribe({
      next: (requests: CommunityRequestModel[]) => {
        this.tableShorthandData = requests;
        this.totalItems = requests.length;
        this.totalPages = Math.ceil(this.totalItems / this.pageSize);
        this.cdr.markForCheck();
      },
      error: (error) => {
        this.toastr.error('Failed to load community requests. Please try again later.');
      }
    });
  }

}
