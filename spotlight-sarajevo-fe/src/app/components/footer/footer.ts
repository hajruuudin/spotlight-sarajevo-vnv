import { Component, Input, OnInit } from '@angular/core';
import { TranslocoPipe } from '@ngneat/transloco';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-footer',
  imports: [TranslocoPipe],
  templateUrl: './footer.html',
  styleUrl: './footer.css',
})
export class Footer {
}
