import { Component, Input } from '@angular/core';
import { CommunityRequestModel } from '../../shared/models/community.request.model';
import { ObjectType, RequestStatus, RequestType } from '../../shared/constants/ObjectTypes';
import { TranslocoPipe } from '@ngneat/transloco';
import { DatePipe, NgClass } from '@angular/common';

@Component({
  selector: 'app-community-request-card',
  imports: [TranslocoPipe, DatePipe, NgClass],
  templateUrl: './community-request-card.html',
  styleUrl: './community-request-card.css',
  host: {
    class: 'w-full opacity-80 hover:opacity-100 transition-opacity'
  }
})
export class CommunityRequestCard {
  @Input() request!: CommunityRequestModel;

  constructor() {}

  getRequestTypeLabel(): string {
    switch (this.request.requestType) {
      case RequestType.ADD:
        return 'communityRequest.requestTypeAdd';
      case RequestType.UPDATE:
        return 'communityRequest.requestTypeUpdate';
      case RequestType.REMOVE:
        return 'communityRequest.requestTypeRemove';
      case RequestType.OTHER:
        return 'communityRequest.requestTypeOther';
      default:
        return 'communityRequest.requestTypeOther';
    }
  }

  getObjectTypeLabel(): string {
    switch (this.request.objectType) {
      case ObjectType.SPOT:
        return 'communityRequest.objectTypeSpot';
      case ObjectType.EVENT:
        return 'communityRequest.objectTypeEvent';
      default:
        return 'communityRequest.objectTypeSpot';
    }
  }

  getStatusColorClass(): string {
    switch (this.request.status) {
      case RequestStatus.PENDING:
        return 'bg-yellow-500/20 border-yellow-500';
      case RequestStatus.APPROVED:
        return 'bg-green-500/20 border-green-500';
      case RequestStatus.REJECTED:
        return 'bg-red-500/20 border-red-500';
      default:
        return 'bg-gray-500/20 border-gray-500';
    }
  }

  getRequestTypeIcon(): string {
    switch (this.request.requestType) {
      case RequestType.ADD:
        return '+';
      case RequestType.UPDATE:
        return '✎';
      case RequestType.REMOVE:
        return '✕';
      case RequestType.OTHER:
        return '?';
      default:
        return '?';
    }
  }
}
