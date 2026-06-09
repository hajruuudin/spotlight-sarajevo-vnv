import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
import {
  EventOrganiserCreateModel,
  EventOrganiserModel,
  EventOrganiserUpdateModel,
} from '../../../shared/models/event.model';
import { ButtonPrimary } from '../../button-primary/button-primary';
import { ButtonSecondary } from '../../button-secondary/button-secondary';
import { TextInput } from '../../text-input/text-input';
import { SelectGroup } from '../../select-group/select-group';
import { ImageUploadService } from '../../../services/image-upload.service';
import { MediaCreateModel } from '../../../shared/models/shared.model';
import { DecimalPipe } from '@angular/common';
import { CategoryService } from '../../../services/category.service';
import { EventCategoryModel } from '../../../shared/models/category.model';
import { EventService } from '../../../services/event.service';

@Component({
  selector: 'app-event-organiser-modal',
  imports: [ReactiveFormsModule, ButtonPrimary, ButtonSecondary, TextInput, SelectGroup, DecimalPipe],
  templateUrl: './event-organiser-modal.html',
  styleUrl: './event-organiser-modal.css',
  host: {
    class: 'fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]',
  },
})
export class EventOrganiserModal implements OnInit {
  protected close!: (result?: any) => void;
  @Input() protected organiserModel: EventOrganiserModel | null = null;

  protected form!: FormGroup;
  protected newThumbnailFile: File | null = null;
  protected newThumbnailPreview: string | null = null;
  protected isSaving: boolean = false;
  protected categoryOptions: { label: string; value: any }[] = [];

  constructor(
    private fb: FormBuilder,
    private toastr: HotToastService,
    private imageUploadService: ImageUploadService,
    private categoryService: CategoryService,
    private eventService: EventService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      organiserName: [this.organiserModel?.organiserName ?? '', Validators.required],
      organiserCreationDate: [this.organiserModel?.organiserCreationDate.substring(0, 10) ?? ''],
      organiserCategoryId: [this.organiserModel?.organiserCategoryId ?? null, Validators.required],
      organiserPhone: [this.organiserModel?.organiserPhone ?? '', Validators.required],
      organiserEmail: [
        this.organiserModel?.organiserEmail ?? '',
        [Validators.required, Validators.email],
      ],
      organiserWebsite: [this.organiserModel?.organiserWebsite ?? '', Validators.required],
    });

    this.categoryService.getAllEventCategories().subscribe({
      next: (categories: EventCategoryModel[]) => {
        this.categoryOptions = categories.map((c) => ({
          label: c.eventCategoryNameEn,
          value: c.id,
        }));
        this.cdr.markForCheck();
      },
    });
  }

  onThumbnailSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;
    const file = input.files[0];
    this.newThumbnailFile = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      this.newThumbnailPreview = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  }

  removeThumbnailSelection(): void {
    this.newThumbnailFile = null;
    this.newThumbnailPreview = null;
  }

  onFormSubmit(): void {
    if (!this.form.valid) {
      this.toastr.info('All required fields must be filled!');
      return;
    }

    this.isSaving = true;
    const formVal = this.form.getRawValue();

    // Check if this is a CREATE operation (organiserModel is null)
    if (this.organiserModel === null) {
      this.handleCreateOrganiser(formVal);
    } else {
      // This is an EDIT operation (organiserModel is provided)
      this.handleEditOrganiser(formVal);
    }
  }

  private handleCreateOrganiser(formVal: any): void {
    if (this.newThumbnailFile) {
      this.imageUploadService.uploadImage(this.newThumbnailFile).subscribe({
        next: (response) => {
          const newThumbnail: MediaCreateModel = new MediaCreateModel(
            0,
            'ORGANISER',
            response.data.url,
            response.data.delete_url,
            true,
          );

          const createPayload = new EventOrganiserCreateModel(
            formVal.organiserCategoryId,
            formVal.organiserCreationDate,
            formVal.organiserEmail,
            formVal.organiserName,
            formVal.organiserPhone,
            formVal.organiserWebsite,
            newThumbnail,
          );

          this.eventService.addNewOrganiser(createPayload).subscribe({
            next: (createdOrganiser: EventOrganiserModel) => {
              console.log('[EventOrganiserModal] Organiser created successfully:', createdOrganiser);
              this.isSaving = false;
              this.toastr.success('Organiser created successfully!');
              this.close({ type: 'save', payload: createdOrganiser });
            },
            error: (err) => {
              console.error('[EventOrganiserModal] Create failed:', err);
              this.isSaving = false;
              this.toastr.error('Failed to create organiser.');
            },
          });
        },
        error: (err) => {
          console.error('[EventOrganiserModal] Thumbnail upload failed:', err);
          this.isSaving = false;
          this.toastr.error('Failed to upload thumbnail image.');
        },
      });
    } else {
      // Create without image
      const createPayload = new EventOrganiserCreateModel(
        formVal.organiserCategoryId,
        formVal.organiserCreationDate,
        formVal.organiserEmail,
        formVal.organiserName,
        formVal.organiserPhone,
        formVal.organiserWebsite,
        new MediaCreateModel(0, 'ORGANISER', '', '', true),
      );

      this.eventService.addNewOrganiser(createPayload).subscribe({
        next: (createdOrganiser: EventOrganiserModel) => {
          console.log('[EventOrganiserModal] Organiser created successfully:', createdOrganiser);
          this.isSaving = false;
          this.toastr.success('Organiser created successfully!');
          this.close({ type: 'save', payload: createdOrganiser });
        },
        error: (err) => {
          console.error('[EventOrganiserModal] Create failed:', err);
          this.isSaving = false;
          this.toastr.error('Failed to create organiser.');
        },
      });
    }
  }

  private handleEditOrganiser(formVal: any): void {
    if (this.newThumbnailFile) {
      this.imageUploadService.uploadImage(this.newThumbnailFile).subscribe({
        next: (response) => {
          const newThumbnail: MediaCreateModel = new MediaCreateModel(
            this.organiserModel!.id,
            'ORGANISER',
            response.data.url,
            response.data.delete_url,
            true,
          );
          const updatePayload = new EventOrganiserUpdateModel(
            this.organiserModel!.id,
            formVal.organiserCategoryId,
            formVal.organiserCreationDate,
            formVal.organiserEmail,
            formVal.organiserName,
            formVal.organiserPhone,
            formVal.organiserWebsite,
            this.organiserModel?.thumbnailImage ? this.organiserModel.thumbnailImage.imageUrl : '',
            newThumbnail,
          );

          this.eventService.updateEventOrganiser(updatePayload).subscribe({
            next: (updatedOrganiser: EventOrganiserModel) => {
              console.log('[EventOrganiserModal] Organiser updated successfully:', updatedOrganiser);
              this.isSaving = false;
              this.toastr.success('Organiser updated successfully!');
              this.close({ type: 'save', payload: updatedOrganiser });
            },
            error: (err) => {
              console.error('[EventOrganiserModal] Update failed:', err);
              this.isSaving = false;
              this.toastr.error('Failed to update organiser.');
            },
          });
        },
        error: (err) => {
          console.error('[EventOrganiserModal] Thumbnail upload failed:', err);
          this.isSaving = false;
          this.toastr.error('Failed to upload thumbnail image.');
        },
      });
    } else {
      const updatePayload = new EventOrganiserUpdateModel(
        this.organiserModel!.id,
        formVal.organiserCategoryId,
        formVal.organiserCreationDate,
        formVal.organiserEmail,
        formVal.organiserName,
        formVal.organiserPhone,
        formVal.organiserWebsite,
        this.organiserModel?.thumbnailImage ? this.organiserModel.thumbnailImage.imageUrl : '',
        new MediaCreateModel(
          this.organiserModel!.id,
          'ORGANISER',
          this.organiserModel?.thumbnailImage ? this.organiserModel.thumbnailImage.imageUrl : '',
          '',
          true,
        ),
      );

      this.eventService.updateEventOrganiser(updatePayload).subscribe({
        next: (updatedOrganiser: EventOrganiserModel) => {
          console.log('[EventOrganiserModal] Organiser updated successfully:', updatedOrganiser);
          this.isSaving = false;
          this.toastr.success('Organiser updated successfully!');
          this.close({ type: 'save', payload: updatedOrganiser });
        },
        error: (err) => {
          console.error('[EventOrganiserModal] Update failed:', err);
          this.isSaving = false;
          this.toastr.error('Failed to update organiser.');
        },
      });
    }
  }

  onClose(): void {
    this.close({ type: 'cancel' });
  }
}
