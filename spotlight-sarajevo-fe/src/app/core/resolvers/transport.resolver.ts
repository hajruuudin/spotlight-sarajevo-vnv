import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { catchError, forkJoin, map, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { PublicTransportService } from '../../services/transport.service';
import { TransportMethodShorthandModel, TransportMethodModel, TransportMethodLineModel } from '../../shared/models/transport.model';
import { TransportType, TRANSPORT_OPERATORS } from '../../shared/constants/TransportOperators';

export interface TransportPageData {
  transportMethods: TransportMethodShorthandModel[];
  initialMethod: TransportMethodModel | null;
  initialLines: TransportMethodLineModel[];
}

export const transportResolver: ResolveFn<TransportPageData> = (route, state) => {
  const transportService = inject(PublicTransportService);

  const lineRequests = TRANSPORT_OPERATORS.map(operator =>
    transportService.findLinesByOperatorAndTransportType(operator.id, TransportType.TRAMCAR).pipe(
      catchError(() => of([]))
    )
  );

  return forkJoin({
    transportMethods: transportService.findAllTransportMethods().pipe(
      catchError(() => of([]))
    ),
    initialMethod: transportService.findMethodById(TransportType.TRAMCAR).pipe(
      catchError(() => of(null))
    ),
    lines: forkJoin(lineRequests).pipe(
      map(results => results.flat()),
      catchError(() => of([]))
    ),
  }).pipe(
    map((response) => ({
      transportMethods: response.transportMethods,
      initialMethod: response.initialMethod,
      initialLines: response.lines,
    })),
    catchError((error: HttpErrorResponse) => {
      console.error('Error in transport resolver:', error);
      return of({
        transportMethods: [],
        initialMethod: null,
        initialLines: [],
      });
    })
  );
};
