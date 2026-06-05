export interface Application {
  id: number;
  jobOfferId: number;
  jobOfferTitle?: string;
  jobOfferCompany?: string;
  applicantId: number;
  applicantUsername?: string;
  status: string;
  coverLetter?: string;
  appliedAt?: string;
}

export interface ApplicationRequest {
  coverLetter?: string;
}