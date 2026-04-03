import { Component, HostListener } from '@angular/core';
import { RouterEvent } from '@angular/router';

@Component({
  selector: 'app-overlay-screen',
  templateUrl: './overlay-screen.component.html',
  styleUrls: ['./overlay-screen.component.css']
})
export class OverlayScreenComponent {


  ngOnInit(): void {

    this.overlayOff();

    if (window.innerWidth < 650) {
      this.overlayOn()
    }

  }

  @HostListener('window:resize', ['$event'])
  onResize(event: Event | RouterEvent): void {
    if (event instanceof Event) {
      const width = (event.target as Window).innerWidth;
      const height = (event.target as Window).innerHeight;

      if (width > 650) {
        this.overlayOff();
      } else if (width < 650) {
        this.overlayOn();
      }
    }

  }


  overlayOn() {
    document.getElementById("overlay").style.display = "block";
  }

  overlayOff() {
    document.getElementById("overlay").style.display = "none";
  }


}
