export interface CasResponse {
  authenticationSucess: {
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
