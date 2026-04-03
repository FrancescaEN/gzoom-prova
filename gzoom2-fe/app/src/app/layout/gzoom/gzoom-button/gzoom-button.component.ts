import { Component, ViewEncapsulation, booleanAttribute, input } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { GzoomGenericComponentComponent } from '../gzoom-generic/gzoom-generic.component';

@Component({
  selector: 'gzoom-button',
  standalone: true,
  imports: [
    ButtonModule
  ],
  templateUrl: './gzoom-button.component.html',
  styleUrl: './gzoom-button.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomButtonComponent extends GzoomGenericComponentComponent {
  label = input<string>();
  ariaLabel = input<string>();
  icon = input<string>();
  iconPos = input<'left' | 'right'>('left');
  size = input<'small' | 'large' | undefined>(undefined);
  rounded = input(false, { transform: booleanAttribute });
  link = input(false, { transform: booleanAttribute });
}

/*
Usage:
<gzoom-button label="Nuova scheda" icon="pi pi-plus-circle" size="large"></gzoom-button>
<br/>
<br/>
<gzoom-button label="Filtri..." icon="pi pi-sliders-h" outlined></gzoom-button>
<br/>
<br/>
<gzoom-button icon="pi pi-sort-alt" outlined size="small"></gzoom-button>
<br/>
<br/>
<gzoom-button icon="pi pi-pencil" outlined></gzoom-button> &nbsp;
<gzoom-button icon="pi pi-arrow-right" outlined></gzoom-button> &nbsp;
<gzoom-button icon="pi pi-arrow-right" outlined [style]="{'background-color-outlined':'#88f'}"></gzoom-button> &nbsp;
<gzoom-button icon="pi pi-arrow-right"></gzoom-button>
<br/>
<br/>
<gzoom-button label="Annulla" outlined [style]="{'height': '50px', 'width': '300px'}"></gzoom-button> &nbsp;
<gzoom-button label="Salva" (onClick)="testClick()" [style]="{'fontFamily': 'Times'}"></gzoom-button>
<br/>
<br/>
<gzoom-button label="Annulla" outlined disabled></gzoom-button> &nbsp;
<gzoom-button label="Salva" disabled (onClick)="testClick()"></gzoom-button>
 */
