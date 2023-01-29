import {Params} from '@angular/router';

export const validateUserEndpoint = '/auth/serviceValidate';
export interface validateUserRequestBody {
  params: Params;
}
