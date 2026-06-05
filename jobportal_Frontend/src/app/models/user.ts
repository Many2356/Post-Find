export interface User {
  id: number;
  username: string;
  email: string;
  role: 'TRABAJADOR' | 'EMPRESARIO' | 'ADMIN';
  fullName?: string;
  bio?: string;
  location?: string;
  website?: string;
  skills?: string;
  company?: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  role: string;
  fullName?: string;
  company?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface UpdateProfileRequest {
  fullName?: string;
  role?: string;
  bio?: string;
  location?: string;
  website?: string;
  skills?: string;
  company?: string;
  email?: string;
}