import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InterfacciamentoDatiComponent} from './interfacciamento-dati.component';

const routes: Routes = [
  { path: '', component: InterfacciamentoDatiComponent  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InterfacciamentoDatiRoutingModule { }
