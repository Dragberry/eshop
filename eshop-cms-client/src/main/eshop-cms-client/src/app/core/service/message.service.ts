import { Issue } from './../../shared/model/issue';
import { Injectable } from '@angular/core';
import { MessageType, Message } from 'src/app/shared/model/message';
import { Observable, Subscriber } from 'rxjs';

@Injectable()
export class MessageService {

  messages = new Map<MessageType, Message[]>();

  observer: Subscriber<Map<MessageType, Message[]>>;
  source: Observable<Map<MessageType, Message[]>>;

  constructor() {
    this.source = Observable.create((observer: Subscriber<Map<MessageType, Message[]>>) => {
      this.observer = observer;
    });
  }

  subscribe(subscriber: (messages) => void): void {
    this.source.subscribe(subscriber);
  }

  showIssues(issues: Issue[]) {
    this.clearAll();
    issues.forEach(issue => {
      this.addMessage(issue.type, issue.errorCode);
    });
    if (this.observer) {
      this.observer.next(this.messages);
    }
  }

  addMessage(type: MessageType, msg: string) {
    let group = this.messages.get(type);
    if (!group) {
      group = [];
      this.messages.set(type, group);
    }
    group.push({text: msg, type: type});
  }

  showError(error: any) {
    this.clearAll();
    this.addMessage(MessageType.ERROR, `error.http.${error.status}`);
  }

  clearAll(): void {
    this.messages.forEach((group, type) => {
      this.messages.set(type, []);
    });
  }
}
