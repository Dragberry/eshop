import { MessageType } from './message';

export class Issue {
  errorCode: string;
  params: any[];
  type: MessageType;
  fieldId?: string;
}
