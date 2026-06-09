import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TranslocoPipe } from '@ngneat/transloco';
import { PageHeader } from '../../../components/page-header/page-header';
import { EventCreateModel } from '../../../shared/models/event.model';
import { EventService } from '../../../services/event.service';
import { SessionService } from '../../../core/services/session.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { SpinnerService } from '../../../core/services/spinner.service';
import { TextInput } from '../../../components/text-input/text-input';
import { TextArea } from '../../../components/text-area/text-area';
import { SelectGroup } from '../../../components/select-group/select-group';
import { MultiSelectGroup } from '../../../components/multiselect-group/multiselect-group';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';
import { ButtonSecondary } from '../../../components/button-secondary/button-secondary';
import { CategoryService } from '../../../services/category.service';
import { TagService } from '../../../services/tag.service';
import { EventCategoryModel, TagModel } from '../../../shared/models/category.model';
import { ImageUploadService } from '../../../services/image-upload.service';
import { MediaCreateModel } from '../../../shared/models/shared.model';
import { forkJoin, of } from 'rxjs';
import { FormArray, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ModalService } from '../../../core/services/modal.service';
import { EventOrganiserModal } from '../../../components/modals/event-organiser-modal/event-organiser-modal';
import { EventOrganiserUpdateModel } from '../../../shared/models/event.model';

@Component({
  selector: 'app-admin-add-events',
  imports: [
    CommonModule,
    TranslocoPipe,
    PageHeader,
    ReactiveFormsModule,
    TextInput,
    TextArea,
    SelectGroup,
    MultiSelectGroup,
    ButtonPrimary,
    ButtonSecondary,
  ],
  templateUrl: './admin-add-events.html',
  styleUrl: './admin-add-events.css',
})
export class AdminAddEvents implements OnInit {
  protected basicInformationForm: FormGroup;
  protected locationAttributesForm: FormGroup;
  protected eventDetailsForm: FormGroup;

  protected categoryOptions: { label: string; value: any }[] = [];
  protected tagOptions: { label: string; value: any }[] = [];
  protected organiserOptions: { label: string; value: any }[] = [];

  protected newThumbnailFile: File | null = null;
  protected newThumbnailPreview: string | null = null;
  protected newImageFiles: File[] = [];
  protected newImagePreviews: string[] = [];

  protected selectedOrganiserId: number | null = null;
  protected selectedOrganiserName: string = '';
  protected newOrganisers: EventOrganiserUpdateModel[] = [];

  protected columnLang: string = 'en';
  protected eventLanguageOptions = [
    { label: 'English', value: 'en' },
    { label: 'Bosnian', value: 'bs' },
    { label: 'Croatian', value: 'hr' },
    { label: 'Serbian', value: 'sr' },
  ];

  private readonly modal = inject(ModalService);

  constructor(
    protected eventService: EventService,
    protected categoryService: CategoryService,
    protected tagService: TagService,
    protected imageUploadService: ImageUploadService,
    protected spinnerService: SpinnerService,
    protected toastService: HotToastService,
    protected fb: FormBuilder,
    protected sessionService: SessionService,
    protected cdr: ChangeDetectorRef,
    protected router: Router,
  ) {
    this.basicInformationForm = this.fb.group({
      slug: [''],
      officialNameEn: [''],
      officialNameBs: [''],
      smallDescriptionEn: [''],
      smallDescriptionBs: [''],
      fullDescriptionEn: [''],
      fullDescriptionBs: [''],
    });

    this.locationAttributesForm = this.fb.group({
      latitude: [0],
      longitude: [0],
      location: [''],
      locationLinkSlug: [''],
      categoryId: [''],
      eventTagIds: [[]],
    });

    this.eventDetailsForm = this.fb.group({
      startDate: [''],
      endDate: [''],
      entryPrice: [0],
      cancelRefund: [false],
      eventLanguage: ['en'],
      ageLimit: [0],
      reservation: [false],
    });
  }

  ngOnInit(): void {
    this.categoryService.getAllEventCategories().subscribe({
      next: (categories) => {
        this.categoryOptions = this.transformEventCategoriesForOptions(
          categories,
          this.columnLang as 'bs' | 'en',
        );
      },
    });

    this.tagService.findAll().subscribe({
      next: (tags) => {
        this.tagOptions = this.transformTagsForOptions(tags);
      },
    });

    this.eventService.findEventOrganisers().subscribe({
      next: (organisers) => {
        this.organiserOptions = organisers.map((organiser) => ({
          label: organiser.organiserName,
          value: organiser.id,
        }));
      },
    });
  }

  private transformEventCategoriesForOptions(
    categories: EventCategoryModel[],
    language: 'bs' | 'en',
  ): { label: string; value: any }[] {
    return categories.map((category) => ({
      label: language === 'en' ? category.eventCategoryNameEn : category.eventCategoryNameBs,
      value: category.id,
    }));
  }

  private transformTagsForOptions(tags: TagModel[]): { label: string; value: any }[] {
    return tags.map((tag) => ({
      label: this.columnLang === 'en' ? tag.tagNameEn : tag.tagNameBs,
      value: tag.id,
    }));
  }

  onThumbnailSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.newThumbnailFile = input.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        this.newThumbnailPreview = e.target?.result as string;
      };
      reader.readAsDataURL(input.files[0]);
    }
  }

  removeThumbnailSelection(): void {
    this.newThumbnailFile = null;
    this.newThumbnailPreview = null;
  }

  onNewImagesSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      Array.from(input.files).forEach((file) => {
        this.newImageFiles.push(file);
        const reader = new FileReader();
        reader.onload = (e) => {
          this.newImagePreviews.push(e.target?.result as string);
        };
        reader.readAsDataURL(file);
      });
    }
  }

  removeNewImage(index: number): void {
    this.newImageFiles.splice(index, 1);
    this.newImagePreviews.splice(index, 1);
  }

  async openOrganiserModal(): Promise<void> {
    const result = await this.modal.openAsync<any>(EventOrganiserModal, {
      organiserModel: null,
    });

    if (result?.type === 'save') {
      const newOrganiser = result.payload as EventOrganiserUpdateModel;
      this.newOrganisers.push(newOrganiser);
      
      this.organiserOptions.push({
        label: newOrganiser.organiserName,
        value: newOrganiser.id,
      });
      
      this.selectedOrganiserId = newOrganiser.id;
      this.selectedOrganiserName = newOrganiser.organiserName;
      
      this.toastService.success('Organiser added successfully!');
      this.cdr.markForCheck();
    }
  }

  onOrganiserSelected(event: any): void {
    const organiserId = event;
    this.selectedOrganiserId = organiserId;
    
    const selected = this.organiserOptions.find(opt => opt.value === organiserId);
    this.selectedOrganiserName = selected?.label || '';
  }

  onCreateEvent(): void {
    if (
      !this.basicInformationForm.valid ||
      !this.locationAttributesForm.valid ||
      !this.eventDetailsForm.valid
    ) {
      this.toastService.error('Please fill in all required fields');
      return;
    }

    if (!this.selectedOrganiserId) {
      this.toastService.error('Please select an organiser');
      return;
    }

    this.spinnerService.showNavigateSpinner();

    const thumbnailUpload$ = this.newThumbnailFile
      ? this.imageUploadService.uploadImage(this.newThumbnailFile)
      : of(null);

    const newImagesUpload$ =
      this.newImageFiles.length > 0
        ? this.imageUploadService.uploadMultipleImages(this.newImageFiles)
        : of([]);

    forkJoin({
      thumbnail: thumbnailUpload$,
      newImages: newImagesUpload$,
    }).subscribe({
      next: (results) => {
        const imagesToBeUploaded: MediaCreateModel[] = results.newImages.map(
          (response) =>
            new MediaCreateModel(
              0,
              'EVENT',
              response.data.url,
              response.data.delete_url,
              false,
            ),
        );

        const newThumbnailImage = results.thumbnail
          ? new MediaCreateModel(
              0,
              'EVENT',
              results.thumbnail.data.url,
              results.thumbnail.data.delete_url,
              true,
            )
          : null;

        const finalPayload = new EventCreateModel(
          this.basicInformationForm.value.slug,
          this.basicInformationForm.value.officialNameBs,
          this.basicInformationForm.value.officialNameEn,
          this.basicInformationForm.value.smallDescriptionBs,
          this.basicInformationForm.value.smallDescriptionEn,
          this.basicInformationForm.value.fullDescriptionBs,
          this.basicInformationForm.value.fullDescriptionEn,
          this.locationAttributesForm.value.latitude,
          this.locationAttributesForm.value.longitude,
          this.locationAttributesForm.value.location,
          this.locationAttributesForm.value.locationLinkSlug,
          this.locationAttributesForm.value.categoryId,
          this.locationAttributesForm.value.eventTagIds || [],
          this.eventDetailsForm.value.startDate,
          this.eventDetailsForm.value.endDate,
          this.eventDetailsForm.value.entryPrice,
          this.eventDetailsForm.value.cancelRefund,
          this.eventDetailsForm.value.eventLanguage,
          this.eventDetailsForm.value.ageLimit,
          this.eventDetailsForm.value.reservation,
          this.selectedOrganiserId!,
          newThumbnailImage,
          imagesToBeUploaded,
        );

        this.eventService.createEvent(finalPayload).subscribe({
          next: (response: any) => {
            this.spinnerService.hideNavigateSpinner();
            this.toastService.success('Event created successfully!');
            this.router.navigate(['/admin/events-overview']);
          },
          error: (error) => {
            this.spinnerService.hideNavigateSpinner();
            this.toastService.error('Failed to create event. Please try again.');
          },
        });
      },
      error: (error) => {
        this.spinnerService.hideNavigateSpinner();
        this.toastService.error('Failed to upload images. Please try again.');
      },
    });
  }

  onCancel(): void {
    this.router.navigate(['/admin/events-overview']);
  }
}
