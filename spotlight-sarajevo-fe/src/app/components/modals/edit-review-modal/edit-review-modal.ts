import { Component, Input, OnInit } from '@angular/core';
import { ButtonPrimary } from '../../button-primary/button-primary';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
import { TextInput } from '../../text-input/text-input';
import { TextArea } from '../../text-area/text-area';
import { SliderInput } from '../../slider-input/slider-input';
import { SessionService } from '../../../core/services/session.service';
import {
  SpotOverviewModel,
  SpotReviewCreateModel,
  SpotReviewModel,
} from '../../../shared/models/spot.model';
import { SpotService } from '../../../services/spot.service';
import { HttpErrorResponse } from '@angular/common/http';
import { TranslocoPipe } from '@ngneat/transloco';
import { EventOrganiserReviewModel } from '../../../shared/models/event.model';

@Component({
  selector: 'app-add-review-modal',
  imports: [ButtonPrimary, ReactiveFormsModule, TextInput, TextArea, SliderInput, TranslocoPipe],
  templateUrl: './edit-review-modal.html',
  styleUrl: './edit-review-modal.css',
  host: {
    class: `fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]`,
  },
})
export class EditReviewModal implements OnInit {
  @Input() protected reviewModel!: SpotReviewModel | EventOrganiserReviewModel;
  @Input() protected spotId: number = 0;
  @Input() protected organiserId: number = 0;

  protected close!: (result?: any) => void;

  protected form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    if (this.isSpotReview(this.reviewModel)) {
      this.form = this.fb.group({
        header: [this.reviewModel.header],
        body: [this.reviewModel.body],
        overallRating: [this.reviewModel.userOverallRating, Validators.required],
        affordability: [this.reviewModel.userAffordability, Validators.required],
        accessibility: [this.reviewModel.userAccessibility, Validators.required],
        atmosphere: [this.reviewModel.userAtmosphere, Validators.required],
        localeQuality: [this.reviewModel.userLocaleQuality, Validators.required],
        staffKindness: [this.reviewModel.userStaffKindness, Validators.required],
        cleanliness: [this.reviewModel.userCleanliness, Validators.required],
      });
    } else if (this.isOrganiserReview(this.reviewModel)) {
      console.log(this.reviewModel)
      this.form = this.fb.group({
        header: [this.reviewModel.header],
        body: [this.reviewModel.body],
        overallRating: [this.reviewModel.userOverallRating, Validators.required],
        atmosphere: [this.reviewModel.userOrganiserAtmosphere, Validators.required],
        quality: [this.reviewModel.userOrganiserQuality, Validators.required],
        enjoyability: [this.reviewModel.userOrganiserEnjoyability, Validators.required],
      });
    }
  }

  onFormSubmit() {
    if (!this.form.valid) {
      return this.close({ type: 'invalid' });
    }

    if (this.isSpotReview(this.reviewModel)) {
      const reviewData = {
        spotId: this.spotId,
        ...this.form.value,
      };

      this.close({
        type: 'edit',
        data: reviewData,
      });
    } else if (this.isOrganiserReview(this.reviewModel)) {
      const reviewData = {
        organiserId: this.organiserId,
        ...this.form.value,
      };

      this.close({
        type: 'edit',
        data: reviewData,
      });
    }
  }

  onClose() {
    this.close({ type: 'cancel' });
  }

  get reviewType(){
    if (this.isSpotReview(this.reviewModel)){
      return true
    } else {
      return false
    }
  }

  private isSpotReview(model: any): model is SpotReviewModel {
    return model && 'userAffordability' in model;
  }

  private isOrganiserReview(model: any): model is EventOrganiserReviewModel {
    return model && 'userOrganiserAtmosphere' in model;
  }
}
