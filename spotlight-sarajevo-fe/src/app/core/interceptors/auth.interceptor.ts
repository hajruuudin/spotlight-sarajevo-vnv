import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthInterceptor implements HttpInterceptor {
  private readonly protectedRoutes = [
    '/review/spot/create',
    '/review/spot/update',
    '/review/spot/delete',
    '/admin/'
  ];

  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const isProtectedRoute = this.protectedRoutes.some((route) => req.url.includes(route));

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (isProtectedRoute) {
          if (error.status === 401) {
            this.router.navigate(['/auth/login']);
          } else if (error.status === 403) {
            this.router.navigate(['/home']);
          }
        }
        return throwError(() => error);
      })
    );
  }
}
