import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection,isDevMode,inject } from '@angular/core';
import { provideRouter, withInMemoryScrolling } from '@angular/router';
import { routes } from './app.routes';
import { provideHotToastConfig } from '@ngxpert/hot-toast';
import { HttpHandlerFn, provideHttpClient, withInterceptors } from '@angular/common/http';
import { TranslocoHttpLoader } from './transloco-loader';
import { provideTransloco } from '@ngneat/transloco';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';

/** 
 * As of Jan 21, the current providers are:
 * provideHttpClient() - For making HTTP requests with a custom interceptor
 * provideBrowserGlobalErrorListeners() - For global error handling in the browser
 * provideZonelessChangeDetection() - For optimizing change detection
 * provideTransloco() - For internationalization and localization dynamically without reloads
 * provideRouter() - For setting up application routing
 * provideHotToastConfig() - For configuring toast notifications with dynamic theme changing
 * provideCharts() - For charting capabilities with default chart types
*/ 
export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([
        (req, next: HttpHandlerFn) =>
          inject(AuthInterceptor).intercept(req, {
            handle: (internalReq) => next(internalReq),
          }),
      ]),
    ),
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(
      routes,
      withInMemoryScrolling({
        scrollPositionRestoration: 'top',
      }),
    ),
    provideHotToastConfig({
      duration: 4000,
      position: 'top-right',
      dismissible: true,
      style: {
        background: 'var(--toast-bg)',
        color: 'var(--toast-text)',
        border: '1px solid var(--toast-border)',
        borderRadius: '12px',
        padding: '6px 16px',
        fontSize: '16px',
        fontWeight: '500',
        textAlign: 'start',
        alignItems: 'center',
      },
      stacking: 'depth',
    }),
    provideTransloco({
      config: {
        availableLangs: ['en', 'ba'],
        defaultLang: 'en',
        reRenderOnLangChange: true,
        prodMode: !isDevMode(),
      },
      loader: TranslocoHttpLoader,
    }),
    provideCharts(withDefaultRegisterables()),
  ],
};
