import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
import { SessionService } from '../../../core/services/session.service';
import { CollectionModel } from '../../../shared/models/collection.model';
import { TextInput } from "../../text-input/text-input";
import { TranslocoPipe } from '@ngneat/transloco';
import { ButtonPrimary } from "../../button-primary/button-primary";

@Component({
  selector: 'app-edit-collection-modal',
  imports: [TextInput, TranslocoPipe, ReactiveFormsModule, ButtonPrimary],
  templateUrl: './edit-collection-modal.html',
  styleUrl: './edit-collection-modal.css',
  host: {
    class: `fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]`,
  }
})
export class EditCollectionModal {
  protected close!: (result?: any) => void;
  @Input() protected collectionModel!: CollectionModel

  protected form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private toastr: HotToastService,
    private session: SessionService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      collectionName: [this.collectionModel.collectionName, Validators.required],
      collectionDescription: [this.collectionModel.collectionDescription, Validators.required]
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
      type: 'edit',
      data: collectionData,
    });
  }

  onClose() {
    this.close({ type: 'cancel' });
  }
}
