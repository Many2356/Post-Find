export interface JobOffer {
  id: number;
  title: string;
  description?: string;
  company: string;
  location?: string;
  salary?: string;
  contractType?: string;
  experienceLevel?: string;
  technologies?: string;
  remote?: string;
  active: boolean;
  employerId: number;
  employerUsername?: string;
  createdAt?: string;
  applicantsCount?: number;
}

export interface JobOfferRequest {
  title: string;
  description?: string;
  company: string;
  location?: string;
  salary?: string;
  contractType?: string;
  experienceLevel?: string;
  technologies?: string;
  remote?: string;
}