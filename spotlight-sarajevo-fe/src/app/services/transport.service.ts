import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import {
  TaxiOperatorModel,
  TransportMethodLineModel,
  TransportMethodModel,
  TransportMethodShorthandModel,
} from '../shared/models/transport.model';

/**
 * Service for interacting with the Public Transport API.
 * Provides methods to fetch transport methods, lines, and taxi operators.
 * All requests include credentials for authentication.
 * 
 * Methods and entities incorporated in the service: TransportMethodShorthandModel, TransportMethodModel, TransportMethodLineModel, TaxiOperatorModel
 * 
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Injectable({
  providedIn: 'root',
})
export class PublicTransportService {
  private API_URL = environment.API_URL;

  constructor(private http: HttpClient) {}

  /**
   * Fetches all transport methods in shorthand format.
   * @returns An observable of an array of TransportMethodShorthandModel.
   */
  findAllTransportMethods() {
    return this.http.get<TransportMethodShorthandModel[]>(this.API_URL + `/transport/all`, {
      withCredentials: true,
    });
  }

  /**
   * Fetches detailed information about a specific transport method by its ID.
   * @param id - The ID of the transport method to fetch.
   * @returns An observable of TransportMethodModel.
   */
  findMethodById(id: number) {
    return this.http.get<TransportMethodModel>(this.API_URL + `/transport/${id}`, {
      withCredentials: true,
    });
  }

  /**
   * Fetches transport lines for a specific operator and transport type.
   * @param operator - The ID of the transport operator.
   * @param transportType - The type of transport (e.g., tramcar, trolley).
   * @returns An observable of an array of TransportMethodLineModel.
   */
  findLinesByOperatorAndTransportType(operator: number, transportType: number) {
    return this.http.get<TransportMethodLineModel[]>(
      this.API_URL + `/transport/lines?operatorId=${operator}&transportTypeId=${transportType}`,
      {
        withCredentials: true,
      },
    );
  }

  /**
   * Fetches all taxi operators.
   * @returns An observable of an array of TaxiOperatorModel.
   */
  findTaxiOperators() {
    return this.http.get<TaxiOperatorModel[]>(this.API_URL + `/transport/taxis`, {
      withCredentials: true,
    });
  }
}
