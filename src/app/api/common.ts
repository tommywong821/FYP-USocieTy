import {HttpParams} from '@angular/common/http';

export type RequestBody = Record<string, any>;
export interface Request {
  endpoint: string;
  queryParam: HttpParams;
  body: RequestBody | null | undefined;
}
