import { Component, Input, OnInit } from '@angular/core';
import { UnitType } from './bodyBarArray';

@Component({
  selector: 'app-information-bar',
  templateUrl: './information-bar.component.html',
  styleUrls: ['./information-bar.component.css']
})
export class InformationBarComponent implements OnInit {

  unitType = UnitType;
  
  @Input() bodyBarArray: any[] = [];

  constructor() { }

  ngOnInit(): void {
  }

}
