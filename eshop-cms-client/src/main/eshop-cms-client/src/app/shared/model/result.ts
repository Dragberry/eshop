import { Issue } from './issue';

export class Result<T> {
  value: T;
  issues: Issue[];
}
