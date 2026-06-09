import { Component } from '@angular/core';
import { ButtonPrimary } from '../../button-primary/button-primary';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-delete-collection-modal',
  imports: [ButtonPrimary, TranslocoPipe],
  templateUrl: './delete-collection-modal.html',
  styleUrl: './delete-collection-modal.css',
  host: {
    class: `fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]`,
  },
})
export class DeleteCollectionModal {
  protected close!: (result: { confirmed: boolean }) => void;

  confirm() {
    this.close({ confirmed: true });
  }

  cancel() {
    this.close({ confirmed: false });
  }
}
