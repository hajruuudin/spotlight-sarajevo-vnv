import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TranslocoPipe } from '@ngneat/transloco';
import { TextInput } from '../../text-input/text-input';
import { ButtonPrimary } from '../../button-primary/button-primary';
import { SelectGroup } from '../../select-group/select-group';
import { HotToastService } from '@ngxpert/hot-toast';
import { SessionService } from '../../../core/services/session.service';

@Component({
  selector: 'app-add-collection-modal',
  imports: [TranslocoPipe, ReactiveFormsModule, TextInput, ButtonPrimary, SelectGroup],
  templateUrl: './add-collection-modal.html',
  styleUrl: './add-collection-modal.css',
  host: {
    class: `fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]`,
  },
})
export class AddCollectionModal implements OnInit {
  protected close!: (result?: any) => void;
  protected selectOptions = [
    { label: 'Spot', value: 1 },
    { label: 'Event', value: 2 },
  ];

  protected form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private toastr: HotToastService,
    private session: SessionService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      collectionName: ['', Validators.required],
      collectionDescription: ['', Validators.required],
      collectionType: [1, Validators.required],
    });
  }

  onFormSubmit() {
    if (!this.form.valid) {
      this.toastr.info(this.session.language() == 'en' ? 'All fields are required' : 'Sva polja su neophodna!');
      return;
    }

    const collectionData = {
      ...this.form.value,
    };

    this.close({
      type: 'add',
      data: collectionData,
    });
  }

  onClose() {
    this.close({ type: 'cancel' });
  }
}
