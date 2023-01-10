import {HttpParams} from '@angular/common/http';

export type RequestBody = Record<string, any>;
export interface Request {
  endpoint: string;
  queryParam: HttpParams | null | undefined;
  body: RequestBody | null | undefined;
}
