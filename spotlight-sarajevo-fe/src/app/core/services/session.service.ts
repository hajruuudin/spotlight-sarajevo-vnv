import { Injectable, effect, signal } from '@angular/core';
import { LoggedUserModel } from '../../shared/models/auth.model';
import { TranslocoService } from '@ngneat/transloco';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { catchError, map, of, tap } from 'rxjs';

/**
 * Service for managing user session, including language, theme, and user data.
 * Utilizes Angular signals for reactive state management.
 * Incorporates models and entities: LoggedUserModel
 *
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly APP_LANG_KEY = 'app_language';
  private readonly APP_USER_KEY = 'app_user';
  private readonly APP_THEME_KEY = 'app_theme';

  readonly language = signal<string>(this.getStoredLanguageFallback());
  readonly theme = signal<string>(this.getStoredThemeFallback());
  readonly user = signal<LoggedUserModel | null>(this.getStoredUser());

  constructor(
    private transloco: TranslocoService,
    private auth: AuthService,
    private router: Router,
  ) {
    effect(() => {
      const lang = this.language();
      this.transloco.setActiveLang(lang);
      localStorage.setItem(this.APP_LANG_KEY, lang);
    });

    effect(() => {
      const theme = this.theme();
      document.documentElement.setAttribute('data-theme', theme);
      localStorage.setItem(this.APP_THEME_KEY, theme);
    });

    effect(() => {
      const user = this.user();
      if (user) {
        localStorage.setItem(this.APP_USER_KEY, JSON.stringify(user));
      } else {
        localStorage.removeItem(this.APP_USER_KEY);
      }
    });
  }

  // ===== LANGUAGE =====
  setLanguage(lang: string): void {
    this.language.set(lang);
  }

  getLanguage(): string {
    return this.language();
  }

  private getStoredLanguageFallback(): string {
    return localStorage.getItem(this.APP_LANG_KEY) || 'en';
  }

  translate(key: string) {
    return this.transloco.selectTranslate(key, {}, this.getLanguage());
  }

  // ===== THEME =====
  setTheme(theme: 'dark' | 'light' | string): void {
    this.theme.set(theme);
  }

  getTheme(): string {
    return this.theme();
  }

  private getStoredThemeFallback(): string {
    return localStorage.getItem(this.APP_THEME_KEY) || 'dark';
  }

  // ===== USER =====
  setUser(userResponse: any): void {
    const actualUser = userResponse?.user ?? userResponse;
    this.user.set(actualUser);
  }

  clearUser(): void {
    this.user.set(null);
  }

  getUser(): LoggedUserModel | null {
    return this.user();
  }

  getUserId(): number | null {
    return this.user()?.id ?? null;
  }

  private getStoredUser(): LoggedUserModel | null {
    const data = localStorage.getItem(this.APP_USER_KEY);
    return data ? JSON.parse(data) : null;
  }

  // ===== SESSION CHECK =====
  restoreSession() {
    return this.auth.isAuthenticated().pipe(
      tap((response: any) => {
        if (response) {
          console.log('Logged user is:', response);
          this.setUser(response);
        }
      }),
      map((response) => response != null),
      catchError(() => of(false)),
    );
  }

  checkSession(): void {
    this.auth
      .isAuthenticated()
      .pipe(
        catchError((error) => {
          if (error.status === 401) {
            this.clearSession();
          }
          return of(false);
        }),
      )
      .subscribe();
  }

  // ===== CLEAR ALL =====
  clearSession(): void {
    this.language.set('en');
    this.theme.set('dark');
    this.user.set(null);
  }
}
