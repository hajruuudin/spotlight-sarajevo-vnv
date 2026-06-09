import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import {
  TouristGuideOverviewModel,
  TouristGuideShorthandModel,
  TouristGuideSectionUpdateModel,
  TouristGuideSectionCreateModel,
  TouristGuideUpdateModel,
} from '../../../../shared/models/tourist.guide.model';
import { MediaCreateModel } from '../../../../shared/models/shared.model';
import { AdminOverviewBaseTable } from '../../admin-overview-table';
import { ButtonPrimary } from '../../../button-primary/button-primary';
import { ButtonSecondary } from '../../../button-secondary/button-secondary';
import { FormArray, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TextInput } from '../../../text-input/text-input';
import { TextArea } from '../../../text-area/text-area';
import { SelectGroup } from '../../../select-group/select-group';
import { GuideType } from '../../../../shared/constants/ObjectTypes';
import { forkJoin, of } from 'rxjs';
import { ImageBBResponse } from '../../../../services/image-upload.service';
import { GuideCategoryModel } from '../../../../shared/models/category.model';

/**
 * Guide Overview Table Component
 *
 * Layout when expanded:
 *  Col 1 – Basic Details (slug, titles, descriptions)
 *  Col 2 – Category (editable select) + Contact Info (EXTERNAL only) + Thumbnail
 *  Col 3..N – One column per guide section (editable title, body, thumbnail; max 6)
 *  Col N+1 – "Add Section" action column (shown when sections < 6)
 */
@Component({
  selector: 'app-guide-overview-table',
  imports: [ButtonPrimary, ButtonSecondary, ReactiveFormsModule, TextInput, TextArea, SelectGroup],
  templateUrl: './guide-overview-table.html',
  styleUrl: './guide-overview-table.css',
  host: { class: 'w-full' },
})
export class GuideOverviewTable extends AdminOverviewBaseTable implements OnChanges, OnInit {
  @Input() tableData: TouristGuideShorthandModel[] = [];
  @Input() itemOverview: TouristGuideOverviewModel | null = null;

  @Output() onSaveChange: EventEmitter<TouristGuideUpdateModel> = new EventEmitter<TouristGuideUpdateModel>();

  protected basicInformationForm: FormGroup;
  protected sectionsFormArray: FormArray;

  protected categoryOptions: { label: string; value: any }[] = [];

  // Guide thumbnail
  protected newThumbnailFile: File | null = null;
  protected newThumbnailPreview: string | null = null;

  protected sectionIds: (number | null)[] = [];
  protected sectionExistingThumbnailUrls: string[] = [];
  protected sectionNewThumbnailFiles: (File | null)[] = [];
  protected sectionNewThumbnailPreviews: (string | null)[] = [];
  protected deletedSectionIds: number[] = [];

  readonly MAX_SECTIONS = 6;

  constructor() {
    super();
    this.sectionsFormArray = this.fb.array([]);
    this.basicInformationForm = this.fb.group({
      slug: [''],
      guideTitleBs: [''],
      guideTitleEn: [''],
      guideSmallDescriptionBs: [''],
      guideSmallDescriptionEn: [''],
      guideFullDescriptionBs: [''],
      guideFullDescriptionEn: [''],
      categoryId: [null],
    });
  }

  /* ===================================================== */
  /* ============ LIFECYCLE UPDATE FUNCTIONS ============= */
  /* ===================================================== */

  ngOnInit(): void {
    this.categoryService.getAllGuideCategories().subscribe({
      next: (categories: GuideCategoryModel[]) => {
        this.categoryOptions = categories.map((c) => ({
          label: c.categoryNameEn,
          value: c.id,
        }));
      },
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['itemOverview']?.currentValue) {
      const overview: TouristGuideOverviewModel = changes['itemOverview'].currentValue;

      this.basicInformationForm.patchValue(
        {
          slug: overview.slug,
          guideTitleBs: overview.guideTitleBs,
          guideTitleEn: overview.guideTitleEn,
          guideSmallDescriptionBs: overview.guideSmallDescriptionBs,
          guideSmallDescriptionEn: overview.guideSmallDescriptionEn,
          guideFullDescriptionBs: overview.guideFullDescriptionBs,
          guideFullDescriptionEn: overview.guideFullDescriptionEn,
          categoryId: overview.categoryId ?? null,
        },
        { emitEvent: false },
      );

      this.sectionsFormArray.clear();
      this.sectionIds = [];
      this.sectionExistingThumbnailUrls = [];
      this.sectionNewThumbnailFiles = [];
      this.sectionNewThumbnailPreviews = [];

      for (const section of overview.sections) {
        this.sectionsFormArray.push(
          this.fb.group({
            sectionTitleBs: [section.sectionTitleBs],
            sectionTitleEn: [section.sectionTitleEn],
            sectionBodyBs: [section.sectionBodyBs],
            sectionBodyEn: [section.sectionBodyEn],
          }),
        );
        this.sectionIds.push(section.id);
        this.sectionExistingThumbnailUrls.push(section.thumbnailImage ? section.thumbnailImage.imageUrl : '');
        this.sectionNewThumbnailFiles.push(null);
        this.sectionNewThumbnailPreviews.push(null);
      }

      this.newThumbnailFile = null;
      this.newThumbnailPreview = null;
      this.deletedSectionIds = [];
    }
  }

  get sectionControls(): FormGroup[] {
    return this.sectionsFormArray.controls as FormGroup[];
  }

  /* =================================================================================== */
  /* ============ DATA MANIPULATION FUNCTIONS WHICH ARE SENT TO THE PARENT ============= */
  /* =================================================================================== */

  protected addSection(): void {
    if (this.sectionsFormArray.length >= this.MAX_SECTIONS) return;
    this.sectionsFormArray.push(
      this.fb.group({
        sectionTitleBs: [''],
        sectionTitleEn: [''],
        sectionBodyBs: [''],
        sectionBodyEn: [''],
      }),
    );
    this.sectionIds.push(null);
    this.sectionExistingThumbnailUrls.push('');
    this.sectionNewThumbnailFiles.push(null);
    this.sectionNewThumbnailPreviews.push(null);
  }

  protected removeSection(index: number): void {
    const id = this.sectionIds[index];
    if (id !== null) {
      this.deletedSectionIds.push(id);
    }
    this.sectionsFormArray.removeAt(index);
    this.sectionIds.splice(index, 1);
    this.sectionExistingThumbnailUrls.splice(index, 1);
    this.sectionNewThumbnailFiles.splice(index, 1);
    this.sectionNewThumbnailPreviews.splice(index, 1);
  }

  protected moveSectionUp(index: number): void {
    if (index === 0) return;
    this.swapSections(index, index - 1);
  }

  protected moveSectionDown(index: number): void {
    if (index === this.sectionsFormArray.length - 1) return;
    this.swapSections(index, index + 1);
  }

  private swapSections(i: number, j: number): void {
    const controlI = this.sectionsFormArray.at(i);
    const controlJ = this.sectionsFormArray.at(j);
    this.sectionsFormArray.setControl(i, controlJ);
    this.sectionsFormArray.setControl(j, controlI);
    [this.sectionIds[i], this.sectionIds[j]] = [this.sectionIds[j], this.sectionIds[i]];
    [this.sectionExistingThumbnailUrls[i], this.sectionExistingThumbnailUrls[j]] = [this.sectionExistingThumbnailUrls[j], this.sectionExistingThumbnailUrls[i]];
    [this.sectionNewThumbnailFiles[i], this.sectionNewThumbnailFiles[j]] = [this.sectionNewThumbnailFiles[j], this.sectionNewThumbnailFiles[i]];
    [this.sectionNewThumbnailPreviews[i], this.sectionNewThumbnailPreviews[j]] = [this.sectionNewThumbnailPreviews[j], this.sectionNewThumbnailPreviews[i]];
  }

  override onSaveChangeSelected(): void {
    if (!this.itemOverview) return;

    this.spinnerService.showNavigateSpinner();

    const existingIndices: number[] = [];
    const newIndices: number[] = [];
    this.sectionControls.forEach((_, i) => {
      if (this.sectionIds[i] !== null) {
        existingIndices.push(i);
      } else {
        newIndices.push(i);
      }
    });

    const guideThumb$ = this.newThumbnailFile
      ? this.imageUploadService.uploadImage(this.newThumbnailFile)
      : of(null);

    const updateThumbUploads$ = existingIndices.map((i) =>
      this.sectionNewThumbnailFiles[i]
        ? this.imageUploadService.uploadImage(this.sectionNewThumbnailFiles[i]!)
        : of(null),
    );

    const addThumbUploads$ = newIndices.map((i) =>
      this.sectionNewThumbnailFiles[i]
        ? this.imageUploadService.uploadImage(this.sectionNewThumbnailFiles[i]!)
        : of(null),
    );

    forkJoin({
      guide: guideThumb$,
      updateThumbs: updateThumbUploads$.length > 0 ? forkJoin(updateThumbUploads$) : of([]),
      addThumbs: addThumbUploads$.length > 0 ? forkJoin(addThumbUploads$) : of([]),
    }).subscribe({
      next: (results) => {
        const newThumbnailImage: MediaCreateModel | null = results.guide
          ? new MediaCreateModel(
              this.itemOverview!.id,
              'TOURIST_GUIDE',
              results.guide.data.url,
              results.guide.data.delete_url,
              true,
            )
          : null;

        const toUpdateSections: TouristGuideSectionUpdateModel[] = existingIndices.map(
          (formIdx, i) => {
            const group = this.sectionControls[formIdx];
            const uploadResult = (results.updateThumbs as (ImageBBResponse | null)[])[i];
            const newThumb: MediaCreateModel | null = uploadResult
              ? new MediaCreateModel(
                  this.sectionIds[formIdx]!,
                  'TOURIST_GUIDE_SECTION',
                  uploadResult.data.url,
                  uploadResult.data.delete_url,
                  true,
                )
              : null;
            return new TouristGuideSectionUpdateModel(
              this.sectionIds[formIdx]!,
              group.value.sectionTitleBs,
              group.value.sectionTitleEn,
              group.value.sectionBodyBs,
              group.value.sectionBodyEn,
              this.sectionExistingThumbnailUrls[formIdx] ?? '',
              newThumb,
              formIdx,
            );
          },
        );

        const toAddSections: TouristGuideSectionCreateModel[] = newIndices.map((formIdx, i) => {
          const group = this.sectionControls[formIdx];
          const uploadResult = (results.addThumbs as (ImageBBResponse | null)[])[i];
          return new TouristGuideSectionCreateModel(
            group.value.sectionTitleBs,
            group.value.sectionTitleEn,
            group.value.sectionBodyBs,
            group.value.sectionBodyEn,
            uploadResult ? uploadResult.data.url : '',
            formIdx,
          );
        });

        const payload = new TouristGuideUpdateModel(
          this.itemOverview!.id,
          this.basicInformationForm.value.slug,
          this.basicInformationForm.value.guideTitleBs,
          this.basicInformationForm.value.guideTitleEn,
          this.basicInformationForm.value.guideSmallDescriptionBs,
          this.basicInformationForm.value.guideSmallDescriptionEn,
          this.basicInformationForm.value.guideFullDescriptionBs,
          this.basicInformationForm.value.guideFullDescriptionEn,
          this.basicInformationForm.value.categoryId,
          this.itemOverview!.thumbnailImage ? this.itemOverview!.thumbnailImage.imageUrl : '',
          newThumbnailImage,
          toUpdateSections,
          toAddSections,
          [...this.deletedSectionIds],
        );

        this.spinnerService.hideNavigateSpinner();
        this.onSaveChange.emit(payload);
      },
      error: () => {
        this.spinnerService.hideNavigateSpinner();
        this.toastService.error('Failed to upload image. Please try again.');
      },
    });
  }

  override onDeleteItemSelected(): void {
    if (this.selectedItemId !== null) {
      this.onDeleteItem.emit(this.selectedItemId);
    }
  }

  /* ============================================================================================ */
  /* ============ UI INTERFACE FUNCTIONS AND HELPERS USED ONLY TO SHOWCASE THE DATA ============= */
  /* ============================================================================================ */


  protected onThumbnailSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.newThumbnailFile = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      this.newThumbnailPreview = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  }

  protected removeThumbnailSelection(): void {
    this.newThumbnailFile = null;
    this.newThumbnailPreview = null;
  }

  protected onSectionThumbnailSelected(index: number, event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.sectionNewThumbnailFiles[index] = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      this.sectionNewThumbnailPreviews[index] = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  }

  protected removeSectionThumbnail(index: number): void {
    this.sectionNewThumbnailFiles[index] = null;
    this.sectionNewThumbnailPreviews[index] = null;
  }

  protected contactInfoEntries(): { key: string; value: string }[] {
    if (!this.itemOverview?.contactInfo) return [];
    return Object.entries(this.itemOverview.contactInfo).map(([key, value]) => ({ key, value }));
  }

  protected isExternal(): boolean {
    return this.itemOverview?.guideType === GuideType.EXTERNAL;
  }
}

