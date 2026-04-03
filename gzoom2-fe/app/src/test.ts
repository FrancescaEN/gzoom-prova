import 'zone.js';
import 'zone.js/testing';
import { getTestBed } from '@angular/core/testing';
import {
  BrowserDynamicTestingModule,
  platformBrowserDynamicTesting
} from '@angular/platform-browser-dynamic/testing';

// Unfortunately there's no typing for the `__karma__` variable. Just declare it as any.
declare var __karma__: any;

// Prevent Karma from running prematurely.
__karma__.loaded = function () {};

// First, initialize the Angular testing environment.
getTestBed().initTestEnvironment(
  BrowserDynamicTestingModule,
  platformBrowserDynamicTesting(), {
    teardown: { destroyAfterEach: false }
  }
);

// Find all the tests
const allTestFiles: string[] = Object.keys(__karma__.files).filter(file => file.endsWith('.spec.ts'));

// Import each test file dynamically
Promise.all(
  allTestFiles.map(moduleName => {
    return import(moduleName);
  })
).then(() => {
  // Start Karma once all imports are done.
  __karma__.start();
});
