export enum MessageType {
  ERROR = 'ERROR',
  WARNING = 'WARNING'
}

export class Issue {
  errorCode: string;
  params: any[];
  type: MessageType;
  fieldId?: string;
}
