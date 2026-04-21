export interface AuthResponse {
  token: string;
  type: string;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
}

export interface UserProfile {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
}
