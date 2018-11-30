import { Injectable } from '@angular/core';
import { Issue, Message, MessageType } from 'src/app/shared/model';
import { Observable, Subscriber } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class MessageService {

  messages = new Map<MessageType, Message[]>();

  observer: Subscriber<Map<MessageType, Message[]>>;
  source: Observable<Map<MessageType, Message[]>>;

  constructor(private translate: TranslateService) {
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
      this.translate.get(issue.errorCode, issue.params).subscribe(translated => {
        this.addMessage(issue.type, translated);
      });
    });
    this.next();
  }

  showMessage(type: MessageType, msg: string, params?: Object): void {
    this.clearAll();
    this.translate.get(msg, params).subscribe(translated => {
      this.addMessage(type, translated);
      this.next();
    });
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
    this.translate.get(`errors.http.${error.status}`).subscribe(msg => {
      this.addMessage(MessageType.ERROR, msg);
      this.next();
    });
  }

  clearAll(): void {
    this.messages.forEach((group, type) => {
      this.messages.set(type, null);
    });
    this.next();
  }

  next(): void {
    if (this.observer) {
      this.observer.next(this.messages);
    }
  }
}
