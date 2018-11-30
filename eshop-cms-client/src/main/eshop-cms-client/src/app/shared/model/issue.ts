import { MessageType } from './message-type';

export class Issue {
  errorCode: string;
  params: any[];
  type: MessageType;
  fieldId?: string;
}
