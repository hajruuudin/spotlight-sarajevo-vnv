import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { PageHeader } from '../../../components/page-header/page-header';
import { ButtonPrimary } from '../../../components/button-primary/button-primary';
import { ButtonSecondary } from '../../../components/button-secondary/button-secondary';
import { TextInput } from '../../../components/text-input/text-input';
import { TextArea } from '../../../components/text-area/text-area';
import { SelectGroup } from '../../../components/select-group/select-group';
import { TouristGuideCreateModel, TouristGuideSectionCreateModel } from '../../../shared/models/tourist.guide.model';
import { GuideType } from '../../../shared/constants/ObjectTypes';
import { GuideCategoryModel } from '../../../shared/models/category.model';
import { CategoryService } from '../../../services/category.service';
import { TouristGuideService } from '../../../services/tourist.guide.service';
import { ImageUploadService, ImageBBResponse } from '../../../services/image-upload.service';
import { SpinnerService } from '../../../core/services/spinner.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { Router } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { MediaCreateModel } from '../../../shared/models/shared.model';

@Component({
  selector: 'app-admin-add-guides',
  imports: [
    PageHeader,
    ButtonPrimary,
    ButtonSecondary,
    ReactiveFormsModule,
    TextInput,
    TextArea,
    SelectGroup,
  ],
  templateUrl: './admin-add-guides.html',
  styleUrl: './admin-add-guides.css',
})
export class AdminAddGuides implements OnInit {
  protected basicInformationForm!: FormGroup;
  protected sectionsFormArray!: FormArray;
  protected contactInfoForm!: FormGroup;

  protected categoryOptions: { label: string; value: any }[] = [];
  protected guideTypeOptions: { label: string; value: GuideType }[] = [
    { label: 'System Guide', value: GuideType.SYSTEM },
    { label: 'External Guide', value: GuideType.EXTERNAL },
  ];

  protected newThumbnailFile: File | null = null;
  protected newThumbnailPreview: string | null = null;

  protected sectionNewThumbnailFiles: (File | null)[] = [];
  protected sectionNewThumbnailPreviews: (string | null)[] = [];

  readonly MAX_SECTIONS = 5;

  constructor(
    protected fb: FormBuilder,
    protected categoryService: CategoryService,
    protected guideService: TouristGuideService,
    protected imageUploadService: ImageUploadService,
    protected spinnerService: SpinnerService,
    protected toastr: HotToastService,
    protected cdr: ChangeDetectorRef,
    protected router: Router,
  ) {
    this.sectionsFormArray = this.fb.array([]);
  }

  ngOnInit(): void {
    this.initializeForm();
    this.loadCategories();
  }

  private initializeForm(): void {
    this.basicInformationForm = this.fb.group({
      guideTitleBs: ['', Validators.required],
      guideTitleEn: ['', Validators.required],
      guideSmallDescriptionBs: ['', Validators.required],
      guideSmallDescriptionEn: ['', Validators.required],
      guideFullDescriptionBs: ['', Validators.required],
      guideFullDescriptionEn: ['', Validators.required],
      categoryId: [null, Validators.required],
      guideType: [GuideType.SYSTEM, Validators.required],
    });

    this.contactInfoForm = this.fb.group({
      email: [''],
      phone: [''],
      website: [''],
      address: [''],
      socialMedia: [''],
    });

    this.addSection();
  }

  private loadCategories(): void {
    this.categoryService.getAllGuideCategories().subscribe({
      next: (categories: GuideCategoryModel[]) => {
        this.categoryOptions = categories.map((c) => ({
          label: c.categoryNameEn,
          value: c.id,
        }));
      },
      error: () => {
        this.toastr.error('Failed to load categories');
      },
    });
  }

  protected get selectedGuideType(): GuideType {
    return this.basicInformationForm.get('guideType')?.value || GuideType.SYSTEM;
  }

  protected isExternal(): boolean {
    return this.selectedGuideType === GuideType.EXTERNAL;
  }

  protected get sectionControls(): FormGroup[] {
    return this.sectionsFormArray.controls as FormGroup[];
  }

  /* =================================================================================== */
  /* ============ SECTION MANAGEMENT FUNCTIONS                                        */
  /* =================================================================================== */

  protected addSection(): void {
    if (this.sectionsFormArray.length >= this.MAX_SECTIONS) {
      this.toastr.warning(`Maximum ${this.MAX_SECTIONS} sections allowed`);
      return;
    }
    this.sectionsFormArray.push(
      this.fb.group({
        sectionTitleBs: ['', Validators.required],
        sectionTitleEn: ['', Validators.required],
        sectionBodyBs: ['', Validators.required],
        sectionBodyEn: ['', Validators.required],
      }),
    );
    this.sectionNewThumbnailFiles.push(null);
    this.sectionNewThumbnailPreviews.push(null);
  }

  protected removeSection(index: number): void {
    this.sectionsFormArray.removeAt(index);
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
    [this.sectionNewThumbnailFiles[i], this.sectionNewThumbnailFiles[j]] = [
      this.sectionNewThumbnailFiles[j],
      this.sectionNewThumbnailFiles[i],
    ];
    [this.sectionNewThumbnailPreviews[i], this.sectionNewThumbnailPreviews[j]] = [
      this.sectionNewThumbnailPreviews[j],
      this.sectionNewThumbnailPreviews[i],
    ];
  }

  /* =================================================================================== */
  /* ============ IMAGE HANDLING FUNCTIONS                                            */
  /* =================================================================================== */

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

  /* =================================================================================== */
  /* ============ FORM SUBMISSION FUNCTIONS                                           */
  /* =================================================================================== */

  protected createGuide(): void {
    if (this.sectionsFormArray.length < 3) {
      this.toastr.error('Minimum 3 sections required');
      return;
    }

    if (this.sectionsFormArray.length > 5) {
      this.toastr.error('Maximum 5 sections allowed');
      return;
    }

    if (!this.basicInformationForm.valid) {
      this.toastr.error('Please fill in all required fields');
      return;
    }

    this.spinnerService.showNavigateSpinner();

    const guideThumbnail$ = this.newThumbnailFile
      ? this.imageUploadService.uploadImage(this.newThumbnailFile)
      : of(null);

    const sectionThumbnailUploads$ = this.sectionNewThumbnailFiles.map((file) =>
      file ? this.imageUploadService.uploadImage(file) : of(null),
    );

    forkJoin({
      guideThumbnail: guideThumbnail$,
      sectionThumbnails:
        sectionThumbnailUploads$.length > 0 ? forkJoin(sectionThumbnailUploads$) : of([]),
    }).subscribe({
      next: (results) => {
        // Create MediaCreateModel for guide thumbnail (only if uploaded)
        const guideThumbnailMedia: MediaCreateModel | null = (results.guideThumbnail as ImageBBResponse | null)
          ? new MediaCreateModel(
              0, // ID will be assigned by backend
              'TOURIST_GUIDE',
              (results.guideThumbnail as ImageBBResponse).data.url,
              (results.guideThumbnail as ImageBBResponse).data.delete_url,
              true,
            )
          : null;

        // Build contact info object (only if EXTERNAL)
        const contactInfo: { [key: string]: string } = {};
        if (this.isExternal()) {
          const contactValues = this.contactInfoForm.value;
          if (contactValues.email) contactInfo['email'] = contactValues.email;
          if (contactValues.phone) contactInfo['phone'] = contactValues.phone;
          if (contactValues.website) contactInfo['website'] = contactValues.website;
          if (contactValues.address) contactInfo['address'] = contactValues.address;
          if (contactValues.socialMedia) contactInfo['socialMedia'] = contactValues.socialMedia;
        }

        // Build sections
        const sections: TouristGuideSectionCreateModel[] = this.sectionControls.map(
          (control, index) => {
            const uploadResult = (results.sectionThumbnails as (ImageBBResponse | null)[])[index];
            return new TouristGuideSectionCreateModel(
              control.value.sectionTitleBs,
              control.value.sectionTitleEn,
              control.value.sectionBodyBs,
              control.value.sectionBodyEn,
              uploadResult?.data?.url || '',
              index,
            );
          },
        );

        // Create the guide payload
        const payload = new TouristGuideCreateModel(
          this.basicInformationForm.value.guideTitleBs,
          this.basicInformationForm.value.guideTitleEn,
          this.basicInformationForm.value.guideSmallDescriptionBs,
          this.basicInformationForm.value.guideSmallDescriptionEn,
          this.basicInformationForm.value.guideFullDescriptionBs,
          this.basicInformationForm.value.guideFullDescriptionEn,
          sections,
          this.basicInformationForm.value.guideType,
          contactInfo,
          guideThumbnailMedia,
          this.basicInformationForm.value.categoryId
        );

        // Submit to backend
        this.guideService.createGuide(payload).subscribe({
          next: () => {
            this.spinnerService.hideNavigateSpinner();
            this.toastr.success('Guide created successfully!');
            this.router.navigate(['/admin/guide-overview']);
          },
          error: () => {
            this.spinnerService.hideNavigateSpinner();
            this.toastr.error('Failed to create guide. Please try again.');
          },
        });
      },
      error: () => {
        this.spinnerService.hideNavigateSpinner();
        this.toastr.error('Failed to upload images. Please try again.');
      },
    });
  }

  protected cancel(): void {
    this.router.navigate(['/admin/guides']);
  }
}
