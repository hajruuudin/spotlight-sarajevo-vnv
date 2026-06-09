import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TranslocoPipe } from '@ngneat/transloco';
import { NotFoundComponent } from '../../components/not-found-component/not-found-component';
import { ButtonPrimary } from '../../components/button-primary/button-primary';

/**
 * Not Found User Interface: This UI page is loaded only in the case that an
 * unknown URL on the application is accessed, or in case of an error that would
 * prevent a critical function of the system.
 */
@Component({
  selector: 'app-not-found',
  imports: [NotFoundComponent, TranslocoPipe, ButtonPrimary],
  templateUrl: './not-found.html',
  styleUrl: './not-found.css',
})
export class NotFound {
  constructor(private router: Router) {}

  navigateHome(): void {
    this.router.navigate(['/']);
  }
}
