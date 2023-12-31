import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {NzTableFilterList} from 'ng-zorro-antd/table';
import {map, Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Request} from '../api/common';
import {FinanceChartRecord} from '../finance/model/IFinanceChartRecord';
import {FinanceRecordTotalNumber} from '../finance/model/IFinanceRecordTotalNumber';
import {FinanceTableRecord} from '../finance/model/IFinanceTableRecord';
import {
  Event,
  EventAttendance,
  EventCount,
  EventEnrollmentRecord,
  UpdateEventEnrollmentRecordPayload,
} from '../model/event';

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

  getEvents(studentId: string, pageIndex: number, pageSize: number): Observable<Event[]> {
    const queryParams = new HttpParams().append('pageNum', pageIndex).append('pageSize', pageSize);

    return this.restful.get<Event[]>(`${environment.backend_url}/student/${studentId}/event`, {params: queryParams});
  }

  createEvent(eventDto: Event, poster: File, societyName: string) {
    const formData: FormData = new FormData();

    const eventJson: Blob = new Blob([JSON.stringify(eventDto)], {type: 'application/json'});
    formData.append('event', eventJson);
    formData.append('poster', poster);
    formData.append('society', societyName);

    return this.restful.post(`${environment.backend_url}/event`, formData, {
      reportProgress: true,
      responseType: 'text',
    });
  }

  updateEvent(eventDto: Event, societyName: string, poster?: File): Observable<void> {
    console.log(eventDto);

    const formData: FormData = new FormData();

    const eventJson: Blob = new Blob([JSON.stringify(eventDto)], {type: 'application/json'});
    formData.append('event', eventJson);
    formData.append('society', societyName);
    if (poster) {
      formData.append('poster', poster);
    }

    return this.restful.put<void>(`${environment.backend_url}/event/${eventDto.id}`, formData);
  }

  deleteEvent(eventId: string): Observable<void> {
    return this.restful.delete<void>(`${environment.backend_url}/event/${eventId}`);
  }

  getEventEnrollmentRecord(eventId: string, pageIndex: number, pageSize: number): Observable<EventEnrollmentRecord[]> {
    const queryParams = new HttpParams().append('pageNum', pageIndex).append('pageSize', pageSize);

    return this.restful.get<EventEnrollmentRecord[]>(`${environment.backend_url}/enrolledEventRecord/${eventId}`, {
      params: queryParams,
    });
  }

  getEventCount(studentId: string): Observable<EventCount> {
    return this.restful.get<EventCount>(`${environment.backend_url}/student/${studentId}/event/totalNumber`);
  }

  getEventEnrollmentRecordCount(eventId: string): Observable<number> {
    return this.restful.get<number>(`${environment.backend_url}/enrolledEventRecord/${eventId}/totalNumber`);
  }

  updateEventEnrollmentRecords(eventId: string, records: UpdateEventEnrollmentRecordPayload[]): Observable<void> {
    return this.restful.put<void>(`${environment.backend_url}/enrolledEventRecord`, records);
  }

  getEventAttendanceRecords(eventId: string, pageIndex: number, pageSize: number): Observable<EventAttendance> {
    const queryParams = new HttpParams().append('pageNum', pageIndex).append('pageSize', pageSize);

    return this.restful.get<EventAttendance>(`${environment.backend_url}/event/${eventId}/attendance`, {
      params: queryParams,
    });
  }

  getEventAttendanceRecordCount(eventId: string): Observable<number> {
    return this.restful.get<number>(`${environment.backend_url}/event/${eventId}/attendance/totalNumber`);
  }

  //Society
  getSocietywithMemberRole(pageNum = 0, pageSize = 100): Observable<any> {
    return this.restful.get<any>(
      `${environment.backend_url}/society/withMemberRole?pageNum=${pageNum}&pageSize=${pageSize}`
    );
  }

  getAllSocietyMember(societyName: string | null): Observable<any> {
    return this.restful.get<any>(`${environment.backend_url}/society/member?societyName=${societyName}`);
  }
  getenrolledSocietyRecord(societyName: string | null): Observable<any> {
    return this.restful.get<any>(`${environment.backend_url}/enrolledSocietyRecord?societyName=${societyName}`);
  }

  deleteSocietyMember(societyName: string | null, studentId: string[] | null): Observable<void> {
    return this.restful.delete<void>(
      `${environment.backend_url}/society/member?societyName=${societyName}&studentId=${studentId}`
    );
  }

  removeAdministrativeRights(societyName: string | null, studentId: string[] | null): Observable<void> {
    return this.restful.delete<void>(
      `${environment.backend_url}/society/member?societyName=${societyName}&studentId=${studentId}`
    );
  }

  removeStudentFromSociety(societyName: string | null, studentId: string[] | null): Observable<void> {
    return this.restful.delete<void>(
      `${environment.backend_url}/society/remove?societyName=${societyName}&studentId=${studentId}`
    );
  }

  assignAdministrativeRights(societyName: string | null, studentIdList: string[] | null): Observable<void> {
    const body = {
      societyName,
      studentIdList,
    };
    return this.restful.post<void>(`${environment.backend_url}/society/member`, body);
  }
  updateEnrolledSocietyRecord(body: any): Observable<void> {
    return this.restful.put<void>(`${environment.backend_url}/enrolledSocietyRecord`, body);
  }
}
