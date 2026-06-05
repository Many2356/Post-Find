import { Injectable }          from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable }          from 'rxjs';
import { JobOffer, JobOfferRequest } from '../models/job-offer';

@Injectable({ providedIn: 'root' })
export class JobOfferService {
  private api = 'https://devjobs-l0bz.onrender.com/api/offers';

  constructor(private http: HttpClient) {}

  getAll(search?: string): Observable<JobOffer[]> {
    let params = new HttpParams();
    if (search) params = params.set('search', search);
    return this.http.get<JobOffer[]>(this.api, { params });
  }

  getById(id: number): Observable<JobOffer> {
    return this.http.get<JobOffer>(`${this.api}/${id}`);
  }

  getByEmployer(employerId: number): Observable<JobOffer[]> {
    return this.http.get<JobOffer[]>(`${this.api}/employer/${employerId}`);
  }

  create(employerId: number, data: JobOfferRequest): Observable<JobOffer> {
    return this.http.post<JobOffer>(`${this.api}?employerId=${employerId}`, data);
  }

  update(id: number, employerId: number, data: JobOfferRequest): Observable<JobOffer> {
    return this.http.put<JobOffer>(`${this.api}/${id}?employerId=${employerId}`, data);
  }

  delete(id: number, employerId: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}?employerId=${employerId}`);
  }
}