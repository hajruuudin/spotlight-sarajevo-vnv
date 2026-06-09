import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TranslocoPipe } from '@ngneat/transloco';
import { PageHeader } from '../../../components/page-header/page-header';
import { SpotCreateModel, SpotWorkHoursModel } from '../../../shared/models/spot.model';
import { SpotService } from '../../../services/spot.service';
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
import { SpotCategoryModel, TagModel } from '../../../shared/models/category.model';
import { ImageUploadService } from '../../../services/image-upload.service';
import { MediaCreateModel } from '../../../shared/models/shared.model';
import { forkJoin, of } from 'rxjs';
import { FormArray } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-add-spots',
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
  templateUrl: './admin-add-spots.html',
  styleUrl: './admin-add-spots.css',
})
export class AdminAddSpots implements OnInit {
  protected basicInformationForm: FormGroup;
  protected attributeInformationForm: FormGroup;
  protected workHoursForm: FormGroup;

  protected categoryOptions: { label: string; value: any }[] = [];
  protected tagOptions: { label: string; value: any }[] = [];

  protected newThumbnailFile: File | null = null;
  protected newThumbnailPreview: string | null = null;
  protected newImageFiles: File[] = [];
  protected newImagePreviews: string[] = [];
  protected columnLang: string = 'en';

  constructor(
    protected spotService: SpotService,
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
      address: [''],
    });

    this.attributeInformationForm = this.fb.group({
      latitude: [0],
      longitude: [0],
      categoryId: [''],
      spotTagIds: [[]],
    });

    this.workHoursForm = this.fb.group({
      workHours: this.buildWorkHoursArray(null),
    });
  }

  ngOnInit(): void {
    this.categoryService.getAllSpotCategories().subscribe({
      next: (categories) => {
        this.categoryOptions = this.transformCategoriesForOptions(
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
  }

  private transformCategoriesForOptions(
    categories: SpotCategoryModel[],
    language: 'bs' | 'en',
  ): { label: string; value: any }[] {
    return categories.map((category) => ({
      label: language === 'en' ? category.spotCategoryNameEn : category.spotCategoryNameBs,
      value: category.id,
    }));
  }

  private transformTagsForOptions(tags: TagModel[]): { label: string; value: any }[] {
    return tags.map((tag) => ({
      label: this.columnLang === 'en' ? tag.tagNameEn : tag.tagNameBs,
      value: tag.id,
    }));
  }

  private buildWorkHoursArray(workHours: any[] | null | undefined): FormArray {
    const array = this.fb.array([] as any[]);

    const allDays = [
      { dayIndex: 0, day: 'Monday' },
      { dayIndex: 1, day: 'Tuesday' },
      { dayIndex: 2, day: 'Wednesday' },
      { dayIndex: 3, day: 'Thursday' },
      { dayIndex: 4, day: 'Friday' },
      { dayIndex: 5, day: 'Saturday' },
      { dayIndex: 6, day: 'Sunday' },
    ];

    allDays.forEach((dayInfo) => {
      const existingHours = Array.isArray(workHours)
        ? workHours.find((wh) => wh.dayIndex === dayInfo.dayIndex)
        : null;

      if (existingHours) {
        array.push(
          this.fb.group({
            dayIndex: [existingHours.dayIndex],
            day: [existingHours.day],
            startTime: [existingHours.startTime],
            endTime: [existingHours.endTime],
            isClosed: [false],
          }) as any,
        );
      } else {
        array.push(
          this.fb.group({
            dayIndex: [dayInfo.dayIndex],
            day: [dayInfo.day],
            startTime: ['09:00'],
            endTime: ['17:00'],
            isClosed: [true],
          }) as any,
        );
      }
    });

    return array;
  }

  get workHoursArray(): FormArray {
    return this.workHoursForm.get('workHours') as FormArray;
  }

  getWorkHourDayControl(index: number): FormGroup {
    return this.workHoursArray.at(index) as FormGroup;
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

  onCreateSpot(): void {
    if (
      !this.basicInformationForm.valid ||
      !this.attributeInformationForm.valid ||
      !this.workHoursForm.valid
    ) {
      this.toastService.error('Please fill in all required fields');
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
              'SPOT',
              response.data.url,
              response.data.delete_url,
              false,
            ),
        );

        const newThumbnailImage = results.thumbnail
          ? new MediaCreateModel(
              0,
              'SPOT',
              results.thumbnail.data.url,
              results.thumbnail.data.delete_url,
              true,
            )
          : null;

        const finalPayload = new SpotCreateModel(
          this.basicInformationForm.value.slug,
          this.basicInformationForm.value.officialNameBs,
          this.basicInformationForm.value.officialNameEn,
          this.basicInformationForm.value.smallDescriptionBs,
          this.basicInformationForm.value.smallDescriptionEn,
          this.basicInformationForm.value.fullDescriptionBs,
          this.basicInformationForm.value.fullDescriptionEn,
          this.attributeInformationForm.value.latitude,
          this.attributeInformationForm.value.longitude,
          this.basicInformationForm.value.address,
          this.attributeInformationForm.value.categoryId,
          this.attributeInformationForm.value.spotTagIds || [],
          this.workHoursArray.value
            .filter((wh: any) => !wh.isClosed)
            .map(
              (wh: any) =>
                new SpotWorkHoursModel(
                  wh.dayIndex,
                  wh.day,
                  wh.startTime,
                  wh.endTime,
                  0, // spotId will be set by backend
                  false,
                ),
            ),
          newThumbnailImage,
          imagesToBeUploaded,
        );

        this.spotService.createSpot(finalPayload).subscribe({
          next: (response: any) => {
            this.spinnerService.hideNavigateSpinner();
            this.toastService.success('Spot created successfully!');
            this.router.navigate(['/admin/spots-overview']);
          },
          error: (error) => {
            this.spinnerService.hideNavigateSpinner();
            this.toastService.error('Failed to create spot. Please try again.');
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
    this.router.navigate(['/admin/spots-overview']);
  }
}
