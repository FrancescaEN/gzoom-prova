import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable()
export class ThemeService {

  private themeChangeSubject : BehaviorSubject<string> = new BehaviorSubject(null);
  themeChange$ = this.themeChangeSubject.asObservable();

  constructor(private router: Router){}

  /**
   * Cambia il tema attivo dell'applicazione.
   * @param theme Il nome del tema da attivare.
   */
  switchStyle(theme: string): void {
    if (theme && this.findStyle(theme)) {
      const links: HTMLLinkElement[] = Array.from(document.getElementsByTagName('link'));
      links.forEach(link => {
        if (link.rel.includes('stylesheet') && link.title) {
          link.disabled = link.title !== theme;
        }
      });
      if(this.router.url.includes('legacy')){
        this.themeChangeSubject.next(theme);
      }
    }
  }

  /**
   * Verifica se un tema esiste tra i fogli di stile disponibili.
   * @param theme Il nome del tema da cercare.
   * @returns True se il tema è presente, altrimenti False.
   */
  private findStyle(theme: string): boolean {
    const links: HTMLLinkElement[] = Array.from(document.getElementsByTagName('link'));
    return links.some(link => link.rel.includes('stylesheet') && link.title === theme);
  }
}
