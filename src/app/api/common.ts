import {HttpParams} from '@angular/common/http';

export type RequestBody = Record<string, any>;
export interface Request {
  endpoint: string;
  urlParams?: Record<string, string>;
  queryParam?: HttpParams;
  body?: RequestBody;
}
