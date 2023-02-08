import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {map, Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Request} from '../api/common';
import {FinanceChartRecord} from '../finance/model/IFinanceChartRecord';
import {FinanceTableRecord} from '../finance/model/IFinanceTableRecord';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private restful: HttpClient) {}

  call<Response>(request: Request): Observable<Response> {
    const url = `${environment.backend_url}${request.endpoint}`;

    return this.restful.post(url, request.body).pipe(map(res => res as Response));
  }

  signOutFromBackend(): Observable<any> {
    return this.restful.post(`${environment.backend_url}/auth/logout`, null);
  }

  healthCheck(): Observable<any> {
    return this.restful.get(`${environment.backend_url}/health`);
  }

  createFinanceRecords(body: any): Observable<any> {
    return this.restful.post(`${environment.backend_url}/finance`, body);
  }

  getFinanceTableData(societyName: string, fromDate: string, toDate: string): Observable<FinanceTableRecord[]> {
    let queryParams = new HttpParams()
      .append('societyName', societyName)
      .append('fromDate', fromDate)
      .append('toDate', toDate);

    return this.restful.get<FinanceTableRecord[]>(`${environment.backend_url}/finance/table`, {params: queryParams});
  }

  getFinancePieChartData(societyName: string, fromDate: string, toDate: string): Observable<FinanceChartRecord[]> {
    let queryParams = new HttpParams()
      .append('societyName', societyName)
      .append('fromDate', fromDate)
      .append('toDate', toDate);

    return this.restful.get<FinanceChartRecord[]>(`${environment.backend_url}/finance/pieChart`, {params: queryParams});
  }

  getFinanceBarChartData(societyName: string, fromDate: string, toDate: string): Observable<FinanceChartRecord[]> {
    let queryParams = new HttpParams()
      .append('societyName', societyName)
      .append('fromDate', fromDate)
      .append('toDate', toDate);

    return this.restful.get<FinanceChartRecord[]>(`${environment.backend_url}/finance/barChart`, {params: queryParams});
  }

  deleteFinanceData(societyName: string, financeRecordIdList: string[]): Observable<FinanceTableRecord[]> {
    let queryParams = new HttpParams().append('societyName', societyName);

    financeRecordIdList.forEach(financeRecordId => {
      queryParams = queryParams.append('id', financeRecordId);
    });

    return this.restful.delete<FinanceTableRecord[]>(`${environment.backend_url}/finance`, {params: queryParams});
  }
}
