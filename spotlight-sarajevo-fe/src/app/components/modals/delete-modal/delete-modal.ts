import { Component } from '@angular/core';
import { ButtonPrimary } from '../../button-primary/button-primary';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-delete-modal',
  imports: [ButtonPrimary, TranslocoPipe],
  templateUrl: './delete-modal.html',
  styleUrl: './delete-modal.css',
  host: {
    class: `fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]`,
  },
})
export class DeleteModal {
  protected close!: (result: { confirmed: boolean }) => void;
  protected titleKey: string = '';
  protected confirmMessageKey: string = '';
  protected confirmButtonKey: string = 'common.delete';
  protected cancelButtonKey: string = 'common.cancel';

  confirm() {
    this.close({ confirmed: true });
  }

  cancel() {
    this.close({ confirmed: false });
  }
}
