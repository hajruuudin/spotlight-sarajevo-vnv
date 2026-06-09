import { Injectable, NgZone, signal } from '@angular/core';

/**
 * Custom spinner service for managing different loading states across the application.
 * Provides methods to show and hide global, section, and navigation spinners.
 * Utilizes Angular signals for reactive state management.
 * 
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Injectable({ providedIn: 'root' })
export class SpinnerService {
  loadingGlobal = signal(false);
  loadingSection = signal(false);
  loadingNavigate = signal(false);

  constructor(private zone: NgZone){}
  
  showGlobalSpinner() {
    this.zone.run(() => {
      this.loadingGlobal.set(true);
    });
  }

  hideGlobalSpinner() {
    this.zone.run(() => {
      this.loadingGlobal.set(false);
    });
  }

  showSectionSpinner() {
    this.loadingSection.set(true)
  }

  hideSectionSpinner(){
    this.loadingSection.set(false)
  }

  showNavigateSpinner(){
    this.loadingNavigate.set(true)
  }

  hideNavigateSpinner(){
    this.loadingNavigate.set(false)
  }
}
