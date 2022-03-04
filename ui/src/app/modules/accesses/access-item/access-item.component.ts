import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccessService } from '@guards/services/access.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-access-item',
  templateUrl: './access-item.component.html',
  styleUrls: ['./access-item.component.scss']
})
export class AccessItemComponent implements OnInit {

  constructor(private accesses: AccessService, private router: Router) { }
  @Input() accessId: number;
  @Input() m: AccessMatchSensor|AccessMatchUser;

  ngOnInit(): void {
    
  }

  isSensor(m: AccessMatch): m is AccessMatchSensor {
    return (<AccessMatchSensor>this.m).sensor != undefined;
  }

  isUser(m: AccessMatch): m is AccessMatchUser {
    return (<AccessMatchUser>this.m).user != undefined;
  }

  isIn(m: AccessMatch): boolean {
    return m.present;
  }

  isOut(m: AccessMatch): boolean {
    return !m.present;
  }

  isManager(m: AccessMatch): boolean {
    return this.isUser(m) && m.privilege == "MANAGE";
  }

  isViewer(m: AccessMatch): boolean {
    return this.isUser(m) && m.privilege == "VIEW";
  }

  onPromote() {
    const m = this.m;
    if (!this.isUser(m)) return;

    if (m.privilege == "MANAGE") {
      this.accesses.dismissUser(this.accessId, m.user.id).subscribe(
        res=>m.privilege = "VIEW",
        console.error
      );
    } else {
      this.accesses.promoteUser(this.accessId, m.user.id).subscribe(
        res=>m.privilege = "MANAGE",
        console.error
      );
    }
  }

  onView() {
    if (this.isSensor(this.m)) {
      this.router.navigate(["/view", this.m.sensor.id])
    }
  }

  onPresent() {
    const m = this.m;
    if (this.isUser(m)) {
      if (this.isIn(m)) {
        this.accesses.delUser(this.accessId, m.user.id).subscribe(
          res=>m.present = false,
          console.error
        )
      } else {
        this.accesses.addUser(this.accessId, m.user.id).subscribe(
          res=>{
            m.present = true;
            m.privilege = "VIEW";
          },
          console.error
        )
      }

    } else if (this.isSensor(m)) {
      if (this.isIn(m)) {
        this.accesses.delSensor(this.accessId, m.sensor.id).subscribe(
          res=>m.present = false,
          console.error
        )
      } else {
        this.accesses.addSensor(this.accessId, m.sensor.id).subscribe(
          res=>m.present = true,
          console.error
        )
      }
    }

  }

}
