import { Injectable, inject } from "@angular/core";
/**
 * This service can be used to manipulate data in the browser's localStorage.
 */
@Injectable({
    providedIn: 'root'
})
export class DataStorageService {

    constructor() { }

    /**
     * This method, given a url and a key, returns the storage key.
     * 
     * @param url reference url
     * @param key variable key
     * @returns storage key
     */
    private getKeyStorage(url: string, key: string) {
        return key + ":" + url;
    }

    /**
     * This method retrieves the string saved on the localStore given a url and a key.
     * 
     * @param url reference url
     * @param key variable key
     * @returns matching string
     */
    getData(url: string, key: string) {
        let keyStorage: string = this.getKeyStorage(url, key);
        return localStorage.getItem(keyStorage);
    }

    /**
     * This method retrieves the string saved on the sessionStore given a url and a key.
     * 
     * @param url reference url
     * @param key variable key
     * @returns matching string
     */
    getSessionData(url: string, key: string) {
        let keyStorage: string = this.getKeyStorage(url, key);
        return sessionStorage.getItem(keyStorage);
    }



    /**
     * This method modifies or inserts (if it doesn't exist) 
     * a variable into the localStore.
     * 
     * @param url reference url
     * @param key variable key
     * @param value string to store
     */
    setData(url: string, key: string, value: string) {
        let keyStorage: string = this.getKeyStorage(url, key);
        localStorage.setItem(keyStorage, value);
    }

    /**
     * This method modifies or inserts (if it doesn't exist) 
     * a variable into the sessionStorage.
     * 
     * @param url reference url
     * @param key variable key
     * @param value string to store
     */
    setSessionData(url: string, key: string, value: string) {
        let keyStorage: string = this.getKeyStorage(url, key);
        sessionStorage.setItem(keyStorage, value);
    }

    /**
     * This method given a url and a key removes 
     * the corresponding string in the localStorage.
     * 
     * @param url reference url
     * @param key variable key
     */
    removeData(url: string, key: string) {
        let keyStorage: string = this.getKeyStorage(url, key);
        localStorage.removeItem(keyStorage);
    }

    /**
    * This method given a url and a key removes 
    * the corresponding string in the sessionStorage.
    * 
    * @param url reference url
    * @param key variable key
    */
    removeSessionData(url: string, key: string) {
        let keyStorage: string = this.getKeyStorage(url, key);
        sessionStorage.removeItem(keyStorage);
    }

}