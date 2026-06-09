import { Component, computed, OnInit, signal } from '@angular/core';
import { environment } from '../environments/environment';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router, RouterOutlet } from '@angular/router';
import { SpinnerComponent } from './components/spinner-component/spinner-component';
import { SpinnerService } from './core/services/spinner.service';
import { SessionService } from './core/services/session.service';
import { filter } from 'rxjs';
import { SpinnerNavigate } from "./components/spinner-navigate/spinner-navigate";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, SpinnerNavigate],
  templateUrl: './app.html'
})
export class App {
  protected loading = false;
  protected apiUrl = environment.API_URL;

  constructor(
    protected spinner: SpinnerService,
    protected session: SessionService,
    protected router: Router
  ) {
    this.session.checkSession();
    this.router.events.pipe(
      filter(event => 
        event instanceof NavigationStart || 
        event instanceof NavigationEnd || 
        event instanceof NavigationCancel || 
        event instanceof NavigationError
      )
    ).subscribe(event => {
      if (event instanceof NavigationStart) {
        this.spinner.showNavigateSpinner()
      } else {
        this.spinner.hideNavigateSpinner()
      }
    })
  }
}
