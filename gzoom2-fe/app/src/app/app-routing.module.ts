import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
  {
    path: "",
    loadChildren: () => import("./view/view.module").then((m) => m.ViewModule),
  }, // modulo caricato in maniera lazy
  { path: "**", redirectTo: "/c/dashboard" },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      bindToComponentInputs: true,
      onSameUrlNavigation: "ignore",
      useHash: false,
    }),
  ], // enableTracing: true per abilitare i log della navigazione
  exports: [RouterModule],
})
export class AppRoutingModule {}
