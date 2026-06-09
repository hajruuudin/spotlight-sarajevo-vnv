import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
import { SessionService } from '../../../core/services/session.service';
import { TranslocoPipe } from '@ngneat/transloco';
import { ButtonPrimary } from '../../button-primary/button-primary';
import { CollectionCheckboxGroupComponent } from '../../checkbox-group/checkbox-group';
import { SpotService } from '../../../services/spot.service';
import { EventService } from '../../../services/event.service';
import {
  AddCollectionItemsModel,
  CollectionItems,
  SpotInCollectionsModel,
} from '../../../shared/models/collection.model';
import { HttpErrorResponse } from '@angular/common/http';
import { EventInCollectionModel } from '../../../shared/models/collection.model';
import { CollectionService } from '../../../services/collection.service';
import { CollectionModel } from '../../../shared/models/collection.model';
import { NotFound } from "../../../interfaces/error/not-found";
import { NotFoundComponent } from "../../not-found-component/not-found-component";

@Component({
  selector: 'app-add-to-collection-modal',
  imports: [TranslocoPipe, ReactiveFormsModule, ButtonPrimary, CollectionCheckboxGroupComponent, NotFoundComponent],
  templateUrl: './add-to-collection-modal.html',
  styleUrl: './add-to-collection-modal.css',
  host: {
    class: `fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]`,
  },
})
export class AddToCollectionModal {
  protected close!: (result?: any) => void;
  @Input() protected objectType!: string;
  @Input() protected objectId!: number;

  protected originalItems: number[] = [];
  protected userCollections: { label: string; value: number }[] = [];
  protected preselectedItems: number[] = [];
  protected form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private collectionService: CollectionService,
    private toastr: HotToastService,
    private cdr: ChangeDetectorRef,
    private session: SessionService
  ) {}

  ngOnInit(): void {
    this.findUserCollections();
    this.findPreSelectedItems();
    this.form = this.fb.group({
      selectedCollections: [this.preselectedItems],
    });
  }

  onFormSubmit() {
    this.handleCollectionChange();
  }

  onClose() {
    this.close({ type: 'cancel' });
  }

  findUserCollections() {
    this.collectionService.findUserCollections().subscribe({
      next: (result: CollectionModel[]) => {
        result.map((res) => {
          if (res.collectionType == this.objectType && !res.isSystem) {
            let obj = {
              label: res.collectionName,
              value: res.id,
            };

            this.userCollections.push(obj);
          }
        });

        this.cdr.detectChanges();
      },
    });
  }

  findPreSelectedItems() {
    if (this.objectType == 'SPOT') {
      this.collectionService.findSpotInCollections(this.objectId).subscribe({
        next: (response: SpotInCollectionsModel) => {
          this.preselectedItems = [...response.ids];

          this.form.patchValue({
            selectedCollections: [...response.ids],
          });
        },
        error: (response: HttpErrorResponse) => {
          this.toastr.error(
            this.session.language() == 'en'
              ? 'Something went wrong, try again later'
              : 'Nesto je krenulo po zlu, pousajte kasnije'
          );
        },
      });
    } else {
      this.collectionService.findEventInCollections(this.objectId).subscribe({
        next: (response: EventInCollectionModel) => {
          this.preselectedItems = [...response.ids];

          this.form.patchValue({
            selectedCollections: [...response.ids],
          });
        },
        error: (response: HttpErrorResponse) => {
          this.toastr.error(
            this.session.language() == 'en'
              ? 'Something went wrong, try again later'
              : 'Nesto je krenulo po zlu, pousajte kasnije'
          );
        },
      });
    }
  }

  handleCollectionChange() {
    const currentSelection = this.form.get('selectedCollections')?.value || [];
    const toStore = currentSelection.filter((id: number) => !this.preselectedItems.includes(id));
    const toRemove = this.preselectedItems.filter((id: number) => !currentSelection.includes(id));

    // 1. No changes
    if (toStore.length === 0 && toRemove.length === 0) {
      this.close({ type: 'exit' });
      return;
    }

    if (toStore.length > 0) {
      this.addObjectsToCollection(toStore);
    }

    if (toRemove.length > 0) {
      this.removeObjectsFromCollection(toRemove);
    }

    let resultType = 'success';
    if (toStore.length > 0 && toRemove.length > 0) resultType = 'success-both';
    else if (toStore.length > 0) resultType = 'success-add';
    else if (toRemove.length > 0) resultType = 'success-remove';

    this.close({ type: resultType });
  }

  addObjectsToCollection(toStore: number[]) {
    this.collectionService
      .addItemToCollectionBulk(new AddCollectionItemsModel(toStore, this.objectType, this.objectId))
      .subscribe({
        next: (response: any) => {},
      });
  }

  removeObjectsFromCollection(toRemove: number[]) {
    this.collectionService
      .removeItemFromCollectionBulk(toRemove, this.objectId, this.objectType)
      .subscribe({
        next: (response: any) => {},
      });
  }
}
