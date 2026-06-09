import { Component } from '@angular/core';
import { Footer } from "../../components/footer/footer";
import { RouterOutlet } from "@angular/router";
import { AdminNavbar } from "../../components/admin-navbar/admin-navbar";
import { ModalHost } from "../../components/modals/modal-host/modal-host";

/**
 * Admin User Interface: Container for all admin-realted pages.
 * This interface encapsulates:
 * - Admin Overview for all types of objects (spots, events, guides, users, transport...)
 * - Adding and Editing objects system wide
 * - Accessing critical data from the database
 */
@Component({
  selector: 'app-admin',
  imports: [Footer, RouterOutlet, AdminNavbar, ModalHost],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
  host: {
    class: "w-full min-h-screen flex flex-col justify-start items-stretch"
  }
})
export class Admin {

}
