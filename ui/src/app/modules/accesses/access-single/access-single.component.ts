import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccessService } from '@services/access.service';
import { Subscription } from 'rxjs';
import { TriEvent } from '@models/tributton';

@Component({
  selector: 'app-access-single',
  templateUrl: './access-single.component.html',
  styleUrls: ['./access-single.component.scss']
})
export class AccessSingleComponent implements OnInit {
  routeSub: Subscription
  constructor(private accesses: AccessService, private route: ActivatedRoute, private router: Router) { }
  id: number;
  sensor = true;
  user = true;
  in = true;
  out = true;
  query: string = "";
  search: AccessSearch = [];
  access?: Access;

  onSearch(event: Event | string) {
    this.query = event instanceof Event ? (<any>event.target).value || "" : event;
    this.updateSearch();
  }

  updateSearch() {
    this.accesses.searchById(this.id, {
      sensor: this.sensor,
      user: this.user,
      in: this.in,
      out: this.out,
      query: this.query
    }).subscribe(
      res=> {
        this.search = res;
        console.log(res);
      }
    );
  }

  onInOrOut(state: TriEvent) {
    [this.out, this.in] = state.mat;
    this.updateSearch();
  }

  onUserOrSensor(state: TriEvent) {
    [this.sensor, this.user] = state.mat;
    this.updateSearch();
  }

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      let id = +params["id"];
      this.id = id;
      this.accesses.getById(this.id).subscribe(
        access=>{
          this.access = access;
        },
        err=>{
          console.error(err);
          //this.router.navigate(["/accesses"]);
        }
      )
      this.accesses.searchById(this.id, {
        query: ""
      }).subscribe(
        res=> {
          this.search = res;
          console.log(res);
        }
      );
    })
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  getSize() {
    return window.innerHeight - 255;
  }



}
