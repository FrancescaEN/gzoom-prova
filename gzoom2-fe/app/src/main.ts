import { enableProdMode } from "@angular/core";
import { platformBrowserDynamic } from "@angular/platform-browser-dynamic";

import { AppModule } from "./app/app.module";
import { environment } from "./environments/environment";

console.log("BOOTSTRAP APP", new Date().toISOString());
if (environment.production) {
  enableProdMode();
} else {
  console.log("Mode DEV");
}

platformBrowserDynamic().bootstrapModule(AppModule);
