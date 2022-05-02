import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AccessService} from "@services/access.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-access-create',
  templateUrl: './access-create.component.html',
  styleUrls: ['./access-create.component.scss']
})
export class AccessCreateComponent implements OnInit {

  nameValue: string = "";
  name = new FormControl("", [Validators.required]);
  maintenance = new FormControl("", []);
  accessForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private accesses: AccessService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.accessForm = this.formBuilder.group({
      name: this.name,
      maintenance: this.maintenance
    });
  }

  onCreate() {
    if (this.accessForm.invalid) {
      return;
    }
    this.accesses.create(
      this.name.value,
      this.maintenance.value
    ).subscribe(
      access => {
        this.router.navigate(["/accesses", access.id]);
      },
      console.error
    );
  }

  getErrorName(): string|null {
    if (this.name.hasError('required')) {
      return 'Vous devez entrer un nom';
    }
    console.log();
    return null;
  }
}
