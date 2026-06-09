import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
import { SessionService } from '../../../core/services/session.service';
import { CommunityRequestService } from '../../../services/community.request.service';
import { CommunityRequestCreateModel, CommunityRequestModel } from '../../../shared/models/community.request.model';
import { ObjectType, RequestType } from '../../../shared/constants/ObjectTypes';
import { TranslocoPipe } from '@ngneat/transloco';
import { TextInput } from '../../text-input/text-input';
import { TextArea } from '../../text-area/text-area';
import { SelectGroup } from '../../select-group/select-group';
import { ButtonPrimary } from '../../button-primary/button-primary';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-community-request-modal',
  imports: [
    ReactiveFormsModule,
    TranslocoPipe,
    TextInput,
    TextArea,
    SelectGroup,
    ButtonPrimary
  ],
  templateUrl: './add-community-request-modal.html',
  styleUrl: './add-community-request-modal.css',
  host: {
    class: 'fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]'
  }
})
export class AddCommunityRequestModal implements OnInit {
  // Constants
  readonly REQUEST_TYPE_OPTIONS = [
    { label: 'Add New', value: RequestType.ADD },
    { label: 'Update Existing', value: RequestType.UPDATE },
    { label: 'Remove', value: RequestType.REMOVE },
    { label: 'Other', value: RequestType.OTHER }
  ];

  readonly OBJECT_TYPE_OPTIONS = [
    { label: 'Spot / Location', value: ObjectType.SPOT },
    { label: 'Event', value: ObjectType.EVENT }
  ];

  // Variables
  protected mainForm!: FormGroup;
  protected spotForm!: FormGroup;
  protected eventForm!: FormGroup;
  protected close!: (result?: any) => void;
  protected isSubmitting = false;

  constructor(
    protected fb: FormBuilder,
    protected toastr: HotToastService,
    protected session: SessionService,
    protected crService: CommunityRequestService
  ) {}

  ngOnInit(): void {
    this.initializeForms();
  }

  initializeForms(): void {
    this.mainForm = this.fb.group({
      requestType: [RequestType.ADD, Validators.required],
      objectType: [ObjectType.SPOT, Validators.required],
      requestHeader: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
      requestDescription: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]]
    });

    this.spotForm = this.fb.group({
      spotName: [''],
      spotAddress: [''],
      spotDescription: [''],
      spotCategory: ['']
    });

    this.eventForm = this.fb.group({
      eventName: [''],
      eventLocation: [''],
      eventDescription: [''],
      eventDate: ['']
    });
  }

  get isAddRequest(): boolean {
    return this.mainForm.get('requestType')?.value === RequestType.ADD;
  }

  get isSpotObject(): boolean {
    return this.mainForm.get('objectType')?.value === ObjectType.SPOT;
  }

  get isEventObject(): boolean {
    return this.mainForm.get('objectType')?.value === ObjectType.EVENT;
  }

  get showSpotDetails(): boolean {
    return this.isAddRequest && this.isSpotObject;
  }

  get showEventDetails(): boolean {
    return this.isAddRequest && this.isEventObject;
  }

  onFormSubmit(): void {
    if (!this.mainForm.valid) {
      this.toastr.warning(
        this.session.language() === 'en'
          ? 'Please fill in all required fields'
          : 'Molimo popunite sva obavezna polja'
      );
      return;
    }

    if (this.showSpotDetails && !this.spotForm.get('spotName')?.value) {
      this.toastr.warning(
        this.session.language() === 'en'
          ? 'Please provide the spot name'
          : 'Molimo unesite naziv lokala'
      );
      return;
    }

    if (this.showEventDetails && !this.eventForm.get('eventName')?.value) {
      this.toastr.warning(
        this.session.language() === 'en'
          ? 'Please provide the event name'
          : 'Molimo unesite naziv doga\u0111aja'
      );
      return;
    }

    this.submitRequest();
  }

  submitRequest(): void {
    this.isSubmitting = true;

    let requestBody: any = null;

    if (this.showSpotDetails) {
      requestBody = {
        type: 'SPOT',
        spotName: this.spotForm.get('spotName')?.value,
        spotAddress: this.spotForm.get('spotAddress')?.value,
        spotDescription: this.spotForm.get('spotDescription')?.value,
        spotCategory: this.spotForm.get('spotCategory')?.value
      };
    } else if (this.showEventDetails) {
      requestBody = {
        type: 'EVENT',
        eventName: this.eventForm.get('eventName')?.value,
        eventLocation: this.eventForm.get('eventLocation')?.value,
        eventDescription: this.eventForm.get('eventDescription')?.value,
        eventDate: this.eventForm.get('eventDate')?.value
      };
    }

    const createModel = new CommunityRequestCreateModel(
      this.mainForm.get('requestType')?.value,
      this.mainForm.get('objectType')?.value,
      this.mainForm.get('requestHeader')?.value,
      this.mainForm.get('requestDescription')?.value,
      requestBody
    );

    this.crService.createCommunityRequest(createModel).subscribe({
      next: (response: CommunityRequestModel) => {
        this.isSubmitting = false;
        this.close({ type: 'add', data: response });
      },
      error: (error: HttpErrorResponse) => {
        this.isSubmitting = false;
        this.toastr.error(
          this.session.language() === 'en'
            ? 'Failed to submit request. Please try again.'
            : 'Slanje zahtjeva nije uspjelo. Molimo poku\u0161ajte ponovo.'
        );
      }
    });
  }

  onClose(): void {
    this.close({ type: 'cancel' });
  }
}
