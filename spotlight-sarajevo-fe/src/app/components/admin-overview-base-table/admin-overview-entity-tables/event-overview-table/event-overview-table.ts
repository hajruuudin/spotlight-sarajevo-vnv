import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  inject,
} from '@angular/core';
import { AdminOverviewBaseTable } from '../../admin-overview-table';
import {
  EventOrganiserReviewModel,
  EventOrganiserUpdateModel,
  EventOverviewModel,
  EventShorthandModel,
  EventUpdateModel,
} from '../../../../shared/models/event.model';
import { ButtonPrimary } from '../../../button-primary/button-primary';
import { ButtonSecondary } from '../../../button-secondary/button-secondary';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TextInput } from '../../../text-input/text-input';
import { TextArea } from '../../../text-area/text-area';
import { SelectGroup } from '../../../select-group/select-group';
import { MultiSelectGroup } from '../../../multiselect-group/multiselect-group';
import { OrganiserReiewCard } from '../../../organiser-reiew-card/organiser-reiew-card';
import { MediaCreateModel } from '../../../../shared/models/shared.model';
import { forkJoin, of } from 'rxjs';
import { EventCategoryModel, TagModel } from '../../../../shared/models/category.model';
import { SortOptions } from '../../../../shared/constants/SortOptions';
import { DatePipe, DecimalPipe } from '@angular/common';
import { ModalService } from '../../../../core/services/modal.service';
import { EventOrganiserModal } from '../../../modals/event-organiser-modal/event-organiser-modal';

/**
 * Event Overview Table Component
 *
 * Implementation of AdminOverviewBaseTable for events. Displays event shorthand data in a paginated
 * tabular layout. When a row is expanded, a 5-section editor panel is shown:
 *   1. Basic Details     – slug, names, small/full descriptions
 *   2. Location & Tags   – lat, lon, venue name, location slug, category, tags
 *   3. Event Details     – dates, pricing, language, age limit, refund & reservation policy
 *   4. Images            – thumbnail + gallery management
 *   5. Organiser         – read-only organiser info + paginated organiser reviews
 *
 * Data mutation is handled by the parent AdminEventOverview component.
 */
@Component({
  selector: 'app-event-overview-table',
  imports: [
    ButtonPrimary,
    ButtonSecondary,
    ReactiveFormsModule,
    TextInput,
    TextArea,
    SelectGroup,
    MultiSelectGroup,
    OrganiserReiewCard,
    DecimalPipe,
    DatePipe
  ],
  templateUrl: './event-overview-table.html',
  styleUrl: './event-overview-table.css',
  host: {
    class: 'w-full',
  },
})
export class EventOverviewTable extends AdminOverviewBaseTable implements OnInit, OnChanges {
  @Input() tableData: EventShorthandModel[] = [];
  @Input() itemOverview: EventOverviewModel | null = null;
  @Input() organiserReviews: EventOrganiserReviewModel[] = [];
  @Input() isLoadingReviews: boolean = false;

  @Output() onSaveChange: EventEmitter<EventUpdateModel> = new EventEmitter<EventUpdateModel>();
  @Output() onOrganiserSaveChange: EventEmitter<EventOrganiserUpdateModel> = new EventEmitter<EventOrganiserUpdateModel>();
  @Output() onReviewLoadMore: EventEmitter<number> = new EventEmitter<number>();
  @Output() onReviewSortChange: EventEmitter<string> = new EventEmitter<string>();

  protected basicInformationForm: FormGroup;
  protected locationAttributesForm: FormGroup;
  protected eventDetailsForm: FormGroup;

  protected categoryOptions: { label: string; value: any }[] = [];
  protected tagOptions: { label: string; value: any }[] = [];

  protected newThumbnailFile: File | null = null;
  protected newThumbnailPreview: string | null = null;
  protected newImageFiles: File[] = [];
  protected newImagePreviews: string[] = [];
  protected imagesToDelete: Set<number> = new Set();

  protected currentReviewSortOption: SortOptions = SortOptions.RATING;
  protected reviewPage: number = 0;

  private readonly modal = inject(ModalService);

  constructor() {
    super();

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
      eventLat: [0],
      eventLon: [0],
      location: [''],
      locationLinkSlug: [''],
      categoryId: [''],
      eventTagIds: [this.mapTagIds(undefined)],
    });

    this.eventDetailsForm = this.fb.group({
      startDate: [''],
      endDate: [''],
      entryPrice: [0],
      cancelRefund: [false],
      eventLanguage: [''],
      ageLimit: [0],
      reservation: [false],
    });
  }

  /* ===================================================== */
  /* ============ LIFECYCLE UPDATE FUNCTIONS ============= */
  /* ===================================================== */

  ngOnInit(): void {
    this.categoryService.getAllEventCategories().subscribe({
      next: (categories: EventCategoryModel[]) => {
        this.categoryOptions = categories.map((c) => ({
          label: this.columnLang === 'en' ? c.eventCategoryNameEn : c.eventCategoryNameBs,
          value: c.id,
        }));
      },
    });

    this.tagService.findAll().subscribe({
      next: (tags: TagModel[]) => {
        this.tagOptions = tags.map((tag) => ({
          label: this.columnLang === 'en' ? tag.tagNameEn : tag.tagNameBs,
          value: tag.id,
        }));
      },
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['itemOverview']?.currentValue) {
      const overview: EventOverviewModel = changes['itemOverview'].currentValue;

      this.basicInformationForm.patchValue(
        {
          slug: overview.slug,
          officialNameEn: overview.officialNameEn,
          officialNameBs: overview.officialNameBs,
          smallDescriptionEn: overview.smallDescriptionEn,
          smallDescriptionBs: overview.smallDescriptionBs,
          fullDescriptionEn: overview.fullDescriptionEn,
          fullDescriptionBs: overview.fullDescriptionBs,
        },
        { emitEvent: false },
      );

      this.locationAttributesForm.patchValue(
        {
          eventLat: overview.eventLat,
          eventLon: overview.eventLon,
          location: overview.location,
          locationLinkSlug: overview.locationLinkSlug,
          categoryId: overview.categoryId,
          eventTagIds: this.mapTagIds(overview.eventTags),
        },
        { emitEvent: false },
      );

      this.eventDetailsForm.patchValue(
        {
          startDate: overview.startDate,
          endDate: overview.endDate,
          entryPrice: overview.entryPrice,
          cancelRefund: overview.cancelRefund,
          eventLanguage: overview.eventLanguage,
          ageLimit: overview.ageLimit,
          reservation: overview.reservation,
        },
        { emitEvent: false },
      );
    }
  }

  /* =================================================================================== */
  /* ============ DATA MANIPULATION FUNCTIONS WHICH ARE SENT TO THE PARENT ============= */
  /* =================================================================================== */

  override onSaveChangeSelected(): void {
    if (!this.itemOverview) return;

    this.spinnerService.showNavigateSpinner();

    const thumbnailUpload$ = this.newThumbnailFile
      ? this.imageUploadService.uploadImage(this.newThumbnailFile)
      : of(null);

    const newImagesUpload$ =
      this.newImageFiles.length > 0
        ? this.imageUploadService.uploadMultipleImages(this.newImageFiles)
        : of([]);

    forkJoin({ thumbnail: thumbnailUpload$, newImages: newImagesUpload$ }).subscribe({
      next: (results) => {
        const imagesToBeUploaded: MediaCreateModel[] = results.newImages.map(
          (response) =>
            new MediaCreateModel(
              this.itemOverview!.id,
              'EVENT',
              response.data.url,
              response.data.delete_url,
              false,
            ),
        );

        const newThumbnailImage = results.thumbnail
          ? new MediaCreateModel(
              this.itemOverview!.id,
              'EVENT',
              results.thumbnail.data.url,
              results.thumbnail.data.delete_url,
              true,
            )
          : null;

        const thumbnailImageUrl = results.thumbnail
          ? results.thumbnail.data.url
          : this.itemOverview?.thumbnailImage?.imageUrl || '';

        const finalPayload = new EventUpdateModel(
          this.itemOverview!.id,
          this.basicInformationForm.value.slug,
          this.basicInformationForm.value.officialNameBs,
          this.basicInformationForm.value.officialNameEn,
          this.basicInformationForm.value.smallDescriptionBs,
          this.basicInformationForm.value.smallDescriptionEn,
          this.basicInformationForm.value.fullDescriptionBs,
          this.basicInformationForm.value.fullDescriptionEn,
          this.locationAttributesForm.value.eventLat,
          this.locationAttributesForm.value.eventLon,
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
          thumbnailImageUrl,
          newThumbnailImage,
          imagesToBeUploaded,
          this.imagesToDelete.size > 0 ? Array.from(this.imagesToDelete) : [],
        );

        this.onSaveChange.emit(finalPayload);
        this.resetImageState();
      },
      error: (error) => {
        this.spinnerService.hideNavigateSpinner();
        this.toastService.error('Failed to upload images. Please try again.');
      },
    });
  }

  override onDeleteItemSelected(): void {
    if (this.selectedItemId == null) return;
    this.onDeleteItem.emit(this.selectedItemId);
  }

  /* ============================================================================================ */
  /* ============ UI INTERFACE FUNCTIONS AND HELPERS USED ONLY TO SHOWCASE THE DATA ============= */
  /* ============================================================================================ */

  private resetImageState(): void {
    this.newThumbnailFile = null;
    this.newThumbnailPreview = null;
    this.newImageFiles = [];
    this.newImagePreviews = [];
    this.imagesToDelete.clear();
  }

  private mapTagIds(tags: TagModel[] | null | undefined): number[] {
    if (!Array.isArray(tags)) return [];
    return tags.map((tag) => tag?.id).filter((id): id is number => typeof id === 'number');
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

  toggleImageForDeletion(imageId: number): void {
    if (this.imagesToDelete.has(imageId)) {
      this.imagesToDelete.delete(imageId);
    } else {
      this.imagesToDelete.add(imageId);
    }
  }

  isImageMarkedForDeletion(imageId: number): boolean {
    return this.imagesToDelete.has(imageId);
  }

  onReviewLoadMoreClicked(): void {
    this.reviewPage++;
    this.onReviewLoadMore.emit(this.reviewPage);
  }

  onReviewSortOptionChange(sortOption: string): void {
    if (sortOption === 'ALPHABETICAL_DESC') this.currentReviewSortOption = SortOptions.ALPHABETICAL;
    if (sortOption === 'RATING_DESC') this.currentReviewSortOption = SortOptions.RATING;
    this.reviewPage = 0;
    this.onReviewSortChange.emit(sortOption);
  }

  async openOrganiserModal(): Promise<void> {
    if (!this.itemOverview?.organiser) return;

    const result =await this.modal.openAsync<any>(EventOrganiserModal, {
      organiserModel: this.itemOverview.organiser,
    });

    if (result?.type == 'save'){
      this.onOrganiserSaveChange.emit(result.payload as EventOrganiserUpdateModel)
    }

    if (result?.type == 'cancel'){
      return
    }
  }
}

