import { Component, computed, ElementRef, HostListener, OnInit, Renderer2 } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SessionService } from '../../core/services/session.service';
import { AsyncPipe } from '@angular/common';
import { SpinnerComponent } from "../../components/spinner-component/spinner-component";
import { SpinnerService } from '../../core/services/spinner.service';
import { SpinnerNavigate } from "../../components/spinner-navigate/spinner-navigate";

/**
 * Authentication User Interface: Container for authentication related
 * pages. This intercace encapsulates:
 * - The login process
 * - The signup process
 * - Two factor authentication (including email verification)
 * - Password reset function
 */
@Component({
  selector: 'app-auth',
  imports: [RouterOutlet],
  templateUrl: './auth.html',
  styleUrl: './auth.css',
})
export class Auth implements OnInit {
  protected backgroundImage!: HTMLElement;
  public language: 'en' | 'ba' = 'en';
  public languageIcon: string = '/assets/icons/EN.svg';

  constructor(
    public session: SessionService,
    public spinner: SpinnerService,
    private el: ElementRef,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    this.backgroundImage = this.el.nativeElement.querySelector('#backgroundImage');

    const savedTheme = localStorage.getItem('app_theme');
    const html = document.documentElement;

    if (savedTheme) {
      html.setAttribute('data-theme', savedTheme);
    } else {
      html.setAttribute('data-theme', 'dark');
    }

    const savedLang = localStorage.getItem('app_language');
    if (savedLang) {
      this.language = savedLang as 'en' | 'ba';
    }
  }

  @HostListener('document:mousemove', ['$event'])
  onMouseMove(event: MouseEvent): void {
    if (!this.backgroundImage) return;

    const { innerWidth, innerHeight } = window;
    const offsetX = (event.clientX / innerWidth - 0.5) * 10;
    const offsetY = (event.clientY / innerHeight - 0.5) * 10;

    this.renderer.setStyle(
      this.backgroundImage,
      'transform',
      `translate(calc(-50% + ${offsetX}px), calc(-50% + ${offsetY}px)) scale(1.05)`
    );
  }

  toggleTheme(): void {
    this.session.setTheme(this.session.theme() === 'dark' ? 'light' : 'dark');
  }

  toggleLanguage() {
    const newLang = this.session.language() === 'en' ? 'ba' : 'en';
    this.session.setLanguage(newLang);
  }
}
