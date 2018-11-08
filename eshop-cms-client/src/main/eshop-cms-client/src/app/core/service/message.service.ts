import { Issue } from './../../shared/model/issue';
import { Injectable } from '@angular/core';
import { MessageType, Message } from 'src/app/shared/model/message';
import { Observable, Subscriber } from 'rxjs';

@Injectable()
export class MessageService {

  messages = new Map<MessageType, Message[]>();

  subscriber: Subscriber<Map<MessageType, Message[]>>;
  source: Observable<Map<MessageType, Message[]>>;

  constructor() {
    this.source = new Observable((subscriber: Subscriber<Map<MessageType, Message[]>>) => {
      this.subscriber = subscriber;
      this.subscriber.next(this.messages);
    });
  }

  subscribe(subscriber: (messages) => void): void {
    this.source.subscribe(subscriber);
  }

  showIssues(issues: Issue[]) {
    issues.forEach(issue => {
      let group = this.messages.get(issue.type);
      if (!group) {
        group = [];
        this.messages.set(issue.type, group);
      }
      group.push({text: issue.errorCode, type: issue.type});
    });
    this.subscriber.next(this.messages);
  }

  showError(error: any) {
    console.log(error);
  }

  clearAll(): void {
    this.messages.forEach((group, type) => {
      this.messages.set(type, []);
    });
  }
}
