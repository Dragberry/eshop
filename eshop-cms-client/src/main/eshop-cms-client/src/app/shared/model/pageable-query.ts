import { HttpParams } from '@angular/common/http';

export class PageableQuery {
    pageNumber: number;
    pageSize: number;
}

export function PageableHttpParams(pageableQuery: PageableQuery, params?: HttpParams): HttpParams {
    if (!params) {
        params = new HttpParams();
    }
    return params
        .set('pageNumber', pageableQuery.pageNumber.toString())
        .set('pageSize', pageableQuery.pageSize.toString());
}
