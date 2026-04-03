import { Component, Input } from '@angular/core';

export class CardDetail {
  title: string;
  description: string;
}

@Component({
  selector: 'gzoom-card-detail',
  templateUrl: './gzoom-card-detail.component.html',
  styleUrls: ['./gzoom-card-detail.component.css']
})
export class GzoomCardDetailComponent {

  @Input() details: CardDetail[] = [];

}
