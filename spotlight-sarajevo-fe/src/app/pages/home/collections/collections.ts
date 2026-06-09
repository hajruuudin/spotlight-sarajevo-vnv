import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PageHeader } from '../../../components/page-header/page-header';
import { TranslocoPipe } from '@ngneat/transloco';
import { SessionService } from '../../../core/services/session.service';
import {
  CollectionCreateModel,
  CollectionItemsModel,
  CollectionModel,
  CollectionUpdateModel,
} from '../../../shared/models/collection.model';
import { CollectionService } from '../../../services/collection.service';
import { HttpErrorResponse } from '@angular/common/http';
import { HotToastService } from '@ngxpert/hot-toast';
import { ActivatedRoute, Router } from '@angular/router';
import { CollectionPageData } from '../../../core/resolvers/collection.resolver';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NotFoundComponent } from '../../../components/not-found-component/not-found-component';
import { ModalService } from '../../../core/services/modal.service';
import { AddCollectionModal } from '../../../components/modals/add-collection-modal/add-collection-modal';
import { SpinnerService } from '../../../core/services/spinner.service';
import { NgClass } from '@angular/common';
import { SearchSpotCard } from '../../../components/search-spot-card/search-spot-card';
import { SpotShorthandModel } from '../../../shared/models/spot.model';
import { EventShorthandModel } from '../../../shared/models/event.model';
import { SearchEventCard } from '../../../components/search-event-card/search-event-card';
import { EditCollectionModal } from '../../../components/modals/edit-collection-modal/edit-collection-modal';
import { DeleteCollectionModal } from '../../../components/modals/delete-collection-modal/delete-collection-modal';
import { CollectionHeader } from '../../../components/collection-header/collection-header';

@Component({
  selector: 'app-collections',
  imports: [
    ReactiveFormsModule,
    PageHeader,
    TranslocoPipe,
    ButtonPrimary,
    NotFoundComponent,
    NgClass,
    SearchSpotCard,
    SearchEventCard,
    CollectionHeader,
  ],
  templateUrl: './collections.html',
  styleUrl: './collections.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class Collections implements OnInit {
  userCollections: CollectionModel[] = [];
  userEventCollections: CollectionModel[] = [];
  userSpotCollections: CollectionModel[] = [];

  selectedCollection: CollectionItemsModel = {
    collectionName: 'Default',
    collectionDescription: 'Default',
    collectionId: 0,
    collectionType: 'SPOT',
    collectionItems: [],
    isSystem: false,
  };

  collectionSelectForm: FormGroup;

  constructor(
    protected session: SessionService,
    protected router: Router,
    protected route: ActivatedRoute,
    protected collectionService: CollectionService,
    protected toastr: HotToastService,
    protected modal: ModalService,
    protected cdr: ChangeDetectorRef,
    protected spinner: SpinnerService,
    protected fb: FormBuilder,
  ) {
    this.collectionSelectForm = this.fb.group({
      selectedCollection: [null],
    });
  }

  ngOnInit(): void {
    const data = this.route.snapshot.data['collectionData'] as CollectionPageData;

    this.userCollections = data.userCollections ?? [];
    this.divideUserCollection();

    this.selectedCollection = data.selectedCollection ?? {
      collectionName: 'Default',
      collectionDescription: 'Default',
      collectionId: 0,
      collectionType: 'SPOT',
      collectionItems: [],
      isSystem: false,
    };
  }

  fetchSystemCollection(collectionType: boolean) {
    if (collectionType) {
      this.collectionService.findAllSpotsCollection().subscribe({
        next: (response: CollectionItemsModel) => {
          const uniqueItems = Array.from(
            new Map(response.collectionItems.map((item) => [item.id, item])).values(),
          );
          response.collectionItems = uniqueItems;
          this.selectedCollection = { ...response };
          this.cdr.markForCheck();
        },
        error: (error: HttpErrorResponse) => {
          this.toastr.error('SPOT COLLECTION ERROR');
          this.cdr.markForCheck();
        },
      });
    } else {
      this.collectionService.findAllEventsCollection().subscribe({
        next: (response: CollectionItemsModel) => {
          const uniqueItems = Array.from(
            new Map(response.collectionItems.map((item) => [item.id, item])).values(),
          );
          response.collectionItems = uniqueItems;
          this.selectedCollection = { ...response };
          this.cdr.markForCheck();
        },
        error: (error: HttpErrorResponse) => {
          this.toastr.error('EVENT COLLECTION ERROR');
          this.cdr.markForCheck();
        },
      });
    }
  }

  fetchSelectedCollection(collectionId: number) {
    this.collectionService.findCollectionItems(collectionId).subscribe({
      next: (response: CollectionItemsModel) => {
        this.selectedCollection = { ...response };
        console.log(this.selectedCollection);
        this.cdr.markForCheck();
      },
      error: (response: HttpErrorResponse) => {
        this.toastr.error(
          this.session.language() == 'en'
            ? 'Cannot load that collection! :('
            : 'Kolekcija se ne moze ucitati! :(',
        );
        this.cdr.markForCheck();
      },
    });
  }

  divideUserCollection() {
    this.userEventCollections = this.userCollections.filter(
      (col) => col.collectionType === 'EVENT',
    );

    this.userSpotCollections = this.userCollections.filter((col) => col.collectionType === 'SPOT');
  }

  async openAddCollectionModal() {
    const result = await this.modal.openAsync<{ type: string; data?: any }>(AddCollectionModal, {});

    if (result?.type === 'cancel') return;

    if (result.type === 'add') {
      this.addNewCollection(
        new CollectionCreateModel(
          result.data.collectionName,
          result.data.collectionDescription,
          result.data.collectionType == 1 ? 'SPOT' : 'EVENT',
        ),
      );
    }
  }

  async openEditCollectionModal() {
    const collectionModel = this.userCollections.find(
      (c) => c.id === this.selectedCollection.collectionId,
    );
    const result = await this.modal.openAsync<{ type: string; data?: any }>(EditCollectionModal, {
      collectionModel: collectionModel,
    });

    if (result?.type === 'cancel') return;

    if (result.type === 'edit') {
      this.editCollection(
        new CollectionUpdateModel(
          collectionModel!.id,
          result.data.collectionName,
          result.data.collectionDescription,
        ),
      );
    }
  }

  async openDeleteCollectionModal() {
    const result = await this.modal.openAsync<{ confirmed: boolean }>(DeleteCollectionModal, {});

    if (!result.confirmed) return;

    if (result.confirmed) {
      this.collectionService.deleteCollection(this.selectedCollection.collectionId).subscribe({
        next: (result: CollectionModel) => {
          this.toastr.success('Collection Deleted');
          this.removeCollectionFrontend(result);
          this.cdr.markForCheck();
          this.fetchSystemCollection(true);
        },
        error: (error: HttpErrorResponse) => {
          this.toastr.error('There was an error. Replace this later.');
          this.cdr.markForCheck();
        },
      });
    }
  }

  addNewCollection(request: CollectionCreateModel) {
    this.spinner.showNavigateSpinner();
    this.collectionService.addCollection(request).subscribe({
      next: (response: CollectionModel) => {
        this.spinner.hideNavigateSpinner();
        this.toastr.success(
          this.session.language() == 'en'
            ? 'New collection created!'
            : 'Nova kolekcija napravljena',
        );
        this.addCollectionFrontend(response);
        this.cdr.markForCheck();
      },
      error: (response: HttpErrorResponse) => {
        this.spinner.hideNavigateSpinner();
        this.toastr.error(
          this.session.language() == 'en' ? 'Something went wrong!' : 'Nesto je krenulo po zlu',
        );
        this.cdr.markForCheck();
      },
    });
  }

  editCollection(request: CollectionUpdateModel) {
    this.spinner.showNavigateSpinner();
    this.collectionService.updateCollection(request).subscribe({
      next: (response: CollectionModel) => {
        this.spinner.hideNavigateSpinner();
        this.toastr.success(
          this.session.language() == 'en' ? 'Collection edited!' : 'Kolekcija izmjenjena',
        );
        this.updateCollectionFrontend(response);
        this.cdr.markForCheck();
      },
      error: (response: HttpErrorResponse) => {
        this.spinner.hideNavigateSpinner();
        this.toastr.error(
          this.session.language() == 'en' ? 'Something went wrong!' : 'Nesto je krenulo po zlu',
        );
        this.cdr.markForCheck();
      },
    });
  }

  addCollectionFrontend(collection: CollectionModel): void {
    if (collection.collectionType === 'SPOT') {
      this.userSpotCollections = [...this.userSpotCollections, collection];
      return;
    }

    if (collection.collectionType === 'EVENT') {
      this.userEventCollections = [...this.userEventCollections, collection];
      return;
    }
  }

  updateCollectionFrontend(updated: CollectionModel): void {
    if (updated.collectionType === 'SPOT') {
      this.userSpotCollections = this.userSpotCollections.map((c) =>
        c.id === updated.id ? updated : c,
      );
      return;
    }

    if (updated.collectionType === 'EVENT') {
      this.userEventCollections = this.userEventCollections.map((c) =>
        c.id === updated.id ? updated : c,
      );
      return;
    }
  }

  removeCollectionFrontend(collection: CollectionModel): void {
    if (collection.collectionType === 'SPOT') {
      this.userSpotCollections = this.userSpotCollections.filter((c) => c.id !== collection.id);
      return;
    }

    if (collection.collectionType === 'EVENT') {
      this.userEventCollections = this.userEventCollections.filter((c) => c.id !== collection.id);
      return;
    }
  }

  isSpot(item: SpotShorthandModel | EventShorthandModel): item is SpotShorthandModel {
    return this.selectedCollection.collectionType === 'SPOT';
  }

  isEvent(item: SpotShorthandModel | EventShorthandModel): item is EventShorthandModel {
    return this.selectedCollection.collectionType === 'EVENT';
  }

  navigateToSpotOverview(spotSlug: string) {
    this.spinner.showNavigateSpinner();
    this.router.navigate(['/spots/' + spotSlug]);
    this.spinner.hideNavigateSpinner();
  }

  navigateToEventOverview(eventSlug: string) {
    this.spinner.showNavigateSpinner();
    this.router.navigate(['/events/' + eventSlug]);
    this.spinner.hideNavigateSpinner();
  }
}
