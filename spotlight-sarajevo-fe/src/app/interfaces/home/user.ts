import { Component } from '@angular/core';
import { NavbarUser } from "../../components/navbar-user/navbar-user";
import { RouterOutlet } from '@angular/router';
import { Footer } from "../../components/footer/footer";
import { SpinnerNavigate } from "../../components/spinner-navigate/spinner-navigate";
import { SpinnerService } from '../../core/services/spinner.service';
import { ModalHost } from "../../components/modals/modal-host/modal-host";

/**
 * Regular User Interface: The largest container, encapsulating:
 * - Homepage for a birds eye view of the application
 * - Searching functions for Spots and Events
 * - Discover page for sponsored and various spots / events
 * - Personal Profile Page
 * - Pages for tourist guides and their overview
 * - Public transport page for information
 * - Other miscelanneous pages.
 */
@Component({
  selector: 'app-user',
  imports: [NavbarUser, RouterOutlet, Footer, ModalHost],
  templateUrl: './user.html',
  styleUrl: './user.css',
  host: {
    class: "w-full min-h-screen flex flex-col justify-start items-stretch"
  }
})
export class User {
  protected loading = false;
  
  constructor(private spinner: SpinnerService) {}
  
  get isLoading() {
    return this.spinner.loadingNavigate();
  }
}
