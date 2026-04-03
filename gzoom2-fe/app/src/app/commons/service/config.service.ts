import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationVersion } from '../model/config';

@Injectable({
  providedIn: 'root'  // Globally available service
})
export class VersionConfigService {
  private config: ApplicationVersion[] | null = null;

  constructor(private http: HttpClient) { }

  // Method to load the config from the JSON file
  loadConfig(): Observable<ApplicationVersion[]> {
    return this.http.get<ApplicationVersion[]>('assets/version.json');
  }

  // Method to get the cached config, or load if not available
  getConfig(): ApplicationVersion[] | null {
    return this.config;
  }

  setConfig(config: ApplicationVersion[]): void {
    this.config = config;
  }
}
