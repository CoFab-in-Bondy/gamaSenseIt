import { Component, OnInit, ViewChild } from "@angular/core";
import { DialogComponent } from "@components/dialog/dialog.component";
import { ErrorService } from "@services/error.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
})
export class AppComponent implements OnInit {
  title = "gamasenseit";


  @ViewChild(DialogComponent)
  private dialog: DialogComponent;


  constructor(public error: ErrorService) {

  }

  ngOnInit() {
    this.error.errors.subscribe(err => {
      this.dialog.onOpen();
    })

  }
}
