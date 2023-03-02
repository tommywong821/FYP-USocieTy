import {UpdateEventRequest, DeleteEventRequest} from './../api/event';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {NzTableFilterList} from 'ng-zorro-antd/table';
import {map, Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Request} from '../api/common';
import {FinanceChartRecord} from '../finance/model/IFinanceChartRecord';
import {FinanceRecordTotalNumber} from '../finance/model/IFinanceRecordTotalNumber';
import {FinanceTableRecord} from '../finance/model/IFinanceTableRecord';
import {Event} from '../model/event';

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

  getFinanceTableData(
    societyName: string,
    fromDate: string,
    toDate: string,
    pageIndex?: number,
    pageSize?: number,
    sortField?: string,
    sortOrder?: string,
    filterKey?: string,
    filterValue?: string[]
  ): Observable<FinanceTableRecord[]> {
    let queryParams = new HttpParams()
      .append('societyName', societyName)
      .append('fromDate', fromDate)
      .append('toDate', toDate);

    if (pageIndex && pageSize) {
      queryParams = queryParams.append('pageIndex', pageIndex).append('pageSize', pageSize);
    }

    if (sortField && sortOrder) {
      queryParams = queryParams.append('sortField', sortField).append('isAscend', sortOrder === 'ascend');
    }

    if (filterKey && filterValue) {
      filterValue.forEach(value => {
        queryParams = queryParams.append(filterKey, value);
      });
    }

    return this.restful.get<FinanceTableRecord[]>(`${environment.backend_url}/finance/table`, {params: queryParams});
  }

  getFinancePieChartData(societyName: string, fromDate: string, toDate: string): Observable<FinanceChartRecord[]> {
    const queryParams = new HttpParams()
      .append('societyName', societyName)
      .append('fromDate', fromDate)
      .append('toDate', toDate);

    return this.restful.get<FinanceChartRecord[]>(`${environment.backend_url}/finance/pieChart`, {params: queryParams});
  }

  getFinanceBarChartData(societyName: string, fromDate: string, toDate: string): Observable<FinanceChartRecord[]> {
    const queryParams = new HttpParams()
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

  getTotalNumberOfFinanceTableData(
    societyName: string,
    fromDate: string,
    toDate: string,
    filterKey?: string,
    filterValue?: string[]
  ): Observable<FinanceRecordTotalNumber> {
    let queryParams = new HttpParams()
      .append('societyName', societyName)
      .append('fromDate', fromDate)
      .append('toDate', toDate);

    if (filterKey && filterValue) {
      filterValue.forEach(value => {
        queryParams = queryParams.append(filterKey, value);
      });
    }

    return this.restful.get<FinanceRecordTotalNumber>(`${environment.backend_url}/finance/totalNumber`, {
      params: queryParams,
    });
  }

  getAllCategoryOfFinanceRecord(societyName: string, fromDate: string, toDate: string): Observable<NzTableFilterList> {
    const queryParams = new HttpParams()
      .append('societyName', societyName)
      .append('fromDate', fromDate)
      .append('toDate', toDate);

    return this.restful.get<NzTableFilterList>(`${environment.backend_url}/finance/category`, {
      params: queryParams,
    });
  }

  getEvent(eventId: string): Observable<Event> {
    return this.restful.get<Event>(`${environment.backend_url}/event/${eventId}`);
  }

  getEvents(pageIndex: number, pageSize: number): Observable<Event[]> {
    const queryParams = new HttpParams().append('pageIndex', pageIndex).append('pageSize', pageSize);

    return this.restful.get<Event[]>(`${environment.backend_url}/event`, {params: queryParams});
  }

  updateEvent(updateEventRequest: UpdateEventRequest): void {
    this.restful.put(`${environment.backend_url}/event/${updateEventRequest.urlParams['id']}`, updateEventRequest.body);
  }

  deleteEvent(eventId: string): void {
    this.restful.delete(`${environment.backend_url}/event/${eventId}`);
  }

  updateEnrollmentStatus(eventId: string, studentId: string, status: string): void {
    const body = {
      eventId,
      studentId,
      status,
    };

    this.restful.put(`${environment.backend_url}/enrolledEventRecord`, body);
  }
}
