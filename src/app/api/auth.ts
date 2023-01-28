import {Params} from '@angular/router';

export const validateUserEndpoint = '/auth/mockServiceValidate';
export interface validateUserRequestBody {
  params: Params;
}
