import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class FaviconService {
    constructor(private http: HttpClient) { }

    /**
     * Imposta la favicon dal server tramite HttpClient.
     * Angular aggiungerà il token automaticamente tramite interceptor.
     */
    setFaviconFromServer(url: string) {
        this.http.get(url, { responseType: 'blob' }).subscribe({
            next: (blob) => {
                const blobUrl = URL.createObjectURL(blob);

                let link: HTMLLinkElement | null = document.querySelector("link[rel*='icon']");
                if (!link) {
                    link = document.createElement('link');
                    link.rel = 'icon';
                    document.head.appendChild(link);
                }
                link.setAttribute('type', 'image/png');
                link.setAttribute('sizes', '16x16');
                link.href = blobUrl;
            },
            error: (err) => {
                console.error('Errore caricando favicon:', err);
            }
        });
    }


    setLogoFromServer(imgElement: HTMLImageElement, url: string) {
        this.http.get(url, { responseType: 'blob' }).subscribe({
            next: (blob) => {
                const blobUrl = URL.createObjectURL(blob);
                imgElement.src = blobUrl;
            },
            error: (err) => {
                console.error('Errore caricando logo:', err);
                
            }
        });
    }
}
