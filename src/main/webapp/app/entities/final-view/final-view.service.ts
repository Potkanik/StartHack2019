import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFinalView } from 'app/shared/model/final-view.model';

type EntityResponseType = HttpResponse<IFinalView>;
type EntityArrayResponseType = HttpResponse<IFinalView[]>;

@Injectable({ providedIn: 'root' })
export class FinalViewService {
    public resourceUrl = SERVER_API_URL + 'api/users/final-view';

    constructor(private http: HttpClient) {}

    create(finalView: IFinalView): Observable<EntityResponseType> {
        return this.http.post<IFinalView>(this.resourceUrl, finalView, { observe: 'response' });
    }

    update(finalView: IFinalView): Observable<EntityResponseType> {
        return this.http.put<IFinalView>(this.resourceUrl, finalView, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFinalView>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFinalView[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
