import { Issue } from './../../shared/model/issue';
import { Injectable } from '@angular/core';

@Injectable()
export class MessageService {

  showIssues(issues: Issue[]) {
    console.log(issues);
  }

  showError(error: any) {
    console.log(error);
  }
}
