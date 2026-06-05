import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Application, ApplicationRequest } from '../models/application';

@Injectable({ providedIn: 'root' })
export class ApplicationService {
  private api = 'http://localhost:8080/api/applications';

  constructor(private http: HttpClient) {}

  apply(jobOfferId: number, applicantId: number, data?: ApplicationRequest): Observable<Application> {
    return this.http.post<Application>(
      `${this.api}/apply/${jobOfferId}?applicantId=${applicantId}`,
      data || {}
    );
  }

  getByUser(userId: number): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.api}/user/${userId}`);
  }

  getByOffer(jobOfferId: number): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.api}/offer/${jobOfferId}`);
  }

  hasApplied(applicantId: number, jobOfferId: number): Observable<{ applied: boolean }> {
    return this.http.get<{ applied: boolean }>(
      `${this.api}/check?applicantId=${applicantId}&jobOfferId=${jobOfferId}`
    );
  }

  withdraw(applicationId: number, applicantId: number): Observable<any> {
    return this.http.delete(`${this.api}/${applicationId}?applicantId=${applicantId}`);
  }
}