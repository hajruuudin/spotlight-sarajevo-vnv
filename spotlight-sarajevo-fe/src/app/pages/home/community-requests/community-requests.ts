import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PageHeader } from '../../../components/page-header/page-header';
import { TranslocoPipe } from '@ngneat/transloco';
import { ActivatedRoute } from '@angular/router';
import { CommunityRequestModel } from '../../../shared/models/community.request.model';
import { RequestStatus } from '../../../shared/constants/ObjectTypes';
import { CommunityRequestCard } from '../../../components/community-request-card/community-request-card';
import { NotFoundComponent } from '../../../components/not-found-component/not-found-component';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';
import { ModalService } from '../../../core/services/modal.service';
import { AddCommunityRequestModal } from '../../../components/modals/add-community-request-modal/add-community-request-modal';
import { SessionService } from '../../../core/services/session.service';
import { HotToastService } from '@ngxpert/hot-toast';

@Component({
  selector: 'app-community-requests',
  imports: [
    PageHeader,
    TranslocoPipe,
    CommunityRequestCard,
    NotFoundComponent,
    ButtonPrimary
  ],
  templateUrl: './community-requests.html',
  styleUrl: './community-requests.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center pt-3'
  }
})
export class CommunityRequests implements OnInit {
  allRequests: CommunityRequestModel[] = [];
  pendingRequests: CommunityRequestModel[] = [];
  approvedRequests: CommunityRequestModel[] = [];
  rejectedRequests: CommunityRequestModel[] = [];

  constructor(
    protected route: ActivatedRoute,
    protected modal: ModalService,
    protected session: SessionService,
    protected toastr: HotToastService,
    protected cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const data = this.route.snapshot.data['requestData'];
    this.allRequests = data?.communityRequests ?? [];
    this.categorizeRequests();
  }

  categorizeRequests(): void {
    this.pendingRequests = this.allRequests
      .filter(r => r.status === RequestStatus.PENDING)
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());

    this.approvedRequests = this.allRequests
      .filter(r => r.status === RequestStatus.APPROVED)
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());

    this.rejectedRequests = this.allRequests
      .filter(r => r.status === RequestStatus.REJECTED)
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());

    this.cdr.detectChanges()
  }

  get isAllEmpty(): boolean {
    return this.allRequests.length === 0;
  }

  async openAddRequestModal(): Promise<void> {
    const result = await this.modal.openAsync<{ type: string; data?: CommunityRequestModel }>(AddCommunityRequestModal, {});

    if (result?.type === 'cancel') return;

    if (result?.type === 'add' && result.data) {
      this.allRequests.unshift(result.data);
      this.categorizeRequests();
      this.toastr.success(
        this.session.language() === 'en'
          ? 'Request submitted successfully!'
          : 'Zahtjev uspješno poslan!'
      );
    }
  }
}
