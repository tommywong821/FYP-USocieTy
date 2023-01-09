export type RequestBody = Record<string, any>;
export interface Request {
  endpoint: string;
  body: RequestBody;
}
