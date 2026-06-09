import { AfterViewInit, Component, ViewChild, ViewContainerRef } from '@angular/core';
import { ModalService } from '../../../core/services/modal.service';

@Component({
  selector: 'app-modal-host',
  imports: [],
  templateUrl: './modal-host.html',
  styleUrl: './modal-host.css'
})
export class ModalHost implements AfterViewInit{
  @ViewChild('container', { read: ViewContainerRef}) container!: ViewContainerRef;

  constructor(private modalService: ModalService){}

  ngAfterViewInit(): void {
    this.modalService.registerContainer(this.container);

  }
}
