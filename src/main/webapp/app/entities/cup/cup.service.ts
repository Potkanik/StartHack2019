import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICup } from 'app/shared/model/cup.model';

type EntityResponseType = HttpResponse<ICup>;
type EntityArrayResponseType = HttpResponse<ICup[]>;

@Injectable({ providedIn: 'root' })
export class CupService {
    public resourceUrl = SERVER_API_URL + 'api/cups';

    constructor(private http: HttpClient) {}

    create(cup: ICup): Observable<EntityResponseType> {
        return this.http.post<ICup>(this.resourceUrl, cup, { observe: 'response' });
    }

    update(cup: ICup): Observable<EntityResponseType> {
        return this.http.put<ICup>(this.resourceUrl, cup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
