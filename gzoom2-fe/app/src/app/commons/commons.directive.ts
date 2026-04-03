import { Directive, Input, ElementRef, AfterContentInit } from '@angular/core';

import * as $ from 'jquery';
import * as _ from 'lodash';
import * as moment from 'moment';
import { VersionConfigService } from './service/config.service';



/**
 * Prints a range of years.
 */
@Directive({ selector: '[appFromYear]' })
export class FromYearDirective implements AfterContentInit {
  @Input() year: number;

  constructor(private readonly el: ElementRef) { }

  ngAfterContentInit() {
    if (this.el.nativeElement) {
      const now = moment().year();
      const text = this.year !== now ? this.year + ' - ' + now : now;
      $(this.el.nativeElement).text(text);
    }
  }
}

/**
 * Prints the be version as text element.
 */
@Directive({ selector: '[appMajorMinorVersion]' })
export class ApplicationMajorMinorVersionDirective implements AfterContentInit {
  private readonly version: string;

  constructor(private readonly el: ElementRef, private readonly conf: VersionConfigService) {

    this.version = conf.getConfig()["version"] ?? conf.getConfig()["product_version"];
  }

  ngAfterContentInit() {
    if (this.el.nativeElement && this.version) {
      $(this.el.nativeElement).text(this.version);
    }
  }
}
