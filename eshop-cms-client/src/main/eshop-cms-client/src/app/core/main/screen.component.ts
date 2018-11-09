import { Observable } from 'rxjs';

export interface ScreenComponent {
    getTitle(): Observable<string>;
}
