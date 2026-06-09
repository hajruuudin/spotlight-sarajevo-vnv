import { Component, OnInit } from '@angular/core';
import { ButtonPrimary } from '../../button-primary/button-primary';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
import { TextInput } from '../../text-input/text-input';
import { TextArea } from '../../text-area/text-area';
import { SliderInput } from '../../slider-input/slider-input';
import { SessionService } from '../../../core/services/session.service';
import { SpotReviewCreateModel, SpotReviewModel } from '../../../shared/models/spot.model';
import { SpotService } from '../../../services/spot.service';
import { HttpErrorResponse } from '@angular/common/http';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-add-review-modal',
  imports: [ButtonPrimary, ReactiveFormsModule, TextInput, TextArea, SliderInput, TranslocoPipe],
  templateUrl: './add-review-modal.html',
  styleUrl: './add-review-modal.css',
  host: {
    class: `fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]`,
  },
})
export class AddReviewModal implements OnInit {
  protected close!: (result?: any) => void;
  protected reviewType: boolean = true;
  protected spotId: number = 0;
  protected organiserId: number = 0;

  protected form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    if (this.reviewType) {
      this.form = this.fb.group({
        header: [''],
        body: [''],
        overallRating: [5.5, Validators.required],
        affordability: [5.5, Validators.required],
        accessibility: [5.5, Validators.required],
        atmosphere: [5.5, Validators.required],
        localeQuality: [5.5, Validators.required],
        staffKindness: [5.5, Validators.required],
        cleanliness: [5.5, Validators.required],
      });
    } else {
      this.form = this.fb.group({
        header: [''],
        body: [''],
        overallRating: [5.5, Validators.required],
        atmosphere: [5.5, Validators.required],
        quality: [5.5, Validators.required],
        enjoyability: [5.5, Validators.required],
      });
    }
  }

  onFormSubmit() {
    if (!this.form.valid) {
      return this.close({ type: 'invalid' });
    }

    if (this.reviewType) {
      const reviewData = {
        spotId: this.spotId,
        ...this.form.value,
      };

      this.close({
        type: 'add',
        data: reviewData,
      });
    } else if (!this.reviewType) {
      const reviewData = {
        organiserId: this.organiserId,
        ...this.form.value,
      };

      this.close({
        type: 'add',
        data: reviewData,
      });
    }
  }

  onClose() {
    this.close({ type: 'cancel' });
  }
}
