import {Params} from '@angular/router';

export const validateUserEndpoint = '/auth/serviceValidate';
export interface validateUserRequestBody {
  params: Params;
}
export interface validateUserResponse {
  authenticationSuccess: {
    user: string;
    attributes: {
      mail: string;
      name: string;
    };
  };
  authenticationFailure: {
    code: string | null;
    value: any;
  };
}
