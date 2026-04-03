import { Injectable } from "@angular/core";

import * as _ from "lodash";

import { FolderMenu, LeafMenu } from "../commons/model/dto";
/*
import { faTachometerAlt,
  faTasks,
  faChartLine,
  faCogs
} from '@fortawesome/fontawesome-free-solid';
import { library } from '@fortawesome/fontawesome';*/

/**
 * Maps of refurbished pages and their relative router states.
 */
export const REFURBISHED_PAGES = {
  "GP_MENU_00001_A.1": ["example"],
  GP_MENU_00001_1: ["example"],
  GP_MENU_00001_2: ["dashboard"],
  GP_MENU_00001_3: ["example"],

  /**
   *  PERFORMANCE STRATEGICA
   */
  /* CTX_BS ------------------------------------------*/
  //-- Administration
  GP_MENU_00611: ["CTX_BS/administration/analysis-type"],
  //-- Management
  GP_MENU_00494: ["CTX_BS/management/queryconfig/data-update/A"],
  //-- Consultation
  GP_MENU_00445: ["CTX_BS/consultation/report-print"],
  GP_MENU_00488: ["CTX_BS/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00550: ["CTX_BS/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
   *  PERFORMANCE OPERATIVA
   */
  /* CTX_OR ------------------------------------------*/
  //-- Administration
  GP_MENU_00612: ["CTX_OR/administration/analysis-type"],
  //-- Management
  GP_MENU_00495: ["CTX_OR/management/queryconfig/data-update/A"],
  GP_MENU_00562: ["CTX_OR/management/timesheet"],
  //-- Consultation  
  GP_MENU_00446: ["CTX_OR/consultation/report-print"],
  GP_MENU_00489: ["CTX_OR/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00551: ["CTX_OR/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
   *  PERFORMANCE INDIVIDUALE
   */
  /* CTX_EP ------------------------------------------*/
  //-- Administration
  GP_MENU_00613: ["CTX_EP/administration/analysis-type"],
  //-- Management
  GP_MENU_00496: ["CTX_EP/management/queryconfig/data-update/A"],
  //-- Consultation  
  GP_MENU_00447: ["CTX_EP/consultation/report-print"],
  GP_MENU_00490: ["CTX_EP/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00552: ["CTX_EP/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
   *  PERFORMANCE DIRIGENTI
   */
  /* CTX_DI ------------------------------------------*/
  //-- Administration
  GP_MENU_00614: ["CTX_DI/administration/analysis-type"],
  //-- Management
  GP_MENU_00510: ["CTX_DI/management/queryconfig/data-update/A"],
  //-- Consultation 
  GP_MENU_00500: ["CTX_DI/consultation/report-print"],
  GP_MENU_00505: ["CTX_DI/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00553: ["CTX_DI/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
   *  PERFORMANCE PARTECIPATE
   */
  /* CTX_PA ------------------------------------------*/
  //-- Administration
  GP_MENU_00615: ["CTX_PA/administration/analysis-type"],
  //-- Management
  GP_MENU_00509: ["CTX_PA/management/queryconfig/data-update/A"],
  //-- Consultation 
  GP_MENU_00499: ["CTX_PA/consultation/report-print"],
  GP_MENU_00504: ["CTX_PA/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00554: ["CTX_PA/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
     *  ANTICORRUZIONE
     */
  /* CTX_CO ------------------------------------------*/
  //-- Administration
  GP_MENU_00616: ["CTX_CO/administration/analysis-type"],
  //-- Management
  GP_MENU_00493: ["CTX_CO/management/queryconfig/data-update/A"],
  //-- Consultation
  GP_MENU_00444: ["CTX_CO/consultation/report-print"],
  GP_MENU_00487: ["CTX_CO/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00555: ["CTX_CO/consultation/analysis"],
  GP_MENU_00626: ["CTX_CO/consultation/dashboard"],
  /*--------------------------------------------------*/

  /**
   *  PROCESSI/PROCEDIMENTI
   */
  /* CTX_PR ------------------------------------------*/
  //-- Administration
  GP_MENU_00617: ["CTX_PR/administration/analysis-type"],
  //-- Management
  GP_MENU_00513: ["CTX_PR/management/queryconfig/data-update/A"],
  GP_MENU_00563: ["CTX_PR/management/timesheet"],
  //-- Consultation 
  GP_MENU_00503: ["CTX_PR/consultation/report-print"],
  GP_MENU_00508: ["CTX_PR/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00556: ["CTX_PR/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
   *  PRIVACY GDPR
   */
  /* CTX_GD ------------------------------------------*/
  //-- Administration
  GP_MENU_00618: ["CTX_GD/administration/analysis-type"],
  //-- Management - Risk
  GP_MENU_00497: ["CTX_GD/management-risk/queryconfig/data-update/A"],
  //-- Consultation 
  GP_MENU_00448: ["CTX_GD/consultation/report-print"],
  GP_MENU_00491: ["CTX_GD/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00557: ["CTX_GD/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
   *  CONTROLLO DI GESTIONE
   */
  /* CTX_CG ------------------------------------------*/
  //-- Administration
  GP_MENU_00619: ["CTX_CG/administration/analysis-type"],
  //-- Management
  GP_MENU_00512: ["CTX_CG/management/queryconfig/data-update/A"],
  //-- Consultation 
  GP_MENU_00502: ["CTX_CG/consultation/report-print"],
  GP_MENU_00507: ["CTX_CG/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00558: ["CTX_CG/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
   *  TRASPARENZA
   */
  /* CTX_TR ------------------------------------------*/
  //-- Administration
  GP_MENU_00620: ["CTX_TR/administration/analysis-type"],
  //-- Management
  GP_MENU_00498: ["CTX_TR/management/queryconfig/data-update/A"],
  GP_MENU_C0004: ["CTX_TR/management/timesheet"], // CUSTOM BOLZANO
  //-- Consultation 
  GP_MENU_00449: ["CTX_TR/consultation/report-print"],
  GP_MENU_00492: ["CTX_TR/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00559: ["CTX_TR/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
   *  PROGRAMMAZIONE INTEGRATA
   */
  /* CTX_RE ------------------------------------------*/
  //-- Administration
  GP_MENU_00621: ["CTX_RE/administration/analysis-type"],
  //-- Management
  GP_MENU_00511: ["CTX_RE/management/queryconfig/data-update/A"],
  //-- Consultation
  GP_MENU_00501: ["CTX_RE/consultation/report-print"],
  GP_MENU_00506: ["CTX_RE/consultation/queryconfig/data-extraction/E"],
  GP_MENU_00560: ["CTX_RE/consultation/analysis"],
  /*--------------------------------------------------*/

  /**
   *  INDICATORI
   */
  /* CTX_AC ------------------------------------------*/
  //-- Configuration
  GP_MENU_00332: ["CTX_AC/configuration/Uom"],
  GP_MENU_00347: ["CTX_AC/configuration/uomType"],
  GP_MENU_00582: ["CTX_AC/configuration/ranges-of-values"],
  GP_MENU_00566: ["CTX_AC/configuration/periodType"],
  GP_MENU_00570: ["CTX_AC/configuration/detection-type"],
  GP_MENU_00571: ["CTX_AC/configuration/nature-unit-cont-extr"],
  GP_MENU_00572: ["CTX_AC/configuration/periods"],
  GP_MENU_00574: ["CTX_AC/configuration/purpose"],
  GP_MENU_00586: ["CTX_AC/configuration/calculation-formulas"],
  //-- Management   
  GP_MENU_00544: ["CTX_AC/management/queryconfig/data-update/A"],
  GP_MENU_00583: ["CTX_AC/management/accounting-and-extra-accounting-units-indicator/INDICATOR"],
  GP_MENU_00584: ["CTX_AC/management/accounting-and-extra-accounting-units-financial/FINANCIAL"],
  GP_MENU_00585: ["CTX_AC/management/accounting-and-extra-accounting-units-account/ACCOUNT"],
  GP_MENU_00604: ["CTX_AC/management/financial-indicators/FINANCIAL"],
  GP_MENU_00608: ["CTX_AC/management/economic-indicators/ACCOUNT"],
  GP_MENU_00609: ["CTX_AC/management/indicators/INDICATOR/N"],
  GP_MENU_00610: ["CTX_AC/management/reserved-indicators/INDICATOR/Y"],
  GP_MENU_00622: ["CTX_AC/management/indicator-movements/INDICATOR/N"],
  GP_MENU_00623: ["CTX_AC/management/economic-indicator-movements/ACCOUNT"],
  GP_MENU_00624: ["CTX_AC/management/financial-indicator-movements/FINANCIAL"],
  GP_MENU_00625: ["CTX_AC/management/reserved-indicator-movements/INDICATOR/Y"],

  //-- Interoperability
  GP_MENU_00543: ["CTX_AC/interoperability/queryconfig/data-extraction/E"],
  /*--------------------------------------------------*/

  /**
   *  OGGETTI
   */
  /* CTX_WE ------------------------------------------*/
  //-- Configuration
  GP_MENU_00567: ["CTX_WE/configuration/typology-relationships-objectives"],
  GP_MENU_00576: ["CTX_WE/configuration/types-attachments-objectives"],
  //-- Management  
  GP_MENU_00602: ["CTX_WE/management/goals"],
  GP_MENU_00568: ["CTX_WE/management/objective-codes"],
  GP_MENU_00577: ["CTX_WE/management/relationship-objectives"],
  GP_MENU_00580: ["CTX_WE/management/subjects-objectives"],
  GP_MENU_00578: ["CTX_WE/management/measures-objectives"],
  GP_MENU_00579: ["CTX_WE/management/notes-objectives"],
  GP_MENU_00581: ["CTX_WE/management/attachments-objectives"],
  //-- Interoperability
  GP_MENU_00486: ["CTX_WE/interoperability/queryconfig/data-extraction/E"],
  GP_MENU_00589: ["CTX_WE/interoperability/query-configuration"],
  GP_MENU_00590: ["CTX_WE/interoperability/query-configuration-extration/E"],
  GP_MENU_00603: ["CTX_WE/interoperability/analysis-type"],
  /*--------------------------------------------------*/

  /**
     *  SOGGETTI
     */
  /* CTX_PY ------------------------------------------*/
  //-- Configuration
  GP_MENU_00573: ["CTX_PY/configuration/positions-economics"],
  GP_MENU_00575: ["CTX_PY/configuration/typologies-roles-subjects"],
  GP_MENU_00587: ["CTX_PY/configuration/types-relationships-between-subjects"],
  //-- Management
  GP_MENU_00546: ["CTX_PY/management/queryconfig/data-update/A"],
  //-- Interoperability
  GP_MENU_00545: ["CTX_PY/interoperability/queryconfig/data-extraction/E"],
  /*--------------------------------------------------*/

  /**
   *  SISTEMA
   */
  /* CTX_BA ------------------------------------------*/
  // GP_MENU_00334: ["CTX_BA/timesheet"],
  //-- Interoperability 
  GP_MENU_00561: ["CTX_BA/interoperability/interfacciamentoDati"],
  GP_MENU_00592: ["CTX_BA/interoperability/subsystem"],
  GP_MENU_00569: ['CTX_BA/interoperability//subsystem-types'],
  //-- Security
  GP_MENU_00595: ["CTX_BA/security/approval-paths"],
  GP_MENU_00601: ["CTX_BA/security/security-groups"],
  //-- Support
  GP_MENU_00593: ["CTX_BA/support/scheduler"],
  GP_MENU_00594: ["CTX_BA/support/consultingLog"],
  /*--------------------------------------------------*/


};

@Injectable()
export class MenuService {
  constructor() { }

  stateFor(context: FolderMenu, menu: FolderMenu, leaf: LeafMenu): string[] {
    const ref = REFURBISHED_PAGES[leaf.id];
    if (ref) {
      return ref;
    } else {
      if (!context) {
        console.log(menu)
        console.log(leaf)

      }
      return ["legacy", context.id, menu.id, leaf.id];
    }
  }

  getGP_MENU(link: string[]) {
    for (const key in REFURBISHED_PAGES) {
      if (REFURBISHED_PAGES.hasOwnProperty(key)) {
        if (link[0].includes(REFURBISHED_PAGES[key][0])) {
          return key;
        }
      }
    }
    return null; // Ritorna null se il valore non è stato trovato

  }
}
